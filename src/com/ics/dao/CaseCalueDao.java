package com.ics.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.ics.pojo.CaseCalue;

public class CaseCalueDao {

	public List<CaseCalue> findCaseCalue(String caseID,String type){
		//连接数据库
		Connection con = DBUtil.initCon();
		if(con == null){
			return null;
		}
		//接收查询返回参数
		ResultSet Rset = null;
		List<CaseCalue>  ccList= new ArrayList<CaseCalue>();
		try {
			String sql = "SELECT * FROM  ICS_CASE_CALCU_T ";
			
			//是否查询全部案件号，calId==0时，查询所有案件号
			if("one".equals(type)){
				sql+= "WHERE CASE_ID =?";
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, caseID);
				Rset = ps.executeQuery();
			} else {
				Statement st = con.createStatement();
				Rset= st.executeQuery(sql);
			}
			while(Rset.next()){
				CaseCalue cc = new CaseCalue();
				cc.setCalId(Rset.getInt("CAL_ID"));
				cc.setCaseId(Rset.getString("CASE_ID"));
				cc.setArrearage(Rset.getLong("ARREARAGE"));
				cc.setCreateDate(Rset.getString("CREATE_DATE"));
				cc.setUpdateDate(Rset.getString("UPDATE_DATE"));
				ccList.add(cc);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ccList;
	}
	
	public void delCaseCalue(int calId){
		//连接数据库
		Connection con = DBUtil.initCon();
		if(con == null){
			return ;
		}
		
		try {
			String sql = "delete from ICS_CASE_CALCU_T where CAL_ID = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, calId);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
