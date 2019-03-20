package com.yum.account.slip.to;

import com.yum.account.slip.to.JournalDetailBean;
import com.yum.base.to.BaseBean;

public class JournalBean extends BaseBean{
	private String journalCode;
	private String slipNo;
	private String balanceDivision;
	private String accountCode;
	private String accountName;
	private String customerCode;
	private String customerName;
	private String price;
	private String voucherCode;
	private String voucherName;
	private String descCode;
	private String descName;
	private JournalDetailBean journalDetailBean;

	public JournalDetailBean getJournalDetailBean() {
		return journalDetailBean;
	}

	public void setJournalDetailBean(JournalDetailBean journalDetailBean) {
		this.journalDetailBean = journalDetailBean;
	}



	public String getVoucherCode() {
		return voucherCode;
	}

	public void setVoucherCode(String voucherCode) {
		this.voucherCode = voucherCode;
	}

	public String getVoucherName() {
		return voucherName;
	}

	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
	}

	public String getDescName() {
		return descName;
	}

	public void setDescName(String descName) {
		this.descName = descName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getBalanceDivision() {
		return balanceDivision;
	}

	public void setBalanceDivision(String balanceDivision) {
		this.balanceDivision = balanceDivision;
	}

	public String getSlipNo() {
		return slipNo;
	}

	public void setSlipNo(String slipNo) {
		this.slipNo = slipNo;
	}

	public String getJournalCode() {
		return journalCode;
	}

	public void setJournalCode(String journalCode) {
		this.journalCode = journalCode;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getDescCode() {
		return descCode;
	}

	public void setDescCode(String descCode) {
		this.descCode = descCode;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

}
