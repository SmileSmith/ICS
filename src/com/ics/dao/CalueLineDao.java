package com.ics.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.omg.CORBA.portable.ApplicationException;

import com.ics.pojo.CalueLine;

public class CalueLineDao {
	
	/**
	 * 根据案件号查询出所有计算数据
	 * @param calId
	 * @return
	 */
	public List<CalueLine> findCalueLine(int calId){
		//连接数据库
		Connection con = DBUtil.initCon();
		if(con == null){
			return null;
		}
	
		List<CalueLine> clList = new ArrayList<CalueLine>();
		PreparedStatement ps = null;
		String sql = "SELECT * FROM  ICS_CALCU_LINE_T WHERE CAL_ID = ? ORDER BY  INDEX";
		try {
			ps = con.prepareStatement(sql);
			ps.setInt(1, calId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				CalueLine cl = new CalueLine();
				cl.setLineId(rs.getInt("LINE_ID"));
				cl.setCalId(rs.getInt("CAL_ID"));
				cl.setIndex(rs.getInt("INDEX"));
				cl.setPrilineid(rs.getInt("PRI_LINE_ID"));
				cl.setSequence(rs.getInt("SEQUENCE"));
				cl.setStartDate(rs.getString("START_DATE"));
				cl.setEndDate(rs.getString("END_DATE"));
				cl.setPrincipal(rs.getDouble("PRINCIPAL"));
				cl.setRate(rs.getDouble("RATE"));
				cl.setRateType(rs.getString("RATE_TYPE"));
				cl.setDays(rs.getInt("DAYS"));
				cl.setRepayment(rs.getDouble("REPAYMENT"));
				cl.setInterest(rs.getDouble("INTEREST"));
				cl.setIntRepay(rs.getDouble("INT_REPAY"));
				cl.setPriRepay(rs.getDouble("PRI_REPAY"));
				cl.setIntResidual(rs.getDouble("INT_RESIDUAL"));
				cl.setPriResidual(rs.getDouble("PRI_RESIDUAL"));
				clList.add(cl);
			}
			
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
		return clList;
	}
	
	/**
	 * 保存表格数据
	 * @param cllist
	 * @param calId
	 * @throws ApplicationException
	 */
	public void saveCalueLine(List<CalueLine> cllist,int calId) throws ApplicationException{
		//连接数据库
		Connection con = DBUtil.initCon();
		if(con == null){
			return ;
		}
		String sql = "insert into ICS_CALCU_LINE_T(LINE_ID,CAL_ID,INDEX,PRI_LINE_ID,SEQUENCE,START_DATE,END_DATE,PRINCIPAL,RATE,RATE_TYPE,DAYS," 
				      + "INTEREST,REPAYMENT,INT_REPAY,PRI_REPAY,INT_RESIDUAL,PRI_RESIDUAL) " 
				      + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
		PreparedStatement ps = null;
		try {
			for(CalueLine cl: cllist){
				ps = con.prepareStatement(sql);
				ps.setInt(1, cl.getLineId());
				ps.setInt(2, calId);
				ps.setInt(3, cl.getIndex());
				ps.setInt(4, cl.getPrilineid());
				ps.setInt(5,cl.getSequence());
				ps.setString(6,cl.getStartDate());
				ps.setString(7,cl.getEndDate());
				ps.setDouble(8, cl.getPrincipal());
				ps.setDouble(9, cl.getRate());
				ps.setString(10,cl.getRateType());
				ps.setInt(11, cl.getDays());
				ps.setDouble(12, cl.getInterest());
				ps.setDouble(13, cl.getRepayment());
				ps.setDouble(14, cl.getIntRepay());
				ps.setDouble(15, cl.getPriRepay());
				ps.setDouble(16, cl.getIntResidual());
				ps.setDouble(17, cl.getPriResidual());
				ps.executeUpdate();
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(null != ps){
					ps.close();					
				}
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	} 
	
	/**
	 * 删除案件计算信息
	 * @param calId
	 * @param lineId
	 * @throws ApplicationException
	 */
	public void delCalueLine(int calId,int lineId)throws ApplicationException{
		//连接数据库
		Connection con = DBUtil.initCon();
		if(con == null){
			return ;
		}
		
		PreparedStatement ps = null;
		String sql = "DELETE FROM ICS_CALCU_LINE_T WHERE 1=1 ";
		try {
			
			//根据案件号删除案件计算信息
			if(calId != 0){
				sql =sql + " AND CAL_ID= ?";
				ps = con.prepareStatement(sql);
				ps.setInt(1, calId);
				ps.executeUpdate();
				return;
			}
			//根据案件计算主键删除案件计算信息
			if(lineId != 0){
				sql =sql + " AND LINE_ID = ?";
				ps = con.prepareStatement(sql);
				ps.setInt(1, lineId);
				ps.executeUpdate();
				return;
			}
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
	 * 查询序列  ICS_CALCU_LINE_S
	 * @return seq
	 */
	public int findLineSeq(){
		//连接数据库
		Connection con = DBUtil.initCon();
		if(con == null){
			return 0;
		}
		int seq = 0;
		
		Statement st = null;
		String sql ="SELECT NEXT VALUE FOR  ICS_CALCU_LINE_S FROM DUAL_SEQ;";
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
}
