package com.yum.account.base.applicationService;

import java.util.ArrayList;

import com.yum.account.base.to.AccountControlBean;
import com.yum.account.base.to.AccountDetailBean;

public interface AccountDetailBaseApplicationService {

	public ArrayList<AccountDetailBean> findAccountDetailList();

	public ArrayList<AccountControlBean> findAccountControlList(String accountCode);

	public void batchAccountControlListProcess(ArrayList<AccountControlBean> accountControlList);

}
