package com.yum.hr.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yum.hr.dao.EmployeeDAO;
import com.yum.hr.dao.EmployeeDAOImpl;
import com.yum.common.db.DataSourceTransactionManager;
import com.yum.common.exception.DataAccessException;
import com.yum.hr.to.EmployeeBean;

public class EmployeeDAOImpl implements EmployeeDAO{
	private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	private static EmployeeDAO instance;
	
	private  EmployeeDAOImpl(){}
	public static EmployeeDAO getInstance() {
	      if (instance == null)
	         instance = new EmployeeDAOImpl();
	      return instance;
	   }
	
	 
	@Override
	public EmployeeBean selectCompanyStaff(String empCode) {
		// TODO Auto-generated method stub
		
		if(logger.isDebugEnabled()){ logger.debug(" EmployeeDAOImpl : selectCompanyStaff 시작 "); }
		
		Connection con = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    try {
	         StringBuffer query = new StringBuffer();
	         query.append("SELECT * FROM EMPLOYEE WHERE EMP_CODE=?");
	         con = dataSourceTransactionManager.getConnection();
	         pstmt = con.prepareStatement(query.toString());
	         pstmt.setString(1, empCode);
	         rs = pstmt.executeQuery();
	         EmployeeBean employeeBean = null;
	         if (rs.next()) {
	        	 
	            employeeBean = new EmployeeBean();
	            employeeBean.setEmpCode(rs.getString("EMP_CODE"));
	            employeeBean.setEmpName(rs.getString("EMP_NAME"));
	            employeeBean.setEmpPassword(rs.getString("EMP_PASSWORD"));
	            employeeBean.setPositionCode(rs.getString("POSITION_CODE"));
	            employeeBean.setAuthority(rs.getString("AUTHORITY"));
	            employeeBean.setDeptCode(rs.getString("DEPT_CODE"));
	            employeeBean.setEmail(rs.getString("EMAIL"));
	            employeeBean.setGender(rs.getString("GENDER"));
	            employeeBean.setResidentNumber(rs.getString("RESIDENT_NUMBER"));
	            employeeBean.setTelephone(rs.getString("TELEPHONE"));
	            
	            employeeBean.setEnteringDate(rs.getString("ENTERING_DATE"));
	            employeeBean.setBirthday(rs.getString("BIRTHDAY"));
	            employeeBean.setEducation(rs.getString("EDUCATION"));
	            employeeBean.setZipCode(rs.getString("ZIP_CODE"));
	            employeeBean.setBasicAddress(rs.getString("BASIC_ADDRESS"));
	            employeeBean.setDetailAddress(rs.getString("DETAIL_ADDRESS"));
	            employeeBean.setImgSrc(rs.getString("IMG_SRC"));
	            System.out.println("		@ 선택된 사원: "+empCode);
	         }
	         if(logger.isDebugEnabled()){ logger.debug(" EmployeeDAOImpl : selectCompanyStaff 종료 "); }
	          return employeeBean;
	        
	      } catch (Exception sqle) {
	    	  	logger.fatal(sqle.getMessage());
	             throw new DataAccessException(sqle.getMessage());
	      } finally {
	         dataSourceTransactionManager.close(pstmt, rs);
	      }
	   
	}
	
