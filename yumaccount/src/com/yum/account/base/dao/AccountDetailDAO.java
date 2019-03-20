package com.yum.account.base.dao;

import java.util.ArrayList;

import com.yum.account.base.to.AccountDetailBean;

public interface AccountDetailDAO {

	ArrayList<AccountDetailBean> findControlItemList();

}
