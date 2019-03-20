package com.yum.account.base.dao;

import java.util.ArrayList;

import com.yum.account.base.to.AccountControlBean;

public interface AccountControlDAO {

	public ArrayList<AccountControlBean> selectAccountControlList(String accountCode);

	public void insertAccountControl(AccountControlBean accountControl);

	public void deleteAccountControl(AccountControlBean accountControl);

	

}
