package com.ics.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * 连接数据库
 */
public class DBUtil {
	
	//连接池
	public static Connection initCon(){  
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
		Connection con = null;
		try {
			 con = ds.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}
	
	//JDBC
	public static void initTable(){  
        //创建数据库表和插入数据  
		Statement st = null ;
		Connection con = DBUtil.initCon();

  		try { 
  			st = con.createStatement();
            if(!hasTable(con,"ICS_CASE_CALCU_T")){
            	String  calcuStr="create memory table ICS_CASE_CALCU_T( CAL_ID integer  not null ,"
						+"CASE_ID varchar(20) not null,"
						+"ARREARAGE decimal(24,2),"
						+"CREATE_DATE date,"
						+"CREATE_BY integer,"
						+ "UPDATE_DATE date,"
						+" UPDATE_BY integer,"
						+" primary key (CAL_ID)"
						+");";
            	st.execute(calcuStr);
            }
            if(!hasTable(con,"ICS_CALCU_LINE_T")){
            	String 	lineStr="create table ICS_CALCU_LINE_T"
						+"("
						+"  LINE_ID integer not null ,"
						+"  CAL_ID integer,"
						+"  INDEX integer,"
						+"  PRI_LINE_ID integer,"
						+"  SEQUENCE integer,"
						+"  START_DATE varchar(8),"
						+"  END_DATE varchar(8),"
						+"  PRINCIPAL decimal(24,2),"
						+"  RATE decimal(12,4),"
						+"  RATE_TYPE char(1),"
						+"  DAYS integer,"
						+"  INTEREST decimal(24,2),"
						+"  REPAYMENT decimal(24,2),"
						+"  INT_REPAY decimal(24,2),"
						+"  PRI_REPAY decimal(24,2),"
						+"  INT_RESIDUAL decimal(24,2),"
						+"  PRI_RESIDUAL decimal(24,2),"
						+"  primary key (LINE_ID)"
						+");";
            	st.execute(lineStr);
		            }
            if(!hasTable(con,"DUAL_SEQ")){
            	
            	if(!hasSequence(con,"ICS_CASE_CALCU_S")){
            		String 	seqStr1="CREATE SEQUENCE  ICS_CASE_CALCU_S START WITH 1 INCREMENT BY 1;";
                	st.execute(seqStr1);
            	}
            	
            	if(!hasSequence(con,"ICS_CALCU_LINE_S")){
            		String 	seqStr2="CREATE SEQUENCE  ICS_CALCU_LINE_S START WITH 1 INCREMENT BY 1;";
                	st.execute(seqStr2);
            	}
            	
            	String 	dualStr="CREATE TABLE DUAL_SEQ ( ID BIGINT );";
            	st.execute(dualStr);
            	
            	String insetSql = "INSERT INTO DUAL_SEQ(ID) VALUES(0);";
            	st.executeUpdate(insetSql);

		            }
		 } catch (Exception e) {  
		            e.printStackTrace(System.err);  
		        } finally {  
		            try {  
		                if (st != null) {  
		                    st.close(); 
		                    con.close();
		                }  
		            } catch (SQLException e) {}  
		        }         
    }  
    
	
	/**
	   * 根据表名,判断数据库表是否存在
	   * @param tableName
	   * @return true:存在该表，false:不存在该表
	   */
	  public static boolean hasTable(Connection con, String tableName) {
		  String[] strs = { "TABLE" };  
		  //判断某一个表是否存在
		  boolean result = false;                                                       
	      try{         
	    	   //获取查找结果
		       ResultSet set = con.getMetaData().getTables(null, null, tableName, strs);
		      //如果查找结果不为空，则说明存在该表
		       while (set.next()) {                                                            
		              result = true;//将返回结果置为true                                                     
		       }
	      } catch(Exception e){
	       e.printStackTrace();
	      } 
	      
	      return result;
	  }
	  
	  
	  public static boolean hasSequence(Connection con, String tableName){
		  //判断一个表是否存在
		  boolean result = false;
		  
		  PreparedStatement ps = null ;
		  String sql = "SELECT * FROM  INFORMATION_SCHEMA.SYSTEM_SEQUENCES WHERE SEQUENCE_NAME = ?";
		  try {
			  ps =con.prepareStatement(sql);
			  ps.setString(1, tableName);
			  ResultSet rs =ps.executeQuery();
			  if(rs.next()){
				  result =true ;  
			  }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		  
		  return result;
	  }
	  
}
