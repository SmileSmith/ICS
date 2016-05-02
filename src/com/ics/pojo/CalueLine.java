package com.ics.pojo;

public class CalueLine {

	/**
	 * CalueLine表ID
	 */
	 private int lineId;
	 
	 /**
	 * 案件ID
	 */
	 private int calId;
	 
	 /**
	  * 序号，对应表格序号
	  */
	 private int sequence;
	 
	 /**
	  * 序号，对应表格行序号
	  */
	 private int index;
	 /**
	 * 关联ID
	 */
	 private int prilineid; 
	 
	 /**
	 * 开始时间
	 */
	 private String startDate;
	 
	 /**
	 * 结束时间
	 */
	 private String endDate;
	 
	 /**
	 * 本金
	 */
	 private double principal;
	 
	 /**
	 * 利率
	 */
	 private double rate;
	 
	 /**
	 * 利率类型 1:四倍同期利率; 2:年利率;3: 月利率;4:天利率
	 */
	 private String rateType;
	 
	 /**
	 * 天数
	 */
	 private int days; 
	 
	 /**
	 * 利息
	 */
	 private double interest;
	 
	 /**
	 * 还款
	 */
	 private double repayment;
	 
	 /**
	 * 还款抵扣利息
	 */
	 private double intRepay;
	 
	 /**
	 * 还款抵扣本金
	 */
	 private double priRepay;
	 
	 /**
	 * 未偿还利息
	 */
	 private double intResidual;
	 
	 /**
	 * 剩余本金
	 */
	 private double priResidual;
	 
	 
	public int getLineId() {
		return lineId;
	}
	public void setLineId(int lineId) {
		this.lineId = lineId;
	}
	public int getCalId() {
		return calId;
	}
	public void setCalId(int calId) {
		this.calId = calId;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
	public int getPrilineid() {
		return prilineid;
	}
	public void setPrilineid(int prilineid) {
		this.prilineid = prilineid;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public double getPrincipal() {
		return principal;
	}
	public void setPrincipal(double principal) {
		this.principal = principal;
	}
	public String getRateType() {
		return rateType;
	}
	public void setRateType(String rateType) {
		this.rateType = rateType;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public double getInterest() {
		return interest;
	}
	public void setInterest(double interest) {
		this.interest = interest;
	}
	public double getRepayment() {
		return repayment;
	}
	public void setRepayment(double repayment) {
		this.repayment = repayment;
	}
	public double getIntRepay() {
		return intRepay;
	}
	public void setIntRepay(double intRepay) {
		this.intRepay = intRepay;
	}
	public double getPriRepay() {
		return priRepay;
	}
	public void setPriRepay(double priRepay) {
		this.priRepay = priRepay;
	}
	public double getIntResidual() {
		return intResidual;
	}
	public void setIntResidual(double intResidual) {
		this.intResidual = intResidual;
	}
	public double getPriResidual() {
		return priResidual;
	}
	public void setPriResidual(double priResidual) {
		this.priResidual = priResidual;
	}
	 
}
