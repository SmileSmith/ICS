package com.ics.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.omg.CORBA.portable.ApplicationException;

import com.ics.pojo.CaseCalue;

public class CaseCalueDao {

	/**
	 * 查询案件信息
	 * @return
	 */
	public List<CaseCalue> findCaseCalue(String caseID,String type){
		//连接数据库
		Connection con = DBUtil.initCon();
		if(con == null){
			return null;
		}
		//接收查询返回参数
		ResultSet Rset = null;
		PreparedStatement ps = null;
		Statement st =null; 
		List<CaseCalue>  ccList= new ArrayList<CaseCalue>();
		try {
			String sql = "SELECT * FROM  ICS_CASE_CALCU_T ";
			
			//是否查询全部案件号，calId==0时，查询所有案件号
			if("one".equals(type)){
				sql+= "WHERE CASE_ID =?";
				ps = con.prepareStatement(sql);
				ps.setString(1, caseID);
				Rset = ps.executeQuery();
			} else {
				st = con.createStatement();
				Rset= st.executeQuery(sql);
			}
			while(Rset.next()){
				CaseCalue cc = new CaseCalue();
				cc.setCalId(Rset.getInt("CAL_ID"));
				cc.setCaseId(Rset.getString("CASE_ID"));
				cc.setArrearage(Rset.getDouble("ARREARAGE"));
				cc.setCreateDate(Rset.getString("CREATE_DATE"));
				cc.setUpdateDate(Rset.getString("UPDATE_DATE"));
				ccList.add(cc);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally {
			try {
				if("one".equals(type)){
					ps.close();
				} else {
					st.close();
				}
				
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return ccList;
	}
	
	/**
	 * 删除案件信息
	 * @return
	 */
	public void delCaseCalue(int calId){
		//连接数据库
		Connection con = DBUtil.initCon();
		if(con == null){
			return ;
		}
		
		PreparedStatement ps = null;
		try {
			String sql = "delete from ICS_CASE_CALCU_T where CAL_ID = ?";
			ps = con.prepareStatement(sql);
			ps.setInt(1, calId);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		    }
		}
	}
	
	
	/**
	 * 查询序列  ICS_CASE_CALCU_S
	 * @return seq
	 */
	public int findCalcuSeq(){
		//连接数据库
		Connection con = DBUtil.initCon();
		if(con == null){
			return 0;
		}
		int seq = 0;
		
		Statement st = null;
		String sql ="SELECT NEXT VALUE FOR  ICS_CASE_CALCU_S FROM DUAL_SEQ;";
		try {
			st  = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			rs.next();
			seq =rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				st.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		    }
		}
		return seq;
	}
	
	
	/**
	 * 保存案件信息
	 * @return 
	 */
	public void saveCaseCalue(CaseCalue cc){
		//连接数据库
		Connection con = DBUtil.initCon();
		if(con == null){
			return ;
		}
		
		PreparedStatement ps =null;
		String sql ="INSERT INTO ICS_CASE_CALCU_T(CAL_ID,CASE_ID,ARREARAGE,CREATE_DATE,UPDATE_DATE) VALUES (?,?,?,SYSDATE,SYSDATE);";
		try {
			ps  = con.prepareStatement(sql);
			ps.setInt(1,cc.getCalId());
			ps.setString(2,cc.getCaseId());
			ps.setDouble(3,cc.getArrearage());
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
		    }
		}
	}
	
	
	
	/**
	 * 修改案件信息
	 * @return 
	 */
	public void updateCaseCalue(CaseCalue cc){
		//连接数据库
		Connection con = DBUtil.initCon();
		if(con == null){
			return ;
		}
		
		PreparedStatement ps =null;
		String sql ="UPDATE ICS_CASE_CALCU_T SET ARREARAGE = ? ,UPDATE_DATE = SYSDATE WHERE CAL_ID = ?;";
		try {
			ps  = con.prepareStatement(sql);
			ps.setDouble(1,cc.getArrearage());
			ps.setInt(2,cc.getCalId());
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
		    }
		}
	}
}
