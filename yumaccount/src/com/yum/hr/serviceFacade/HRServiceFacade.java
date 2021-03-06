package com.yum.hr.serviceFacade;

import java.util.ArrayList;

import com.yum.hr.to.EmployeeBean;

public interface HRServiceFacade {

	public ArrayList<EmployeeBean> findEmployeeList(String deptCode);

	public void batchEmployeeInfo(EmployeeBean employeeBean);

	public void batchEmployee(ArrayList<EmployeeBean> empList);

	public void registerEmployee(EmployeeBean employeeBean);

	public ArrayList<EmployeeBean> findSYSList(String authority);
}
