package com.yum.account.base.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yum.account.base.dao.AccountDetailDAO;
import com.yum.account.base.dao.AccountDetailDAOImpl;
import com.yum.account.base.to.AccountDetailBean;
import com.yum.common.db.DataSourceTransactionManager;
import com.yum.common.exception.DataAccessException;

public class AccountDetailDAOImpl implements AccountDetailDAO{
	protected final Log logger = LogFactory.getLog(this.getClass());

	private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();
	
	private static AccountDetailDAO instace;
	private AccountDetailDAOImpl(){}
	public static AccountDetailDAO getInstance() {
		// TODO Auto-generated method stub
		if(instace== null){
			instace =new AccountDetailDAOImpl();
		}
		return instace;
	}
	
	@Override
	public ArrayList<AccountDetailBean> findControlItemList() {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" AccountDetailDAOImpl : findControlItemList 시작 "); }
		Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<AccountDetailBean> controlItemList = new ArrayList<>();
        
        try{
            StringBuffer query = new StringBuffer();
            query.append("SELECT * FROM ACC_DETAIL");
            con = dataSourceTransactionManager.getConnection();
            pstmt = con.prepareStatement(query.toString());
            rs = pstmt.executeQuery();
            while(rs.next()){
            	AccountDetailBean controlItemBean = new AccountDetailBean();
            	controlItemBean.setAccountControlCode(rs.getString("ACCOUNT_CONTROL_CODE"));
            	controlItemBean.setAccountControlName(rs.getString("ACCOUNT_CONTROL_NAME"));
            	controlItemBean.setAccountType(rs.getString("ACCOUNT_TYPE"));            	
                controlItemList.add(controlItemBean);
            }
            if(logger.isDebugEnabled()){ logger.debug(" AccountDetailDAOImpl : findControlItemList 종료 "); }
            return controlItemList;
        }catch(Exception sqle){
        	logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
         }finally{
        	 dataSourceTransactionManager.close(pstmt, rs);
         }
	}

}
