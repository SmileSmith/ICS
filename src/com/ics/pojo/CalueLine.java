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
	 * 关联ID
	 */
	 private int relLineId;   
	 
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
	 private Long principal;
	 
	 /**
	 * 利息
	 */
	 private Long interestRate; 
	 
	 /**
	 * 汇率类型 1:四倍同期利率; 2:年利率;3: 月利率;4:天利率
	 */
	 private String rateType;
	 
	 /**
	 * 天数
	 */
	 private int days; 
	 
	 /**
	 * 利息
	 */
	 private Long interest;
	 
	 /**
	 * 还款
	 */
	 private Long repayment;
	 
	 /**
	 * 还款抵扣利息
	 */
	 private Long intRepay;
	 
	 /**
	 * 还款抵扣本金
	 */
	 private Long priRepay;
	 
	 /**
	 * 剩余本金
	 */
	 private Long intResidual;
	 
	 /**
	 * 未偿还利息
	 */
	 private Long payResidual;
	 
	 
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
	public int getRelLineId() {
		return relLineId;
	}
	public void setRelLineId(int relLineId) {
		this.relLineId = relLineId;
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
	public Long getPrincipal() {
		return principal;
	}
	public void setPrincipal(Long principal) {
		this.principal = principal;
	}
	public Long getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(Long interestRate) {
		this.interestRate = interestRate;
	}
	public String getRateType() {
		return rateType;
	}
	public void setRateType(String rateType) {
		this.rateType = rateType;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public Long getInterest() {
		return interest;
	}
	public void setInterest(Long interest) {
		this.interest = interest;
	}
	public Long getRepayment() {
		return repayment;
	}
	public void setRepayment(Long repayment) {
		this.repayment = repayment;
	}
	public Long getIntRepay() {
		return intRepay;
	}
	public void setIntRepay(Long intRepay) {
		this.intRepay = intRepay;
	}
	public Long getPriRepay() {
		return priRepay;
	}
	public void setPriRepay(Long priRepay) {
		this.priRepay = priRepay;
	}
	public Long getIntResidual() {
		return intResidual;
	}
	public void setIntResidual(Long intResidual) {
		this.intResidual = intResidual;
	}
	public Long getPayResidual() {
		return payResidual;
	}
	public void setPayResidual(Long payResidual) {
		this.payResidual = payResidual;
	}
	 
}
