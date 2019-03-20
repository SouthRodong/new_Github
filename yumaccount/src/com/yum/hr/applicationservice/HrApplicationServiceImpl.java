package com.yum.hr.applicationservice;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yum.hr.applicationservice.HrApplicationService;
import com.yum.hr.applicationservice.HrApplicationServiceImpl;
import com.yum.common.exception.DataAccessException;
import com.yum.hr.dao.EmployeeDAO;
import com.yum.hr.dao.EmployeeDAOImpl;
import com.yum.hr.to.EmployeeBean;

public class HrApplicationServiceImpl implements HrApplicationService{

	protected final Log logger = LogFactory.getLog(this.getClass());
	private EmployeeDAO employeeDAO = EmployeeDAOImpl.getInstance();
	
	private static HrApplicationService instance;
	private HrApplicationServiceImpl(){}
	
	public static HrApplicationService getInstance() {
		// TODO Auto-generated method stub
		System.out.println("		@ HrApplicationService 객체접근");
		if(instance == null){
			instance = new HrApplicationServiceImpl();
		}
		return instance; 
	}
	
	
	@Override
	public ArrayList<EmployeeBean> findEmployeeList(String deptCode) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" HrApplicationServiceImpl : findEmployeeList 시작 "); }
		ArrayList<EmployeeBean> empList = null;
		try{
		empList = employeeDAO.selectEmployeeList(deptCode);
		}catch(DataAccessException e){
			logger.fatal(e.getMessage());
			throw e; 
		}

if(logger.isDebugEnabled()){ logger.debug(" HrApplicationServiceImpl : findEmployeeList 종료 "); }
		return empList;
	}

	@Override
	public void batchEmployeeInfo(EmployeeBean employeeBean) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" HrApplicationServiceImpl : batchEmployeeInfo 시작 "); }
		try {
			employeeDAO.updateEmployeeInfo(employeeBean);
		} catch (DataAccessException e) {
			logger.fatal(e.getMessage());
			throw e;
		}
		if(logger.isDebugEnabled()){ logger.debug(" HrApplicationServiceImpl : batchEmployeeInfo 종료 "); }
	}

	@Override
	public void batchEmployee(ArrayList<EmployeeBean> empList) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" HrApplicationServiceImpl : batchEmployee 시작 "); }
		try{
			for(EmployeeBean employBean : empList){
				String empStatus = employBean.getStatus();
				switch (empStatus) {
				case "update":
					modifyEmployee(employBean);
					break;
				case "delete":
					removeEmployee(employBean);
					break;
				}
			}
		}catch(DataAccessException e){
			logger.fatal(e.getMessage());
			throw e;
		}
		if(logger.isDebugEnabled()){ logger.debug(" HrApplicationServiceImpl : batchEmployee 종료 "); }
	}

	private void modifyEmployee(EmployeeBean employBean) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" HrApplicationServiceImpl : modifyEmployee 시작 "); }
		try{
		employeeDAO.updateEmployee(employBean);
		}catch(DataAccessException e){
			logger.fatal(e.getMessage());
			throw e;		
			
		}
		if(logger.isDebugEnabled()){ logger.debug(" HrApplicationServiceImpl : modifyEmployee 종료 "); }
	}

	private void removeEmployee(EmployeeBean employBean) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" HrApplicationServiceImpl : removeEmployee 시작 "); }
		try{
		employeeDAO.deleteEmployee(employBean.getEmpCode());
		}catch(DataAccessException e){
			logger.fatal(e.getMessage());
			throw e;
			
		}
		if(logger.isDebugEnabled()){ logger.debug(" HrApplicationServiceImpl : removeEmployee 종료 "); }
	}

	@Override
	public void registerEmployee(EmployeeBean employeeBean) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" HrApplicationServiceImpl : registerEmployee 시작 "); }
		try{
			
			employeeDAO.insertEmployee(employeeBean);
		}catch(Exception e){
			logger.fatal(e.getMessage());
			throw e;
			
		}
		if(logger.isDebugEnabled()){ logger.debug(" HrApplicationServiceImpl : registerEmployee 종료 "); }
	}

	@Override
	public ArrayList<EmployeeBean> findSYSList(String authority) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" HrApplicationServiceImpl : findSYSList 시작 "); }
		ArrayList<EmployeeBean> empList = null;
		try{
			empList = employeeDAO.findSYSList(authority);
		}catch(DataAccessException e){
			logger.fatal(e.getMessage());
			throw e; 
		}

		if(logger.isDebugEnabled()){ logger.debug(" HrApplicationServiceImpl : findSYSList 종료 "); }
		return empList;
	}

	

}
