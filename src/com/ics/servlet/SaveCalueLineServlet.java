package com.ics.servlet;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.omg.CORBA.portable.ApplicationException;

import com.ics.dao.CalueLineDao;
import com.ics.dao.CaseCalueDao;
import com.ics.pojo.CalueLine;
import com.ics.pojo.CaseCalue;

public class SaveCalueLineServlet extends HttpServlet {

	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//接收页面参数
		String tableData = request.getParameter("param");
		String caseId = request.getParameter("caseId");
		String sCalId = request.getParameter("calId");
		
		int calId =0;
		
		//json格式数据转化为List
		JSONArray  json = JSONArray.fromObject(tableData);
		@SuppressWarnings({ "unchecked", "deprecation", "static-access" })
		List<CalueLine> cllist = json.toList(json, CalueLine.class);
		if(null == cllist || "".equals(cllist)){
			return;
		}
		
		//计算结算结果
		double arrearage = 0d;
	    for(int i = 0; i < cllist.size(); i++){
	    	CalueLine cl = cllist.get(i);
	    	if(cl.getSequence() != 0){
	    		arrearage = Double.valueOf(new DecimalFormat("0.00").format(cl.getIntResidual() + cl.getPriResidual()));
	    		break;
	    	}
	    }
	    CaseCalue cc = new CaseCalue();
	    cc.setCaseId(caseId);
	    cc.setArrearage(arrearage);
		
		CalueLineDao clDao = new CalueLineDao();
		CaseCalueDao ccDao = new CaseCalueDao();
		//案件号为空，该案件为新增，需要保存案件信息
		if(null == sCalId || "".equals(sCalId)){
			calId = ccDao.findCalcuSeq();
			if(calId == 0){
				return ;
			}
			//传递参数
			cc.setCalId(calId);
			
			ccDao.saveCaseCalue(cc);
		} else {
			calId = Integer.parseInt(sCalId);
			
			try {
				//存在案件号  删除原来保存的所有计算记录
				clDao.delCalueLine(calId, 0);
				
				//修改计算结果和更新时间
				cc.setCalId(calId);
				ccDao.updateCaseCalue(cc);
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//保存计算记录
		
		try {
			clDao.saveCalueLine(cllist,calId);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
