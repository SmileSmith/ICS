package com.ics.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ics.dao.CaseCalueDao;

public class DelCaseCalueServlet extends HttpServlet {
	
	
	private static final long serialVersionUID = 1L;


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String  calId = request.getParameter("calId");
		CaseCalueDao ccDao = new CaseCalueDao();
		ccDao.delCaseCalue(Integer.parseInt(calId));
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
	}

	
	public void init() throws ServletException {
		// Put your code here
	}

}
