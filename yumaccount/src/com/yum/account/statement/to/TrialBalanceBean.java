package com.yum.account.statement.to;

public class TrialBalanceBean {
	
	private String accountName;
	private String accountCode;
	private String totalDeb;
	private String totalCre;
	private String level;
	private String leafs;


	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountCode() {
		return accountCode;
	}
	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}
	public String getTotalDeb() {
		return totalDeb;
	}
	public void setTotalDeb(String totalDeb) {
		this.totalDeb = totalDeb;
	}
	public String getTotalCre() {
		return totalCre;
	}
	public void setTotalCre(String totalCre) {
		this.totalCre = totalCre;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getLeafs() {
		return leafs;
	}
	public void setLeafs(String leafs) {
		this.leafs = leafs;
	}
	
}
