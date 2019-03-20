package com.yum.account.base.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yum.account.base.dao.AccountControlDAO;
import com.yum.account.base.dao.AccountControlDAOImpl;
import com.yum.account.base.to.AccountControlBean;
import com.yum.common.db.DataSourceTransactionManager;
import com.yum.common.exception.DataAccessException;

public class AccountControlDAOImpl implements AccountControlDAO{
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();

	private static AccountControlDAO instance;
	private AccountControlDAOImpl(){}
	public static AccountControlDAO getInstance() {
		// TODO Auto-generated method stub
		if(instance ==null){
			instance = new AccountControlDAOImpl();
		}
		return instance;
	}

	@Override
	public ArrayList<AccountControlBean> selectAccountControlList(String accountCode) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" AccountControlDAOImpl : selectAccountControlList 시작 "); }
		Connection con = null;
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	ArrayList<AccountControlBean> accountControlList = new ArrayList<>();
        try{
        	StringBuffer query = new StringBuffer();
        	
            query.append("SELECT * FROM ACC_CONTROL AC ,ACC_DETAIL CI ");
            query.append("WHERE AC.ACCOUNT_CONTROL_CODE=CI.ACCOUNT_CONTROL_CODE AND AC.ACCOUNT_CODE=? ");            
            con = dataSourceTransactionManager.getConnection();
            pstmt = con.prepareStatement(query.toString());
            pstmt.setString(1, accountCode);            
            rs = pstmt.executeQuery();
            while(rs.next()){
            	AccountControlBean accountControlBean = new AccountControlBean();
                accountControlBean.setAccountCode(rs.getString("ACCOUNT_CODE"));
                accountControlBean.setAccountControlCode(rs.getString("ACCOUNT_CONTROL_CODE"));
                accountControlBean.setControlName(rs.getString("CONTROL_NAME"));
                accountControlBean.setInputType(rs.getString("INPUT_TYPE"));
                accountControlBean.setAccountControlCheck(rs.getString("ACCOUNT_CONTROL_CHECK"));
                accountControlList.add(accountControlBean);
            }
           if(logger.isDebugEnabled()){ logger.debug(" AccountControlDAOImpl : selectAccountControlList 종료 "); }
            return accountControlList;
        }catch(Exception sqle){
        	logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
         }finally{
        	 dataSourceTransactionManager.close(pstmt, rs);
         }
	}
	@Override
	public void insertAccountControl(AccountControlBean accountControl) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" AccountControlDAOImpl : insertAccountControl 시작 "); }
		  	Connection con = null;
	        PreparedStatement pstmt = null;
	        try {
	            StringBuffer query = new StringBuffer();
	            query.append(" INSERT INTO ACC_CONTROL VALUES(?, ?, ?, ?, ?)");
	            con = dataSourceTransactionManager.getConnection();
	            pstmt = con.prepareStatement(query.toString());
	            pstmt.setString(1, accountControl.getAccountCode());
	            pstmt.setString(2, accountControl.getAccountControlCode());
	            pstmt.setString(3, accountControl.getControlName());
	            pstmt.setString(4, accountControl.getInputType());
	            pstmt.setString(5, accountControl.getAccountControlCheck());
	            pstmt.executeUpdate();
	        
	        } catch(Exception sqle) {
	        	logger.fatal(sqle.getMessage());
				throw new DataAccessException(sqle.getMessage());
			} finally {
				dataSourceTransactionManager.close(pstmt);
			}
	        if(logger.isDebugEnabled()){ logger.debug(" AccountControlDAOImpl : insertAccountControl 종료 "); }
	}
	@Override
	public void deleteAccountControl(AccountControlBean accountControl) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" AccountControlDAOImpl : deleteAccountControl 시작 "); }
		 Connection con = null;
	        PreparedStatement pstmt = null;
	        try {
	            StringBuffer query = new StringBuffer();
	            query.append(" DELETE FROM ACC_CONTROL WHERE ACCOUNT_CONTROL_CODE = ? AND ACCOUNT_CODE =?");
	            con = dataSourceTransactionManager.getConnection();
	            pstmt = con.prepareStatement(query.toString());
	            pstmt.setString(1, accountControl.getAccountControlCode());
	            pstmt.setString(2, accountControl.getAccountCode());
	            
	            pstmt.executeUpdate();
	           
	        } catch(Exception sqle) {
	        	logger.fatal(sqle.getMessage());
				throw new DataAccessException(sqle.getMessage());
			} finally {
				dataSourceTransactionManager.close(pstmt);
			}
	        if(logger.isDebugEnabled()){ logger.debug(" AccountControlDAOImpl : deleteAccountControl 종료 "); }
	}
	
	
}
