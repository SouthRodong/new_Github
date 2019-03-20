package com.yum.account.statement.applicationService;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yum.account.statement.applicationService.StatementApplicationService;
import com.yum.account.statement.applicationService.StatementApplicationServiceImpl;
import com.yum.account.statement.dao.TrialBalanceDAO;
import com.yum.account.statement.dao.TrialBalanceDAOImpl;
import com.yum.account.statement.dao.IncomeStatementDAO;
import com.yum.account.statement.dao.IncomeStatementDAOImpl;


public class StatementApplicationServiceImpl implements StatementApplicationService{
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	private static TrialBalanceDAO trialBalanceDAO =TrialBalanceDAOImpl.getInstance();
	private static IncomeStatementDAO IncomeStatementDAO = IncomeStatementDAOImpl.getInstance();
		
	private static StatementApplicationService instance;
	private StatementApplicationServiceImpl (){}
	public static StatementApplicationService getInstance() {
		// TODO Auto-generated method stub
		if(instance == null){
			instance = new StatementApplicationServiceImpl();
			System.out.println("		@ StatementApplicationServiceImpl에 접근");
		}
		return instance;
	}
	

	@Override
	public HashMap<String, Object> findTrialBalanceList(String fromDate, String toDate) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" StatementApplicationServiceImpl : findTrialBalanceList 시작 "); }
		HashMap<String, Object> trialBalanceList = null;
		try {
			trialBalanceList = trialBalanceDAO.findTrialBalanceList(fromDate, toDate);
			System.out.println("		@ 합계잔액시산표 조회 : "+fromDate+" 부터 "+toDate+" 까지");
		} catch (Exception error) {	
			logger.fatal(error.getMessage());
			throw error;
		}
		if(logger.isDebugEnabled()){ logger.debug(" StatementApplicationServiceImpl : findTrialBalanceList 종료 "); }
		return trialBalanceList;
	}
	
	@Override
	 public HashMap<String,Object> findincomeStatementList(String fromDate, String toDate){
		// TODO Auto-generated method stub
				if(logger.isDebugEnabled()){ logger.debug(" StatementApplicationServiceImpl : findincomeStatementList 시작 "); }
				HashMap<String, Object> incomeStatementList = null;
				try {
					incomeStatementList = IncomeStatementDAO.findincomeStatementList(fromDate, toDate);
					System.out.println("		@ 손익계산서 조회 : "+fromDate+" 부터 "+toDate+" 까지");
				} catch (Exception error) {	
					logger.fatal(error.getMessage());
					throw error;
				}
				if(logger.isDebugEnabled()){ logger.debug(" StatementApplicationServiceImpl : findincomeStatementList 종료 "); }
				return incomeStatementList;
	 }

}
