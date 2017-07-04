package com.ics.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.ics.pojo.CalueLine;

public class OpeCalueLineServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
          this.doPost(request, response);
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//接收页面参数
		String tableData = request.getParameter("param");
        String type = request.getParameter("type");//用来判断是ADD还是DEL
		
		
		//json格式数据转化为List
		JSONArray  json = JSONArray.fromObject(tableData);
		@SuppressWarnings({ "unchecked", "deprecation", "static-access" })
		List<CalueLine> cllist = json.toList(json, CalueLine.class);
		if(null == cllist || "".equals(cllist)){
			return;
		}
		 
		CalueLineServlet clServlet = new CalueLineServlet();
		
		
		List<CalueLine> callist = new ArrayList<CalueLine>();
		
		List<CalueLine> resultList = new ArrayList<CalueLine>();
		
		int relLineNo = -1;
		int beforeLineNo = -1;
		String Flag = "noRelativeFlag";
		int beforePrimaryIndex = -1;
		
		for(int i=0;i<cllist.size();i++){
			if(i==0){
				callist.add(cllist.get(0));
			} else {
				callist.add(cllist.get(i));

				if(cllist.get(i).getSequence()==0){
					//关联行
					Flag = "realtiveByPricipal";
					int j;
					for(j=i-1;j>=0;j--){	
						if(0 != resultList.get(j).getSequence()){
							if(-1 == relLineNo){
								relLineNo = j;
								beforeLineNo = j;
							} else{
								beforeLineNo = j;
								break;
							}
						}					
					}
					
					for(int k=i-1;k>=beforeLineNo;k--){
						callist.add(resultList.get(k));
					}
					
				} else {
					int j;
					for(j=i-1;j>=0;j--){	
						if(0 != resultList.get(j).getSequence()){
							beforeLineNo = j;
							break;
						}					
					}
					callist.add(resultList.get(j));
					//主行默认用上一行主行的剩余本金刷掉
					callist.get(0).setPrincipal(resultList.get(beforePrimaryIndex).getPriResidual());
				}

			}
			callist = clServlet.calculator(callist, Flag);
			resultList.add(callist.get(0));
			if(callist.get(0).getPriResidual() != 0d){
				beforePrimaryIndex = resultList.size()-1;	
			}
			callist.clear();
			Flag = "noRelativeFlag";
		}
		
/*		if("Add".equals(type)){
			for(int i = 0;i < cllist.size(); i++){
			   CalueLine pricl = cllist.get(i);//获取循环数据
				
				Double repayment_total = 0d;  	//总还款
				Double interest_total = 0d;		//总利息
				Double pricipal_total = 0d;  	//总本金		
				
				if(i == 0){
					clServlet.calInterest(pricl);//计算利息
					clServlet.calRepayResidualData(pricl); //计算数据
				    repayment_total = pricl.getRepayment();  	//总还款
					interest_total = pricl.getInterest();		//总利息
					pricipal_total = pricl.getPrincipal();  	//总本金	
					
					for(int j = i+1;j < cllist.size(); j++ ){
						CalueLine cl = cllist.get(j);
						if(j == i+1 & cl.getSequence() != 0){
							break;
						}
						if(cl.getSequence() != 0 ){
							//计算行的计算结果
							CalueLine cline = new CalueLine();
							cline.setRepayment(repayment_total);
							cline.setInterest(interest_total);
							cline.setPrincipal(pricipal_total);
							clServlet.calRepayResidualData(cline);
							pricl.setIntRepay(cline.getIntRepay());
							pricl.setPriRepay(cline.getPriRepay());
							pricl.setIntResidual(cline.getIntResidual());
							pricl.setPriResidual(cline.getPriResidual());
							break;
							
						}
						if(cl.getRepayment() == 0){
							cl.setPrincipal(pricl.getPrincipal());
							clServlet.calInterest(cl);//计算利息
							cl.setPrincipal(0d);
						} else {
							clServlet.calInterest(cl);//计算利息
						}
						cl.setSequence(0);  //设置所有关联行的sequence = 0
						repayment_total = Double.valueOf(new DecimalFormat("0.00").format(repayment_total + cl.getRepayment()));
						interest_total = Double.valueOf(new DecimalFormat("0.00").format(interest_total + cl.getInterest()));
						pricipal_total = Double.valueOf(new DecimalFormat("0.00").format(pricipal_total + cl.getPrincipal()));
					}
				} else {
					//处理关联行的数据
					if(pricl.getSequence() != 0 ){
						Double oldInterest = 0d; //上个主行的剩余利息
						
						//获取上一个主行的剩余本机和剩余利息.sequence
						for(int j =i-1; j>=0 ; j--){
							CalueLine cl = cllist.get(j);
							if(cl.getSequence() != 0){
								pricl.setPrincipal(cl.getPriResidual());
								oldInterest =cl.getIntResidual();
								pricl.setSequence(cl.getSequence()+1);
								break;
							}
						}
						
						
						clServlet.calInterest(pricl);//计算利息
						pricl.setInterest(Double.valueOf(new DecimalFormat("0.00").format(pricl.getInterest() + oldInterest)));
						clServlet.calRepayResidualData(pricl); //计算数据
						pricl.setInterest(Double.valueOf(new DecimalFormat("0.00").format(pricl.getInterest() - oldInterest)));
						repayment_total = pricl.getRepayment();  	//总还款
					    interest_total = pricl.getInterest();		//总利息
						pricipal_total = pricl.getPrincipal();  	//总本金	
						
						//判断是否有关联行，并计算数据
						for(int j = i+1;j < cllist.size(); j++){
							CalueLine cl = cllist.get(j);
							if(j == i+1 & cl.getSequence() != 0){
								break;
							}
							if(cl.getSequence() != 0){
								
								//计算行的计算结果
								CalueLine cline = new CalueLine();
								cline.setRepayment(repayment_total);
								cline.setInterest(Double.valueOf(new DecimalFormat("0.00").format(interest_total + oldInterest)));
								cline.setPrincipal(pricipal_total);
								cl.setPrincipal(pricipal_total);
								clServlet.calRepayResidualData(cline);
								pricl.setIntRepay(cline.getIntRepay());
								pricl.setPriRepay(cline.getPriRepay());
								pricl.setIntResidual(cline.getIntResidual());
								pricl.setPriResidual(cline.getPriResidual());
								break;
								
							}
							
							if(cl.getRepayment() == 0){
								cl.setPrincipal(pricl.getPrincipal());
								clServlet.calInterest(cl);//计算利息
								cl.setPrincipal(0d);
							} else {
								clServlet.calInterest(cl);//计算利息
							}
							cl.setSequence(0);  //设置所有关联行的sequence = 0
							repayment_total = Double.valueOf(new DecimalFormat("0.00").format(repayment_total + cl.getRepayment()));
							interest_total = Double.valueOf(new DecimalFormat("0.00").format(interest_total + cl.getInterest()));
							pricipal_total = Double.valueOf(new DecimalFormat("0.00").format(pricipal_total + cl.getPrincipal()));
						}
					}
				}
			}
		}*/
		
		JSONArray jsonData =  JSONArray.fromObject(resultList);
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(jsonData);
		out.flush();
		out.close();
	}

}
