package com.ics.dao;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class HsqldbContextListener implements ServletContextListener{
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 启动工程调用 --启动数据库，初始化表格
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		//启动数据库
		HsqldbServer hsqldbServer = HsqldbServer.getInstance();  
        hsqldbServer.startServer();
        //初始化表格
        DBUtil.initTable();
	}  
}
