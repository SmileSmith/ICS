package com.ics.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.CORBA.portable.ApplicationException;

import com.ics.dao.CalueLineDao;
import com.ics.dao.CaseCalueDao;

public class DelCaseCalueServlet extends HttpServlet {
	
	
	private static final long serialVersionUID = 1L;


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String  calId = request.getParameter("calId");
		
		//删除案件信息
		CaseCalueDao ccDao = new CaseCalueDao();
		ccDao.delCaseCalue(Integer.parseInt(calId));
		
		//删除案件计算信息
		CalueLineDao clDao = new CalueLineDao();
		try {
			clDao.delCalueLine(Integer.parseInt(calId), 0);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
	}

	
	public void init() throws ServletException {
		// Put your code here
	}

}