	@Override  
	public ArrayList<EmployeeBean> selectEmployeeList(String deptCode) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" EmployeeDAOImpl : selectEmployeeList 시작 "); }
		Connection con=null;
	    PreparedStatement pstmt=null;
	    ResultSet rs=null;
	    ArrayList<EmployeeBean> empList=new ArrayList<EmployeeBean>();
	    try {
		StringBuffer query=new StringBuffer();
        query.append(" SELECT * FROM EMPLOYEE where DEPT_CODE = ? ");
        con = dataSourceTransactionManager.getConnection();
		pstmt = con.prepareStatement(query.toString());
		pstmt.setString(1, deptCode);
        rs = pstmt.executeQuery();
		System.out.println("		@ 선택된 부서: "+deptCode);
        while (rs.next()){
           	EmployeeBean employeeBean=new EmployeeBean();
            employeeBean.setEmpCode(rs.getString("EMP_CODE"));
            employeeBean.setEmpName(rs.getString("EMP_NAME"));
            employeeBean.setEmpPassword(rs.getString("EMP_PASSWORD"));
            employeeBean.setPositionCode(rs.getString("POSITION_CODE"));
            employeeBean.setAuthority(rs.getString("AUTHORITY"));
            employeeBean.setDeptCode(rs.getString("DEPT_CODE"));
            employeeBean.setEmail(rs.getString("EMAIL"));            
            employeeBean.setGender(rs.getString("GENDER"));
            employeeBean.setResidentNumber(rs.getString("RESIDENT_NUMBER"));
            employeeBean.setTelephone(rs.getString("TELEPHONE"));
            employeeBean.setEnteringDate(rs.getString("ENTERING_DATE"));
            employeeBean.setBirthday(rs.getString("BIRTHDAY"));
            
            employeeBean.setEducation(rs.getString("EDUCATION"));
            employeeBean.setZipCode(rs.getString("ZIP_CODE"));
            employeeBean.setBasicAddress(rs.getString("BASIC_ADDRESS"));
            employeeBean.setDetailAddress(rs.getString("DETAIL_ADDRESS"));
            employeeBean.setImgSrc(rs.getString("IMG_SRC"));
           	empList.add(employeeBean);
           }
        if(logger.isDebugEnabled()){ logger.debug(" EmployeeDAOImpl : selectEmployeeList 종료 "); }
        	return empList;
	    } catch (Exception sqle) { 
				// TODO Auto-generated catch block
	    	logger.fatal(sqle.getMessage());
	    	throw new DataAccessException(sqle.getMessage());
	    		
		}finally{
			dataSourceTransactionManager.close(pstmt, rs);
		}
			  
	} 
	
	@Override
	public void updateEmployeeInfo(EmployeeBean employeeBean) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" EmployeeDAOImpl : updateEmployeeInfo 시작 "); }
		 Connection conn = null;
	        PreparedStatement pstmt = null;
	        System.out.println("		@ 수정할 사원코드: "+employeeBean.getEmpCode());
	        System.out.println("		@ 수정할 사원이름: "+employeeBean.getEmpName());
	        try {
	            StringBuffer query = new StringBuffer();
	            query.append("UPDATE EMPLOYEE SET ");
	            query.append("GENDER =?, ");
	            query.append("TELEPHONE= ?, ZIP_CODE = ?, BASIC_ADDRESS = ?, ");
	            query.append("DETAIL_ADDRESS = ?, EMAIL = ?, IMG_SRC = ?, ");
	            query.append("RESIDENT_NUMBER = ? ");
	            query.append("WHERE EMP_CODE = ?");
	            conn = dataSourceTransactionManager.getConnection();
	            pstmt = conn.prepareStatement(query.toString());

	            pstmt.setString(1, employeeBean.getGender());
	            pstmt.setString(2, employeeBean.getTelephone());
	            pstmt.setString(3, employeeBean.getZipCode());
	            pstmt.setString(4, employeeBean.getBasicAddress());
	            pstmt.setString(5, employeeBean.getDetailAddress());
	            pstmt.setString(6, employeeBean.getEmail());
	            pstmt.setString(7, employeeBean.getImgSrc());
	            pstmt.setString(8, employeeBean.getResidentNumber());
	            pstmt.setString(9, employeeBean.getEmpCode());

	            pstmt.executeUpdate();
	            if(logger.isDebugEnabled()){ logger.debug(" EmployeeDAOImpl : updateEmployeeInfo 종료 "); }
	        }catch(Exception sqle){
	        	logger.fatal(sqle.getMessage());
	            throw new DataAccessException(sqle.getMessage());
	        }finally{
	           dataSourceTransactionManager.close(pstmt);
	        }
	}
	
	
	@Override
	public void updateEmployee(EmployeeBean bean) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" EmployeeDAOImpl : updateEmployee 시작 "); }
		Connection con = null;
		PreparedStatement pstmt = null;
		System.out.println("		@ 수정할 사원코드: "+bean.getEmpCode());
        System.out.println("		@ 수정할 사원이름: "+bean.getEmpName());
		try{
			StringBuffer query = new StringBuffer();
			query.append("UPDATE EMPLOYEE ");
			query.append("SET DEPT_CODE=?, "); 
			query.append("AUTHORITY=? ");		
			query.append("WHERE EMP_CODE=? ");
			con = dataSourceTransactionManager.getConnection();
			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, bean.getDeptCode());
			pstmt.setString(2, bean.getAuthority());
			pstmt.setString(3, bean.getEmpCode());
			pstmt.executeUpdate();
			if(logger.isDebugEnabled()){ logger.debug(" EmployeeDAOImpl : updateEmployee 종료 "); }
		}catch(Exception sqle){
			logger.fatal(sqle.getMessage());
			throw new DataAccessException(sqle.getMessage()); 
				
		}finally{
			dataSourceTransactionManager.close(pstmt);
		}
	}
	
	@Override
	public void deleteEmployee(String empCode) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" EmployeeDAOImpl : deleteEmployee 시작 "); }
		Connection con = null;
		PreparedStatement pstmt = null;
		System.out.println("		@ 삭제할 사원코드: "+empCode);
        
		try{
			StringBuffer query = new StringBuffer();
			query.append(" DELETE FROM EMPLOYEE ");
			query.append(" WHERE EMP_CODE=? ");
			con = dataSourceTransactionManager.getConnection();
			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, empCode);
			pstmt.executeUpdate();
			if(logger.isDebugEnabled()){ logger.debug(" EmployeeDAOImpl : deleteEmployee 종료 "); }
		}catch(Exception sqle){
			logger.fatal(sqle.getMessage());
			throw new DataAccessException(sqle.getMessage());
		}finally{
			dataSourceTransactionManager.close(pstmt);
		}
	}
	@Override
	public void insertEmployee(EmployeeBean employeeBean) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" EmployeeDAOImpl : insertEmployee 시작 "); }
		Connection con = null;
		PreparedStatement pstmt = null;
		try{
			StringBuffer query = new StringBuffer();
			query.append(" INSERT INTO EMPLOYEE (EMP_CODE, EMP_NAME, EMP_PASSWORD, ");
			query.append("POSITION_CODE, AUTHORITY, DEPT_CODE, EMAIL, ");
			query.append("GENDER, RESIDENT_NUMBER, TELEPHONE, ENTERING_DATE, ");
			query.append("BIRTHDAY, EDUCATION, ZIP_CODE, BASIC_ADDRESS, ");
			query.append("DETAIL_ADDRESS, IMG_SRC) ");
			query.append("VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");			 			
			con = dataSourceTransactionManager.getConnection();
			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, employeeBean.getEmpCode());
			pstmt.setString(2, employeeBean.getEmpName());
			pstmt.setString(3, employeeBean.getEmpPassword());
			pstmt.setString(4, employeeBean.getPositionCode());
			pstmt.setString(5, employeeBean.getAuthority());
			pstmt.setString(6, employeeBean.getDeptCode());
			pstmt.setString(7, employeeBean.getEmail());
			pstmt.setString(8, employeeBean.getGender());

			pstmt.setString(9, employeeBean.getResidentNumber());
			pstmt.setString(10, employeeBean.getTelephone());
			
			pstmt.setString(11, employeeBean.getEnteringDate());
			pstmt.setString(12, employeeBean.getBirthday());
			pstmt.setString(13, employeeBean.getEducation());
			pstmt.setString(14, employeeBean.getZipCode());
			pstmt.setString(15, employeeBean.getBasicAddress());
			pstmt.setString(16, employeeBean.getDetailAddress());
			pstmt.setString(17, employeeBean.getImgSrc());
			System.out.println("		@ 가입할 사원코드"+employeeBean.getEmpCode());
			System.out.println("		@ 가입할 사원이름"+employeeBean.getEmpName());
			pstmt.executeUpdate();
			if(logger.isDebugEnabled()){ logger.debug(" EmployeeDAOImpl : insertEmployee 종료 "); }
		}catch(Exception sqle){
			logger.fatal(sqle.getMessage());
			throw new DataAccessException(sqle.getMessage());
		}finally{
			dataSourceTransactionManager.close(pstmt);
		}
	}
	
	@Override
	public EmployeeBean selectEmployee(String empCode) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" EmployeeDAOImpl : selectEmployee 시작 "); }
		 Connection conn = null;
	      PreparedStatement pstmt = null;
	      ResultSet rs = null;
	      EmployeeBean employeeBean = null;
	      try{
	         StringBuffer query = new StringBuffer();
	         query.append("SELECT * FROM EMPLOYEE WHERE EMP_CODE=?");
	         conn = dataSourceTransactionManager.getConnection();
	         pstmt = conn.prepareStatement(query.toString());
	         pstmt.setString(1, empCode);
	         rs = pstmt.executeQuery();
	         if(rs.next()){
	       	  employeeBean = new EmployeeBean();
	       	  employeeBean.setEmpCode(rs.getString("EMP_CODE"));
	       	  employeeBean.setEmpName(rs.getString("EMP_NAME"));
	       	  employeeBean.setEmpPassword(rs.getString("EMP_PASSWORD"));
	       	  employeeBean.setDeptCode(rs.getString("DEPT_CODE"));
	       	  employeeBean.setEnteringDate(rs.getString("ENTERING_DATE"));
	       	  employeeBean.setAuthority(rs.getString("AUTHORITY"));
	       	System.out.println("		@ 조회한 사원코드 : "+empCode);
	         }
	         if(logger.isDebugEnabled()){ logger.debug(" EmployeeDAOImpl : selectEmployee 종료 "); }
	      }catch(Exception sqle){
	    	  logger.fatal(sqle.getMessage());
	         throw new DataAccessException(sqle.getMessage());
	      }
	      return employeeBean;
	}
	@Override
	public ArrayList<EmployeeBean> findSYSList(String authority) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" EmployeeDAOImpl : findSYSList 시작 "); }
		Connection con=null;
	    PreparedStatement pstmt=null;
	    ResultSet rs=null;
	    ArrayList<EmployeeBean> empList=new ArrayList<EmployeeBean>();
	    try {
		StringBuffer query=new StringBuffer();
        query.append(" SELECT * FROM EMPLOYEE where AUTHORITY = ? ");
        con = dataSourceTransactionManager.getConnection();
		pstmt = con.prepareStatement(query.toString());
		pstmt.setString(1, authority);
        rs = pstmt.executeQuery();
		
        while (rs.next()){
           	EmployeeBean employeeBean=new EmployeeBean();
            employeeBean.setEmpCode(rs.getString("EMP_CODE"));
            employeeBean.setEmpName(rs.getString("EMP_NAME"));
            employeeBean.setAuthority(rs.getString("AUTHORITY"));
           	empList.add(employeeBean);
           }
        if(logger.isDebugEnabled()){ logger.debug(" EmployeeDAOImpl : findSYSList 종료 "); }
        	return empList;
	    } catch (Exception sqle) { 
				// TODO Auto-generated catch block
	    	logger.fatal(sqle.getMessage());
	    	throw new DataAccessException(sqle.getMessage());
	    		
		}finally{
			dataSourceTransactionManager.close(pstmt, rs);
		}
	}

}