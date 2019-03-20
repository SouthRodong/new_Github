package com.yum.account.base.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yum.account.base.dao.AccountDAO;
import com.yum.account.base.dao.AccountDAOImpl;
import com.yum.account.base.to.AccountBean;
import com.yum.common.db.DataSourceTransactionManager;
import com.yum.common.exception.DataAccessException;

public class AccountDAOImpl implements AccountDAO{
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	private DataSourceTransactionManager dataSourceTransactionManager=DataSourceTransactionManager.getInstance();

	
	private static AccountDAO instance;
	private AccountDAOImpl(){}
	public static AccountDAO getInstance() {
		// TODO Auto-generated method stub
		if(instance == null){
			instance = new AccountDAOImpl();
		}
		return instance;
	}

	@Override
	public ArrayList<AccountBean> selectAccountList() {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" AccountDAOImpl : selectAccountList 시작 "); }
		 	Connection con = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
	        ArrayList<AccountBean> accountList = new ArrayList<>();
	        try {
	            StringBuffer query = new StringBuffer();
	            
				query.append("SELECT A.*, LEVEL, ");
				query.append("(CASE WHEN CONNECT_BY_ISLEAF = 0 OR LEVEL = 1 THEN 'FALSE' ELSE 'TRUE' END) LEAF ");
				query.append("FROM ACCOUNT A ");
				query.append("START WITH A.SUPER_ACCOUNT_CODE IS NULL ");
				query.append("CONNECT BY A.SUPER_ACCOUNT_CODE = PRIOR A.ACCOUNT_CODE ");
				query.append("ORDER SIBLINGS BY A.ACCOUNT_CODE");
	            
	            con = dataSourceTransactionManager.getConnection();
	            pstmt = con.prepareStatement(query.toString());
	            rs = pstmt.executeQuery();
	            while(rs.next()){
	            	AccountBean accountBean=new AccountBean();
	            	accountBean.setAccountCode(rs.getString("ACCOUNT_CODE"));
	            	accountBean.setSuperAccountCode(rs.getString("SUPER_ACCOUNT_CODE"));
	                accountBean.setAccountName(rs.getString("ACCOUNT_NAME"));
	                accountBean.setAccountUseCheck(rs.getString("ACCOUNT_USE_CHECK"));
	                accountBean.setLevel(rs.getInt("LEVEL"));
	                accountBean.setLeaf(rs.getString("LEAF"));
	                accountBean.setLoaded("true");
	                accountBean.setExpanded("true");
	                accountList.add(accountBean);
	            }
	            if(logger.isDebugEnabled()){ logger.debug(" AccountDAOImpl : selectAccountList 종료 "); }
	            return accountList;
	        }catch(Exception sqle){
	        	logger.fatal(sqle.getMessage());
	            throw new DataAccessException(sqle.getMessage());
	        }finally{
	        	 dataSourceTransactionManager.close(pstmt, rs);
	        }
	}

}
