package com.yum.account.base.serviceFacade;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yum.account.base.serviceFacade.AccountServiceFacade;
import com.yum.account.base.serviceFacade.AccountServiceFacadeImpl;
import com.yum.account.base.applicationService.AccountBaseApplicationService;
import com.yum.account.base.applicationService.AccountBaseApplicationServiceImpl;
import com.yum.account.base.applicationService.AccountDetailBaseApplicationService;
import com.yum.account.base.applicationService.AccountDetailBaseApplicationServiceImpl;
import com.yum.account.base.to.AccountBean;
import com.yum.account.base.to.AccountControlBean;
import com.yum.account.base.to.AccountDetailBean;
import com.yum.common.db.DataSourceTransactionManager;
import com.yum.common.exception.DataAccessException;

public class AccountServiceFacadeImpl implements AccountServiceFacade{
	protected final Log logger = LogFactory.getLog(this.getClass());
	private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();
	AccountBaseApplicationService accountBaseApplicationService = AccountBaseApplicationServiceImpl.getInstance();
	AccountDetailBaseApplicationService accountDetailBaseApplicationService = AccountDetailBaseApplicationServiceImpl.getInstance();
	
	private static AccountServiceFacade instance ;
	private AccountServiceFacadeImpl(){}
	public static AccountServiceFacade getInstance() {
		// TODO Auto-generated method stub
		if(instance ==null){
			instance = new AccountServiceFacadeImpl();
		}
		return instance;
	}
	
	@Override
	public ArrayList<AccountBean> findAccountList() {
		// TODO Auto-generated method stub
		 dataSourceTransactionManager.beginTransaction();
		if(logger.isDebugEnabled()){ logger.debug(" AccountServiceFacadeImpl : findAccountList 시작 "); }
		ArrayList<AccountBean> accountList=null;
		
		 try{
			
			accountList=accountBaseApplicationService.findAccountList();
			dataSourceTransactionManager.commitTransaction();
		 }catch(DataAccessException e){
			 logger.fatal(e.getMessage());
	           dataSourceTransactionManager.rollbackTransaction();
	           throw e;
	     }
		 if(logger.isDebugEnabled()){ logger.debug(" AccountServiceFacadeImpl : findAccountList 종료 "); }
		 return accountList;
	}
	
	
	
	
	@Override
	public ArrayList<AccountControlBean> findAccountControlList(String accountCode) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" AccountDetailServiceFacadeImpl : findAccountControlList 시작 "); }
		 dataSourceTransactionManager.beginTransaction();
	        ArrayList<AccountControlBean> accountControlList=null; //?
	        try{
	        	accountControlList=accountDetailBaseApplicationService.findAccountControlList(accountCode);
	        	dataSourceTransactionManager.commitTransaction();
	        }catch(DataAccessException e){
	        	logger.fatal(e.getMessage());
	        	dataSourceTransactionManager.rollbackTransaction();
	        	throw e;
	        }
	        if(logger.isDebugEnabled()){ logger.debug(" AccountDetailServiceFacadeImpl : findAccountControlList 종료 "); }
			return accountControlList;
		}
	@Override
	public ArrayList<AccountDetailBean> findAccountDetailList() {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" AccountDetailServiceFacadeImpl : findAccountDetailList 시작 "); }
		 dataSourceTransactionManager.beginTransaction();
			ArrayList<AccountDetailBean> controlItemList=null;
	        try{
	        	controlItemList=accountDetailBaseApplicationService.findAccountDetailList();
	        	dataSourceTransactionManager.commitTransaction();
	        }catch(DataAccessException e){
	        	logger.fatal(e.getMessage());
	    		dataSourceTransactionManager.rollbackTransaction();
	    	throw e;
	        }
	        if(logger.isDebugEnabled()){ logger.debug(" AccountDetailServiceFacadeImpl : findAccountDetailList 종료 "); }
			return controlItemList;
	}
	@Override
	public void batchAccountControlListProcess(ArrayList<AccountControlBean> accountControlList) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" AccountDetailServiceFacadeImpl : batchAccountControlListProcess 시작 "); }
		 dataSourceTransactionManager.beginTransaction();
	        try{
	        	accountDetailBaseApplicationService.batchAccountControlListProcess(accountControlList);
	        	dataSourceTransactionManager.commitTransaction();
	        }catch(DataAccessException e){
	        	logger.fatal(e.getMessage());
	        	dataSourceTransactionManager.rollbackTransaction();
	        	
	        	throw e;
	        }
	        if(logger.isDebugEnabled()){ logger.debug(" AccountDetailServiceFacadeImpl : batchAccountControlListProcess 종료 "); }
	}
	

}
