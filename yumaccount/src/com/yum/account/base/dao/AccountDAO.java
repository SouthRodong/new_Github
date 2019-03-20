package com.yum.account.base.dao;

import java.util.ArrayList;

import com.yum.account.base.to.AccountBean;

public interface AccountDAO {

	public ArrayList<AccountBean> selectAccountList();

}
