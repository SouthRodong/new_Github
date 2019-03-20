package com.yum.hr.dao;

import java.util.ArrayList;

import com.yum.hr.to.EmployeeBean;

public interface EmployeeDAO {

	public EmployeeBean selectCompanyStaff(String empCode); 

	public ArrayList<EmployeeBean> selectEmployeeList(String deptCode);
		

	public void updateEmployeeInfo(EmployeeBean employeeBean);

	public void updateEmployee(EmployeeBean employBean);

	public void deleteEmployee(String empCode);

	public void insertEmployee(EmployeeBean employeeBean);

	public EmployeeBean selectEmployee(String EmpCode);

	public ArrayList<EmployeeBean> findSYSList(String authority); 

}
