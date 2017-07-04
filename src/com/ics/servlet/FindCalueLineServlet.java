package com.ics.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.ics.dao.CalueLineDao;
import com.ics.pojo.CalueLine;

public class FindCalueLineServlet extends HttpServlet {

	
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//不缓存数据
		response.setHeader( "Pragma", "no-cache" );   
		response.setDateHeader("Expires", 0);   
		response.addHeader( "Cache-Control", "no-cache" );//浏览器和缓存服务器都不应该缓存页面信息
		response.addHeader( "Cache-Control", "no-store" );//请求和响应的信息都不应该被存储在对方的磁盘系统中；    
		response.addHeader( "Cache-Control", "must-revalidate" );
		
		int calId = Integer.parseInt(request.getParameter("calId"));
		CalueLineDao clDao = new CalueLineDao();
		List<CalueLine> clList =clDao.findCalueLine(calId);
		
		JSONArray jsonData =  JSONArray.fromObject(clList);
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(jsonData);
		out.flush();
		out.close();
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}

	public void init() throws ServletException {
		// Put your code here
	}

}
