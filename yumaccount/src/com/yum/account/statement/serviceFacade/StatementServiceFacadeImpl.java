package com.yum.account.statement.serviceFacade;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yum.account.statement.serviceFacade.StatementServiceFacade;
import com.yum.account.statement.serviceFacade.StatementServiceFacadeImpl;
import com.yum.account.statement.applicationService.StatementApplicationService;
import com.yum.account.statement.applicationService.StatementApplicationServiceImpl;
import com.yum.common.db.DataSourceTransactionManager;
import com.yum.common.exception.DataAccessException;


public class StatementServiceFacadeImpl implements StatementServiceFacade{
	protected final Log logger = LogFactory.getLog(this.getClass());

	private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();
	StatementApplicationService statementApplicationService = StatementApplicationServiceImpl.getInstance();
	private static StatementServiceFacade instance;
	private StatementServiceFacadeImpl(){}
	public static StatementServiceFacade getInstance() {
		// TODO Auto-generated method stub
		if(instance == null){
			instance = new StatementServiceFacadeImpl();
			System.out.println("		@ StatementServiceFacadeImpl에 접근");
		}
		return instance;
	}
	

	@Override
	public HashMap<String, Object> findTrialBalanceList(String fromDate, String toDate) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" StatementServiceFacadeImpl : findTrialBalanceList 시작 "); }
		dataSourceTransactionManager.beginTransaction();
	    HashMap<String, Object> trialBalanceList=null;
		try{
			trialBalanceList=statementApplicationService.findTrialBalanceList(fromDate, toDate);
			System.out.println("		@ 조회일자 : "+fromDate+" 부터 "+toDate+" 까지");
		}catch(DataAccessException e){
			logger.fatal(e.getMessage());
		   dataSourceTransactionManager.rollbackTransaction();
		   throw e;
	   }	
		if(logger.isDebugEnabled()){ logger.debug(" StatementServiceFacadeImpl : findTrialBalanceList 종료 "); }
		return trialBalanceList;
	}


	@Override
	public HashMap<String,Object> findincomeStatementList(String fromDate, String toDate){
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" StatementServiceFacadeImpl : findincomeStatementList   시작 "); }
		dataSourceTransactionManager.beginTransaction();
	    HashMap<String, Object> incomeStatementList=null;
		try{
			incomeStatementList=statementApplicationService.findincomeStatementList(fromDate, toDate);
			System.out.println("		@ 조회일자 : "+fromDate+" 부터 "+toDate+" 까지");
		}catch(DataAccessException e){
			logger.fatal(e.getMessage());
		   dataSourceTransactionManager.rollbackTransaction();
		   throw e;
	   }	
		if(logger.isDebugEnabled()){ logger.debug(" StatementServiceFacadeImpl : findincomeStatementList   종료 "); }
		return incomeStatementList;
		
	}	
	
}
