package com.yum.account.base.to;

import java.util.ArrayList;

import com.yum.account.base.to.AccountControlBean;
import com.yum.base.to.BaseBean;

public class AccountBean extends BaseBean{
	private String accountCode;
	private String accountName;
	private String superAccountCode;
	private String accountUseCheck;
	private String groupCodeName;
	private ArrayList<AccountControlBean> accountControlList;

	private int level;
	private String leaf;

	private String loaded;
	private String expanded;

	public String getAccountCode() {
		return accountCode;
	}
	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getSuperAccountCode() {
		return superAccountCode;
	}
	public void setSuperAccountCode(String superAccountCode) {
		this.superAccountCode = superAccountCode;
	}
	public String getGroupCodeName() {
		return groupCodeName;
	}
	public void setGroupCodeName(String groupCodeName) {
		this.groupCodeName = groupCodeName;
	}
	public ArrayList<AccountControlBean> getAccountControlList() {
		return accountControlList;
	}
	public void setAccountControlList(ArrayList<AccountControlBean> accountControlList) {
		this.accountControlList = accountControlList;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getLeaf() {
		return leaf;
	}
	public void setLeaf(String leaf) {
		this.leaf = leaf;
	}
	public String getLoaded() {
		return loaded;
	}
	public void setLoaded(String loaded) {
		this.loaded = loaded;
	}
	public String getExpanded() {
		return expanded;
	}
	public void setExpanded(String expanded) {
		this.expanded = expanded;
	}
	public String getAccountUseCheck() {
		return accountUseCheck;
	}
	public void setAccountUseCheck(String accountUseCheck) {
		this.accountUseCheck = accountUseCheck;
	}
}
