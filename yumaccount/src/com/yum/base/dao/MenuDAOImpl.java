package com.yum.base.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yum.base.dao.MenuDAO;
import com.yum.base.dao.MenuDAOImpl;
import com.yum.base.to.MenuBean;
import com.yum.common.db.DataSourceTransactionManager;
import com.yum.common.exception.DataAccessException;

public class MenuDAOImpl implements MenuDAO{
	 private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();
	
	 protected final Log logger = LogFactory.getLog(this.getClass());
	 
	 private static MenuDAO instance = new MenuDAOImpl();
	 private MenuDAOImpl(){}
	 public static MenuDAO getInstance() {
	      return instance; 
	   }
	@Override
	public ArrayList<MenuBean> selectMenuList(String empCode) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" MenuDAOImpl : selectMenuList 시작"); }
		System.out.println("		@ 넘겨받은 empCode: "+empCode);
		ArrayList<MenuBean> menuList = new ArrayList<MenuBean>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
	         StringBuffer query = new StringBuffer();
	         query.append(" SELECT M.MENU_CODE, M.MENU_NAME, M.SUPER_MENU_CODE, M.MENU_URL, A.AUTHORITY_NAME ");
	         query.append(" FROM EMPLOYEE E, AUTHORITY A, MENU_AUTHORITY MA, MENU M ");
	         query.append(" WHERE E.AUTHORITY = A.AUTHORITY ");
	         query.append("  AND A.AUTHORITY = MA.AUTHORITY ");
	         query.append("  AND MA.MENU_CODE = M.MENU_CODE ");
	         query.append("  AND E.EMP_CODE=? ORDER BY M.MENU_CODE ");
	         con = dataSourceTransactionManager.getConnection();
	         pstmt = con.prepareStatement(query.toString());
	         pstmt.setString(1, empCode);
	         rs = pstmt.executeQuery();
	         while (rs.next()) {
	            MenuBean menuBean = new MenuBean();
	            menuBean.setMenuCode(rs.getString("MENU_CODE"));
	            menuBean.setMenuName(rs.getString("MENU_NAME"));
	            menuBean.setSuperMenuCode(rs.getString("SUPER_MENU_CODE"));
	            menuBean.setMenuUrl(rs.getString("MENU_URL"));
	            menuBean.setAuthorityName(rs.getString("AUTHORITY_NAME"));
	            menuList.add(menuBean);
	         
	         }
	         
	         
	         System.out.println("		@ 메뉴 권한: "+menuList.get(0).getAuthorityName());
	         
	         
	         if(logger.isDebugEnabled()){ logger.debug(" MenuDAOImpl : selectMenuList 종료 "); }
	             return menuList;
	             
	         
	      } catch (Exception sqle) { 
	    	  logger.fatal(sqle.getMessage());
	          throw new DataAccessException(sqle.getMessage());
	      } finally {
	         dataSourceTransactionManager.close(pstmt, rs);
	      }
	}
	@Override
	public ArrayList<MenuBean> selectAllMenuList() {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" MenuDAOImpl : selectAllMenuList 시작 "); }
		 ArrayList<MenuBean> menuList = new ArrayList<MenuBean>();
		      
		      Connection con = null;
		      PreparedStatement pstmt = null;
		      ResultSet rs = null;
		      try {
		         StringBuffer query = new StringBuffer();
		         query.append(" SELECT * FROM MENU ORDER BY MENU_CODE");	 
		         con = dataSourceTransactionManager.getConnection();
		         pstmt = con.prepareStatement(query.toString());	         
		         rs = pstmt.executeQuery();
		         while (rs.next()) {
		            MenuBean menuBean = new MenuBean();
		            menuBean.setMenuCode(rs.getString("MENU_CODE"));
		            menuBean.setMenuName(rs.getString("MENU_NAME"));
		            menuBean.setSuperMenuCode(rs.getString("SUPER_MENU_CODE"));
		            menuBean.setMenuUrl(rs.getString("MENU_URL"));	            
		            menuList.add(menuBean);
		         }
		         System.out.println("		@ 메뉴경로 : "+menuList.get(0).getMenuUrl());
		         if(logger.isDebugEnabled()){ logger.debug(" MenuDAOImpl : selectAllMenuList 종료 "); }
		        return menuList;
		      } catch (Exception sqle) {
		    	  logger.fatal(sqle.getMessage());
		         throw new DataAccessException(sqle.getMessage());
		      } finally {
		         dataSourceTransactionManager.close(pstmt, rs);
		      }
	   }

}
