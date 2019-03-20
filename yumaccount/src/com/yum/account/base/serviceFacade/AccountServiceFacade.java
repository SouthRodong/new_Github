package com.yum.account.base.serviceFacade;

import java.util.ArrayList;

import com.yum.account.base.to.AccountBean;
import com.yum.account.base.to.AccountControlBean;
import com.yum.account.base.to.AccountDetailBean;

public interface AccountServiceFacade {

	public ArrayList<AccountBean> findAccountList();
	
	public ArrayList<AccountControlBean> findAccountControlList(String accountCode);

	public ArrayList<AccountDetailBean> findAccountDetailList();

	public void batchAccountControlListProcess(ArrayList<AccountControlBean> accountControlList);

}
