package com.ics.dao;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import org.apache.commons.dbcp.BasicDataSource;

import com.ics.pojo.CaseCalue;

public class Test {
	
	public static BasicDataSource initData(){  
		ResourceBundle rb = ResourceBundle.getBundle("DBData") ;
		
		BasicDataSource ds = new BasicDataSource();
		ds.setUrl(rb.getString("hsqldburl"));
		ds.setDriverClassName(rb.getString("hsqldbDriver"));
		ds.setUsername(rb.getString("username"));
		ds.setPassword("");
		//ds.setPassword(rb.getString(""));
		ds.setMaxActive(Integer.parseInt(rb.getString("maxActive"))); //最大活动数
		ds.setMaxIdle(Integer.parseInt(rb.getString("maxIdle")));   //最大保存数
		ds.setMaxWait(Integer.parseInt(rb.getString("maxWait")));//最大等待时间
		return ds;
	}
	
	public static void main(String[] args) {
		
		HsqldbServer hsqldbServer = HsqldbServer.getInstance();  
        hsqldbServer.startServer();  
        
		//获取配置文件
        BasicDataSource  ds = initData();
		
		try {
			Connection con = ds.getConnection();
			//String sql = "select * from ICS_CASE_CALCU_T";
			 String sqlStr ="insert into ICS_CASE_CALCU_T(CASE_ID,ARREARAGE,CREATE_DATE,UPDATE_DATE) values('a002',100000,sysdate,sysdate);";  
			Statement sta = con.createStatement();
			sta.executeUpdate(sqlStr);
			/*ResultSet rs = sta.executeQuery(sql);
			 if(rs.next()){
				 System.out.println(rs.getInt("ID"));
			 }*/
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
