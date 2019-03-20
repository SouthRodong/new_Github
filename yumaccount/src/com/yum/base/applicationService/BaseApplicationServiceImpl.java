package com.yum.base.applicationService;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yum.base.applicationService.BaseApplicationServiceImpl;
import com.yum.base.applicationService.BaseApplicationService;
import com.yum.base.dao.AddressDAO;
import com.yum.base.dao.AddressDAOImpl;
import com.yum.base.dao.MenuDAO;
import com.yum.base.dao.MenuDAOImpl;
import com.yum.base.exception.DeptCodeNotFoundException;
import com.yum.base.exception.IdNotFoundException;
import com.yum.base.exception.PwMissmatchException;
import com.yum.base.to.AddressBean;
import com.yum.base.to.MenuBean;
import com.yum.common.exception.DataAccessException;
import com.yum.hr.dao.EmployeeDAO;
import com.yum.hr.dao.EmployeeDAOImpl;
import com.yum.hr.to.EmployeeBean;

public class BaseApplicationServiceImpl implements BaseApplicationService{

	protected final Log logger = LogFactory.getLog(this.getClass());
	
	private static BaseApplicationServiceImpl instance = new BaseApplicationServiceImpl();
	private BaseApplicationServiceImpl(){} 
	public static BaseApplicationService getInstance() {
		// TODO Auto-generated method stub
		System.out.println("		@ BaseApplicationServiceImpl 객체접근");
		return instance;
	}
	private MenuDAO menuDAO = MenuDAOImpl.getInstance(); 
	private EmployeeDAO employeeDAO = EmployeeDAOImpl.getInstance();
	private AddressDAO addressDAO  = AddressDAOImpl.getInstance();
	

	@Override
	public HashMap<String, Object> accessToAuthority(String empCode, String empPassword,String deptCode) throws IdNotFoundException,DeptCodeNotFoundException, PwMissmatchException ,DataAccessException{
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" BaseApplicationServiceImpl : accessToAuthority 시작 "); }
		
		EmployeeBean employeeBean = null;
		ArrayList<MenuBean> menuList = null;
		ArrayList<MenuBean> masterMenuList = null;
		HashMap<String, Object> result = new HashMap<>();
		try{
			employeeBean = employeeDAO.selectCompanyStaff(empCode);
		
			if(employeeBean==null){
				
	        	throw new IdNotFoundException(" 존재 하지 않는 계정입니다. ");
			}else{
				
				if(!(employeeBean.getDeptCode().equals(deptCode))) {
					throw new DeptCodeNotFoundException("존재하지않는 부서입니다");
				}else {
				if(employeeBean.getEmpPassword().equals(empPassword)){	
					
					
	        		menuList=menuDAO.selectMenuList(empCode);		
	        		masterMenuList=menuDAO.selectAllMenuList();	  
	        		System.out.println("		@ 메뉴경로 : "+menuList.get(0).getMenuUrl());
	        		System.out.println("		@ 권한이름 : "+menuList.get(0).getAuthorityName());
	        		System.out.println("		@ 상위코드 : "+masterMenuList.get(0).getMenuCode());
	        		result.put("employeeBean", employeeBean);
	        		result.put("menuList", menuList);
	        		result.put("masterMenuList", masterMenuList);
	        		if(logger.isDebugEnabled()){ logger.debug(" BaseApplicationServiceImpl : accessToAuthority 종료 "); }
	        		
	    		}else{
	    			
	    			throw new PwMissmatchException(" 비밀번호가 틀립니다. ");
	    		}
				
				}
				
			}
		}catch(DataAccessException e){
			logger.fatal(e.getMessage());
			throw e;			
		}
		return result;
				
	}
	
	@Override
	public ArrayList<MenuBean> findMenuCodeList(String empCode) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" BaseApplicationServiceImpl : findMenuCodeList 시작 "); }
		ArrayList<MenuBean> menuList = null;
		try{
			menuList = menuDAO.selectMenuList(empCode);
			
    		System.out.println("		@ 권한이름 : "+menuList.get(0).getAuthorityName());
    		System.out.println("		@ 메뉴코드 : "+menuList.get(0).getMenuCode());
		}catch(DataAccessException e){
			logger.fatal(e.getMessage());
			throw e;
			
		}
		if(logger.isDebugEnabled()){ logger.debug(" BaseApplicationServiceImpl : findMenuCodeList 종료 "); }
		return menuList;
	}
	@Override
	public ArrayList<AddressBean> findRoadList(String sido, String sigunguname, String roadname) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" BaseApplicationServiceImpl : findRoadList 시작 "); }
		ArrayList<AddressBean> postRoadList=null;
		try{
		postRoadList = addressDAO.selectRoadList(sido,sigunguname,roadname);
		System.out.println("		@ 시/도: "+sido+", 시/군/구: "+sigunguname+", 도로명: "+roadname);
		
		}catch (Exception e){
			logger.fatal(e.getMessage());
			throw e;		
		}
		if(logger.isDebugEnabled()){ logger.debug(" BaseApplicationServiceImpl : findRoadList 종료 "); }
	return postRoadList;
	}
	@Override
	public ArrayList<AddressBean> findSigunguList(String parameter) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" BaseApplicationServiceImpl : findSigunguList 시작 "); }
		ArrayList<AddressBean> postSigunguList=null;
		try{
		postSigunguList = addressDAO.selectSigunguList(parameter);
		System.out.println("		@ 파라메터: "+parameter);
		}catch (Exception e){
			logger.fatal(e.getMessage());
			throw e;		
		}
		if(logger.isDebugEnabled()){ logger.debug(" BaseApplicationServiceImpl : findSigunguList 종료 "); }
	return postSigunguList;

	}
	@Override
	public ArrayList<AddressBean> findSidoList() {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" BaseApplicationServiceImpl : findSidoList 시작 "); }
		ArrayList<AddressBean> postSidoList=null;
		try{
			postSidoList = addressDAO.selectSidoList();
		}catch (Exception e){
			logger.fatal(e.getMessage());
			throw e;		
		}
		if(logger.isDebugEnabled()){ logger.debug(" BaseApplicationServiceImpl : findSidoList 종료 "); }
		return postSidoList;
	
	}
	@Override
	public ArrayList<AddressBean> findPostList(String dong) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" BaseApplicationServiceImpl : findPostList 시작 "); }
		ArrayList<AddressBean> postList=null;
		try{
		postList = addressDAO.selectPostList(dong);
		System.out.println("		@ 동이름: "+dong);
		}catch (Exception e){
			logger.fatal(e.getMessage());
			throw e;		
		}
		if(logger.isDebugEnabled()){ logger.debug(" BaseApplicationServiceImpl : findPostList 종료 "); }
	return postList;
	}

	
}
