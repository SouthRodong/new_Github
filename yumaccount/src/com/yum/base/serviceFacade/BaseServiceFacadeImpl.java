package com.yum.base.serviceFacade;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yum.base.applicationService.BaseApplicationService;
import com.yum.base.applicationService.BaseApplicationServiceImpl;
import com.yum.base.applicationService.CodeListApplicationService;
import com.yum.base.applicationService.CodeListApplicationServiceImpl;
import com.yum.base.exception.DeptCodeNotFoundException;
import com.yum.base.exception.IdNotFoundException;
import com.yum.base.exception.PwMissmatchException;
import com.yum.base.to.AddressBean;
import com.yum.base.to.CodeBean;
import com.yum.base.to.DetailCodeBean;
import com.yum.base.to.MenuBean;
import com.yum.common.db.DataSourceTransactionManager;
import com.yum.common.exception.DataAccessException;
import com.yum.base.serviceFacade.BaseServiceFacade;
import com.yum.base.serviceFacade.BaseServiceFacadeImpl;



public class BaseServiceFacadeImpl implements BaseServiceFacade{
	
	protected final Log logger = LogFactory.getLog(this.getClass());

	private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance(); 
	BaseApplicationService baseCodeApplicationService = BaseApplicationServiceImpl.getInstance();
	
	CodeListApplicationService codeListApplicationService = CodeListApplicationServiceImpl.getInstance();
	
	private static BaseServiceFacade instance = new BaseServiceFacadeImpl ();
		
	private BaseServiceFacadeImpl(){};  
	public static BaseServiceFacade getInstance() {
		// TODO Auto-generated method stub
		return instance;
	}
	
