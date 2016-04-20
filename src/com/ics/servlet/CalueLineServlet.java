package com.ics.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ics.pojo.CalueLine;

public class CalueLineServlet extends HttpServlet {

	
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
           this.doPost(request, response);
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		  CalueLine cl = new CalueLine();
		  cl.setStartDate(request.getParameter("startDate"));
		  cl.setEndDate(request.getParameter("endDate"));
		  cl.setDays(Integer.getInteger(request.getParameter("days")));
		  cl.setInterest(Long.parseLong(request.getParameter("rate")));
		  cl.setRateType(request.getParameter("rateType"));
		  cl.setRepayment(Long.parseLong(request.getParameter("repayment")));
		  cl.setPrincipal(Long.parseLong(request.getParameter("principal")));
		  
	}

	public void init() throws ServletException {
		// Put your code here
	}

}
