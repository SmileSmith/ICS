package com.ics.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.ics.dao.CalueLineDao;
import com.ics.pojo.CalueLine;

public class CalueLineServlet extends HttpServlet {

	
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
           this.doPost(request, response);
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
			String inJsonData = request.getParameter("param");
			String realtiveFlag = request.getParameter("realtiveFlag");
			
			//json格式数据转化为List
			JSONArray  json = JSONArray.fromObject(inJsonData);
			@SuppressWarnings({ "unchecked", "deprecation", "static-access" })
			List<CalueLine> cllist = json.toList(json, CalueLine.class);
			
			
			if(null != cllist && cllist.size() != 0){
				
				cllist = this.calculator(cllist, realtiveFlag);
				
				JSONArray outJsonData = JSONArray.fromObject(cllist);
				response.setCharacterEncoding("UTF-8");
				response.setContentType("Application/json ;charset=utf-8");
				PrintWriter out  = response.getWriter();
				out.print(outJsonData);
				out.flush();
				out.close();
		}
			
	}

	public void init() throws ServletException {
		// Put your code here
	}
	

	public List<CalueLine> calculator (List<CalueLine>  cllist, String Flag){
		CalueLine newLine = cllist.get(0);
		CalueLine relLine = new CalueLine();
		CalueLine beforeLine = new CalueLine();
		
		CalueLine calLine = new CalueLine();
			calLine.setDays(newLine.getDays());
			calLine.setRate(newLine.getRate());
			calLine.setRateType(newLine.getRateType());
		
		//查询新增行的lineId
		CalueLineDao clDao = new CalueLineDao();
		if(0 == newLine.getLineId()){
			newLine.setLineId(clDao.findLineSeq());			
		}

		
		//计算金额返回
		if(Flag.equals("realtiveByPricipal")){
		    //表格中有数据  新增行是关联行
			int i = 0;
			int relLineNo = -1;     //关联主行在cllist中的位置
			int beforeLineNo = -1;  //前一主行数据在cllist中的位置
			
			double tempInterest = 0d;//所有被关联的行的利息
			double tempPrincipal= 0d;//所有被关联的行的利息
			
			
			//确定关联行和前一行数据在cllist中的位置
			for(int n=0;n<cllist.size();n++){
				if(0 != cllist.get(n).getSequence()){
					if(-1 == relLineNo){
						relLineNo = n;
					} else{
						beforeLineNo = n;
					}
				} else {
					if(-1 == relLineNo){
						if(n!=0)tempInterest += cllist.get(n).getInterest();
						tempPrincipal += cllist.get(n).getPrincipal();
					}
				}				
			}
/*			for(CalueLine cl: cllist){

				i++;
			}*/
			//取主行和关联行
			relLine = cllist.get(relLineNo);
			if(beforeLineNo != -1) {
				beforeLine = cllist.get(beforeLineNo);
			}
			//计算利息的本金
			double calPricipal = 0d == newLine.getPrincipal()?relLine.getPrincipal():newLine.getPrincipal();
			calLine.setPrincipal(calPricipal);
			//计算利息
			this.calInterest(calLine);
			//计算行的数据写入新增行
			newLine.setInterest(calLine.getInterest());
			if(0 == newLine.getPrilineid()){
				newLine.setPrilineid(relLine.getLineId());
			}
			newLine.setSequence(0);
			
			//计算repay/residual数据，本金和利息要重新算
			calLine.setRepayment(newLine.getRepayment() + relLine.getRepayment());
			calLine.setPrincipal(tempPrincipal + relLine.getPrincipal());
			calLine.setInterest(newLine.getInterest()+tempInterest + relLine.getInterest() + beforeLine.getIntResidual());
			this.calRepayResidualData(calLine);
			
			//计算行的数据写入关联行
			relLine.setIntRepay(calLine.getIntRepay());
			relLine.setPriRepay(calLine.getPriRepay());
			relLine.setIntResidual(calLine.getIntResidual());
			relLine.setPriResidual(calLine.getPriResidual());			

		} else {
			//非关联行
			if(cllist.size() == 1){
				//表格数据为空的情况
				this.calInterest(newLine);
				this.calRepayResidualData(newLine);
				//初始化表格序列
				newLine.setSequence(1);

			}  else {
				beforeLine = cllist.get(1);
				//表格中有数据  新增行不是关联行
				this.calInterest(newLine);
				
				calLine.setInterest(newLine.getInterest() + beforeLine.getIntResidual());
				calLine.setPrincipal(newLine.getPrincipal());
				calLine.setRepayment(newLine.getRepayment());
				this.calRepayResidualData(calLine);
				
				//计算行的数据写入新增行
				newLine.setIntRepay(calLine.getIntRepay());
				newLine.setPriRepay(calLine.getPriRepay());
				newLine.setIntResidual(calLine.getIntResidual());
				newLine.setPriResidual(calLine.getPriResidual());
				newLine.setSequence(beforeLine.getSequence()+1);
			}

		}
		return cllist;
	}
	
	/**
	 * 计算利息
	 * @param calLine
	 */
	public void calInterest (CalueLine calLine){
		double pricipal = calLine.getPrincipal(); //计算本金
		double interest = 0d;		//计算利息
		BigDecimal  daysRate = null; //计算天利率
		
		//算出总天利率
		String rateType = calLine.getRateType();
		if(null != rateType && rateType != ""){
			if("rateFourTime".equals(rateType)){
				daysRate  = new BigDecimal((calLine.getRate()/100/4)/365*calLine.getDays());
			} else if("rateByYear".equals(rateType)){
				daysRate = new BigDecimal(calLine.getRate()/100/365*calLine.getDays());
			} else {
				daysRate = new BigDecimal(calLine.getRate()/100/30*calLine.getDays());
			}
		}
		//用新增行本金算出新增行利息（新增行本金为空的情况：realtivByRate，新增行本金取关联行）
		interest = new BigDecimal(pricipal).multiply(daysRate).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); 
		
		calLine.setInterest(interest);
		
	}
	
	public void calRepayResidualData (CalueLine calLine){
		
		Double repayment_total = calLine.getRepayment();  	//总还款
		Double interest_total = calLine.getInterest();		//总利息
		Double pricipal_total = calLine.getPrincipal();  	//总本金			
		
		if((repayment_total - interest_total) >= 0){
			//还款金额大于等于利息
			calLine.setIntRepay(Double.valueOf(new DecimalFormat("0.00").format(interest_total)));
			calLine.setPriRepay (Double.valueOf(new DecimalFormat("0.00").format(repayment_total - interest_total)));
			calLine.setIntResidual(0d);
			calLine.setPriResidual(Double.valueOf(new DecimalFormat("0.00").format(pricipal_total-(repayment_total - interest_total))));
		 } else {
			 //还款金额小于于利息
			 calLine.setIntRepay(Double.valueOf(new DecimalFormat("0.00").format(repayment_total)));
			 calLine.setPriRepay(0d);
			 calLine.setIntResidual(Double.valueOf(new DecimalFormat("0.00").format(interest_total - repayment_total)));
			 calLine.setPriResidual(Double.valueOf(new DecimalFormat("0.00").format(pricipal_total)));
		 } 		
	}
	
	
}