	@Override						
	public HashMap<String, Object> accessToAuthority(String empCode, String empPassword, String deptCode) throws IdNotFoundException,DeptCodeNotFoundException, PwMissmatchException {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" BaseServiceFacadeImpl : accessToAuthority 시작 "); }
		dataSourceTransactionManager.beginTransaction();
		System.out.println("		@ DB 접근 : accessToAuthority");
		HashMap<String,Object> result=null;
		try{
			result=baseCodeApplicationService.accessToAuthority(empCode, empPassword,deptCode);
			dataSourceTransactionManager.commitTransaction();
			System.out.println("		@ DB 커밋");
		}catch(DataAccessException e){
			logger.fatal(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			   //throw e;
			System.out.println("		@ DB 롤백");
		}
		if(logger.isDebugEnabled()){ logger.debug(" BaseServiceFacadeImpl : accessToAuthority 종료 "); }
		return result;
	}
	@Override
	public ArrayList<MenuBean> findUserMenuList(String empCode) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" BaseServiceFacadeImpl : findUserMenuList 시작 "); }
		dataSourceTransactionManager.beginTransaction();
		System.out.println("		@ DB 접근 : findUserMenuList");
		ArrayList<MenuBean> menuList=null;
		try{
			menuList = baseCodeApplicationService.findMenuCodeList(empCode);
			dataSourceTransactionManager.commitTransaction();
			System.out.println("		@ DB 커밋");
		}catch(DataAccessException e){
			logger.fatal(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			System.out.println("		@ DB 롤백");
			//throw e;
			
		}
		if(logger.isDebugEnabled()){ logger.debug(" BaseServiceFacadeImpl : findUserMenuList 종료 "); }
		return menuList;
	}
	@Override
	public ArrayList<DetailCodeBean> findDetailCodeList(String code) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" BaseServiceFacadeImpl : findDetailCodeList 시작 "); }			
		dataSourceTransactionManager.beginTransaction();
		System.out.println("		@ DB 접근 : findDetailCodeList");
		ArrayList<DetailCodeBean> datailCondeList=null;
		try{
		datailCondeList = codeListApplicationService.findDetailCodeList(code);
		System.out.println("		@ 코드 상세 정보: "+datailCondeList.get(0).getCodeName());
		}catch(DataAccessException e){  
			logger.fatal(e.getMessage());
			throw e; 
		}
		if(logger.isDebugEnabled()){ logger.debug(" BaseServiceFacadeImpl : findDetailCodeList 종료 "); }
		return datailCondeList;
	}
	@Override
	public ArrayList<AddressBean> findRoadList(String sido, String sigunguname, String roadname) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" BaseServiceFacadeImpl : findRoadList 시작 "); }
		dataSourceTransactionManager.beginTransaction();
		ArrayList<AddressBean> postRoadList=null;

		try{
			postRoadList=baseCodeApplicationService.findRoadList(sido, sigunguname, roadname);
			System.out.println("		@ 주소 얻어오기: 시/도= "+sido+", 시/군/구= "+sigunguname+", 도로명= "+roadname);
			dataSourceTransactionManager.commitTransaction();
			System.out.println("		@ DB 커밋");
		}catch(Exception e){
			logger.fatal(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			System.out.println("		@ DB 롤백");
			//throw e;
		}
		if(logger.isDebugEnabled()){ logger.debug(" BaseServiceFacadeImpl : findRoadList 종료 "); }
		return postRoadList;
	}
	@Override
	public ArrayList<AddressBean> findSigunguList(String parameter) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" BaseServiceFacadeImpl : findSigunguList 시작 "); }
			dataSourceTransactionManager.beginTransaction();
				ArrayList<AddressBean> postSigunguList=null;

				try{
					postSigunguList=baseCodeApplicationService.findSigunguList(parameter);
					System.out.println("		@ 시/군/구: "+postSigunguList.get(0).getSigungu());
					dataSourceTransactionManager.commitTransaction();
					System.out.println("		@ DB 커밋");
				}catch(Exception e){
					logger.fatal(e.getMessage());
					dataSourceTransactionManager.rollbackTransaction();
					System.out.println("		@ DB 롤백");
					//throw e;
				}
				if(logger.isDebugEnabled()){ logger.debug(" BaseServiceFacadeImpl : findSigunguList 종료 "); }
				return postSigunguList;
	}
	@Override
	public ArrayList<AddressBean> findSidoList() {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" BaseServiceFacadeImpl : findSidoList 시작 "); }
		dataSourceTransactionManager.beginTransaction();
		System.out.println("		@ DB 접근");
		ArrayList<AddressBean> postSidoList=null;

		try{
			postSidoList=baseCodeApplicationService.findSidoList();
			System.out.println("		@ DB 시/도: "+postSidoList.get(0).getSido());
			dataSourceTransactionManager.commitTransaction();
			System.out.println("		@ DB 커밋");

		}catch(Exception e){
			logger.fatal(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			System.out.println("		@ DB 롤백");
			//throw e;
		}
		if(logger.isDebugEnabled()){ logger.debug(" BaseServiceFacadeImpl : findSidoList 종료 "); }
		return postSidoList;
	}
	@Override
	public ArrayList<AddressBean> findPostList(String dong) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" BaseServiceFacadeImpl : findPostList 시작 "); }
		dataSourceTransactionManager.beginTransaction();
		System.out.println("		@ DB 접근 : findPostList");
		ArrayList<AddressBean> postList=null;
		try{
			postList=baseCodeApplicationService.findPostList(dong);
			System.out.println("		@ 우편번호: "+postList.get(0).getZipcode());
			dataSourceTransactionManager.commitTransaction();
			System.out.println("		@ DB 커밋");
		}catch(Exception e){
			logger.fatal(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			System.out.println("		@ DB 롤백");
			//throw e;
			}
		if(logger.isDebugEnabled()){ logger.debug(" BaseServiceFacadeImpl : findPostList 종료 "); }
			return postList;
	}
	@Override
	public ArrayList<CodeBean> findCodeList() {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" BaseServiceFacadeImpl : findCodeList 시작 "); }
		dataSourceTransactionManager.beginTransaction();
		System.out.println("		@ DB 접근 : findCodeList");
		ArrayList<CodeBean> codeList = null;
		try{
		codeList = codeListApplicationService.findCodeList();
		System.out.println("		@ 코드리스트: "+codeList.get(0).getDivisionCode());
		}catch(DataAccessException e){
			logger.fatal(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			System.out.println("		@ 베이스 퍼사드 조회 오류");
			System.out.println("		@ DB 롤백");
			//throw e;
		}
		if(logger.isDebugEnabled()){ logger.debug(" BaseServiceFacadeImpl : findCodeList 종료 "); }
		return codeList;
	}
	@Override
	public void batchCodeProcess(ArrayList<CodeBean> codeList) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" BaseServiceFacadeImpl : batchCodeProcess 시작 "); }
		dataSourceTransactionManager.beginTransaction();
		System.out.println("		@ DB 접근 : batchCodeProcess");
		try{
			
			codeListApplicationService.batchCodeProcess(codeList); 
			System.out.println("		@ codeListApplicationService에 넘어가는 데이터: "+codeList.get(0).getDivisionCode());
			dataSourceTransactionManager.commitTransaction();
			System.out.println("		@ DB 커밋");
		}catch(DataAccessException e){
			logger.fatal(e.getMessage());

			dataSourceTransactionManager.rollbackTransaction();
			System.out.println("		@ DB 롤백");
			//throw e;
		}
		if(logger.isDebugEnabled()){ logger.debug(" BaseServiceFacadeImpl : batchCodeProcess 종료 "); }
	}
	
	

}
