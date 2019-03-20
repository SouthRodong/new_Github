package com.yum.account.base.applicationService;


import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yum.account.base.applicationService.AccountBaseApplicationServiceImpl;

import com.yum.account.base.to.AccountControlBean;
import com.yum.account.base.dao.AccountControlDAO;
import com.yum.account.base.dao.AccountControlDAOImpl;

import com.yum.account.base.applicationService.AccountBaseApplicationService;
import com.yum.account.base.dao.AccountDAO;
import com.yum.account.base.dao.AccountDAOImpl;
import com.yum.account.base.to.AccountBean;
import com.yum.common.exception.DataAccessException;

@SuppressWarnings("unused")
public class AccountBaseApplicationServiceImpl implements AccountBaseApplicationService{

	protected final Log logger = LogFactory.getLog(this.getClass());
	
	private AccountDAO accountDAO = AccountDAOImpl.getInstance();
	
	private static AccountBaseApplicationService instance;
	private AccountBaseApplicationServiceImpl(){}
	public static AccountBaseApplicationService getInstance() {
		// TODO Auto-generated method stub
		if(instance ==null){
			instance = new AccountBaseApplicationServiceImpl();
		}
		return instance;
	}
	
	public ArrayList<AccountBean> findAccountList() {
			// TODO Auto-generated method stub
			if(logger.isDebugEnabled()){ logger.debug(" AccountBaseApplicationServiceImpl : findAccountList 시작 "); }
			ArrayList<AccountBean> accountList = null;
			
			try {
				accountList = accountDAO.selectAccountList();
				
			} catch (DataAccessException e) {
				logger.fatal(e.getMessage());
				throw e;
			}
			if(logger.isDebugEnabled()){ logger.debug(" AccountBaseApplicationServiceImpl : findAccountList 종료 "); }
			return accountList;
		}
	
	

	

}
