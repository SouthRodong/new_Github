package com.yum.account.base.to;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yum.base.to.BaseBean;
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountControlBean extends BaseBean{
	private String accountCode;
	private String accountControlCode;
	private String controlName;
	private String inputType;
	private String accountControlCheck;
	
	public String getAccountCode() {
		return accountCode;
	}
	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getAccountControlCode() {
		return accountControlCode;
	}
	public void setAccountControlCode(String accountControlCode) {
		this.accountControlCode = accountControlCode;
	}
	public String getControlName() {
		return controlName;
	}
	public void setControlName(String controlName) {
		this.controlName = controlName;
	}
	public String getAccountControlCheck() {
		return accountControlCheck;
	}
	public void setAccountControlCheck(String accountControlCheck) {
		this.accountControlCheck = accountControlCheck;
	}
	public String getInputType() {
		return inputType;
	}
	public void setInputType(String inputType) {
		this.inputType = inputType;
	}
}
