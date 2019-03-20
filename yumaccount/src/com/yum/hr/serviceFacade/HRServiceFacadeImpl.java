package com.yum.hr.serviceFacade;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yum.hr.serviceFacade.HRServiceFacade;
import com.yum.hr.serviceFacade.HRServiceFacadeImpl;
import com.yum.common.db.DataSourceTransactionManager;
import com.yum.common.exception.DataAccessException;
import com.yum.hr.applicationservice.HrApplicationService;
import com.yum.hr.applicationservice.HrApplicationServiceImpl;
import com.yum.hr.to.EmployeeBean;

public class HRServiceFacadeImpl implements HRServiceFacade{
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();
	
	HrApplicationService hrApplicationService = HrApplicationServiceImpl.getInstance();

	private static HRServiceFacade instance;
	private HRServiceFacadeImpl(){}
	public static HRServiceFacade getInstance() {
		// TODO Auto-generated method stub
		if(instance == null){
			instance = new HRServiceFacadeImpl();
		}
		return instance;
	}
	

	@Override
	public ArrayList<EmployeeBean> findEmployeeList(String deptCode) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" HRServiceFacadeImpl : findEmployeeList 시작 "); }
		dataSourceTransactionManager.beginTransaction();
		ArrayList<EmployeeBean> empList = null;
		try{
		empList = hrApplicationService.findEmployeeList(deptCode);
		System.out.println("		@ 조회된 부서코드 : "+deptCode);
		dataSourceTransactionManager.commitTransaction(); 
		}catch(DataAccessException e){
			logger.fatal(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction(); 
			throw e; 
		}		
		if(logger.isDebugEnabled()){ logger.debug(" HRServiceFacadeImpl : findEmployeeList 종료 "); }
		return empList;
	}
	@Override
	public void batchEmployeeInfo(EmployeeBean employeeBean) {
		// TODO Auto-generated method stub

if(logger.isDebugEnabled()){ logger.debug(" HRServiceFacadeImpl : batchEmployeeInfo 시작 "); }
		dataSourceTransactionManager.beginTransaction();
		try {
			hrApplicationService.batchEmployeeInfo(employeeBean);
			System.out.println("		@ 조회된 사원명 : "+employeeBean.getEmpName());
			dataSourceTransactionManager.commitTransaction();
		} catch (DataAccessException e) {
			logger.fatal(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}
		if(logger.isDebugEnabled()){ logger.debug(" HRServiceFacadeImpl : batchEmployeeInfo 종료 "); }
	}
	@Override
	public void batchEmployee(ArrayList<EmployeeBean> empList) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" HRServiceFacadeImpl : batchEmployee 시작 "); }
		dataSourceTransactionManager.beginTransaction();
		try {
			hrApplicationService.batchEmployee(empList);
			for(int a=0; a<empList.size(); a++) {
			System.out.println("		@ 사원리스트 : "+empList.get(a).getEmpName());
			}
			dataSourceTransactionManager.commitTransaction();
		} catch (DataAccessException e) {
			logger.fatal(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;  
		}
		if(logger.isDebugEnabled()){ logger.debug(" HRServiceFacadeImpl : batchEmployee 종료 "); }
	}
	@Override
	public void registerEmployee(EmployeeBean employeeBean) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" HRServiceFacadeImpl : registerEmployee 시작 "); }
		dataSourceTransactionManager.beginTransaction();
		try{
			hrApplicationService.registerEmployee(employeeBean);
			System.out.println("		@ 등록된 사원명 : "+employeeBean.getEmpName());
			dataSourceTransactionManager.commitTransaction();
			
		}catch (DataAccessException e){
			logger.fatal(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			throw e;
		}
		if(logger.isDebugEnabled()){ logger.debug(" HRServiceFacadeImpl : registerEmployee 종료 "); }
	}
	@Override
	public ArrayList<EmployeeBean> findSYSList(String authority) {
		if(logger.isDebugEnabled()){ logger.debug(" HRServiceFacadeImpl : findSYSList 시작 "); }
		dataSourceTransactionManager.beginTransaction();
		ArrayList<EmployeeBean> empList = null;
		try{
		empList = hrApplicationService.findSYSList(authority);
		dataSourceTransactionManager.commitTransaction(); 
		}catch(DataAccessException e){
			logger.fatal(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction(); 
			throw e; 
		}		
		if(logger.isDebugEnabled()){ logger.debug(" HRServiceFacadeImpl : findSYSList 종료 "); }
		return empList;
	}

	
}
