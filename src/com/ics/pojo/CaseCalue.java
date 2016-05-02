package com.ics.pojo;
public class CaseCalue {

	/**
	 * CaseCalue表ID
	 */
	private int calId;
	
	/**
	 * 案件ID
	 */
	private String caseId;
	
	/**
	 * 欠款汇总
	 */
	private Double arrearage;
	
	/**
	 * 创建人
	 */
	private int createBy;
	
	/**
	 * 创建日期
	 */
	private String createDate;
	
	/**
	 *最后更新人
	 */
	private int updateBy;
	
	/**
	 * 最后更新日期
	 */
	private String updateDate;
	public int getCalId() {
		return calId;
	}
	public void setCalId(int calId) {
		this.calId = calId;
	}
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public int getCreateBy() {
		return createBy;
	}
	public void setCreateBy(int createBy) {
		this.createBy = createBy;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public int getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(int updateBy) {
		this.updateBy = updateBy;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public Double getArrearage() {
		return arrearage;
	}
	public void setArrearage(Double arrearage) {
		this.arrearage = arrearage;
	}
}
