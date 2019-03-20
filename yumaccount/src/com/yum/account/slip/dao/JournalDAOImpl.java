package com.yum.account.slip.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yum.account.slip.dao.JournalDAO;
import com.yum.account.slip.dao.JournalDAOImpl;
import com.yum.account.slip.to.JournalBean;
import com.yum.common.db.DataSourceTransactionManager;
import com.yum.common.exception.DataAccessException;

public class JournalDAOImpl implements JournalDAO{
	protected final Log logger = LogFactory.getLog(this.getClass());

	private DataSourceTransactionManager dataSourceTransactionManager
	   = DataSourceTransactionManager.getInstance();
	
	private static JournalDAO instance;
	private JournalDAOImpl(){}
	public static JournalDAO getInstance() {
		// TODO Auto-generated method stub
		if(instance == null){
			instance = new JournalDAOImpl();
			System.out.println("		@ JournalDAOImpl에 접근");
		}
		return instance;
	}
	

	@Override
	public ArrayList<JournalBean> selectJournalList(String slipNo) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" JournalDAOImpl : selectJournalList 시작 "); }
			Connection conn = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
	        ArrayList<JournalBean> journalList = new ArrayList<>();
	         try {
	            StringBuffer query = new StringBuffer();
	            query.append("SELECT * FROM JOURNAL WHERE SLIP_NO=?");
	            conn = dataSourceTransactionManager.getConnection();
	            pstmt = conn.prepareStatement(query.toString());
	            pstmt.setString(1, slipNo);
	            rs = pstmt.executeQuery();
	            while(rs.next()){
	               JournalBean journalBean=new JournalBean();
	               journalBean.setJournalCode(rs.getString("JOURNAL_CODE"));
	               journalBean.setSlipNo(rs.getString("SLIP_NO"));
	               journalBean.setBalanceDivision(rs.getString("BALANCE_DIVISION"));
	               journalBean.setAccountCode(rs.getString("ACCOUNT_CODE"));
	               journalBean.setAccountName(rs.getString("ACCOUNT_NAME"));
	               journalBean.setCustomerCode(rs.getString("CUSTOMER_CODE"));
	               journalBean.setCustomerName(rs.getString("CUSTOMER_NAME"));
	               journalBean.setPrice(rs.getString("PRICE"));
	               journalBean.setVoucherCode(rs.getString("VOUCHER_CODE"));
	               journalBean.setVoucherName(rs.getString("VOUCHER_NAME"));
	               journalBean.setDescCode(rs.getString("DESC_CODE"));
	               journalBean.setDescName(rs.getString("DESC_NAME"));
	               journalList.add(journalBean);
	               System.out.println("		@ 분개 조회됨");
	            }
	    		if(logger.isDebugEnabled()){ logger.debug(" JournalDAOImpl : selectJournalList 종료 "); }
	            return journalList;
	         } catch(Exception sqle) {
	        	 logger.fatal(sqle.getMessage());
	            throw new DataAccessException(sqle.getMessage());
	         } finally {
	            dataSourceTransactionManager.close(pstmt, rs);
	         }
	}
	@Override
	public void insertJournal(JournalBean journalBean) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" JournalDAOImpl : insertJournal 시작 "); }
		 Connection conn = null;
	        PreparedStatement pstmt = null;
	        try{
	           StringBuffer query = new StringBuffer();
	           query.append("INSERT INTO JOURNAL VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
	           conn = dataSourceTransactionManager.getConnection();
	           pstmt=conn.prepareStatement(query.toString());
	           pstmt.setString(1,journalBean.getJournalCode());
	           pstmt.setString(2,journalBean.getSlipNo());
	           pstmt.setString(3,journalBean.getBalanceDivision());
	           pstmt.setString(4,journalBean.getAccountCode());
	           pstmt.setString(5,journalBean.getAccountName());
	           pstmt.setString(6,journalBean.getCustomerCode());
	           pstmt.setString(7,journalBean.getCustomerName());
	           pstmt.setString(8,journalBean.getPrice());
	           pstmt.setString(9,journalBean.getVoucherCode());
	           pstmt.setString(10,journalBean.getVoucherName());
	           pstmt.setString(11,journalBean.getDescCode());
	           pstmt.setString(12,journalBean.getDescName());
	           pstmt.executeUpdate();
	           System.out.println("		@ 분개 추가됨");
	        } catch(Exception sqle) {
	        	logger.fatal(sqle.getMessage());
	           throw new DataAccessException(sqle.getMessage());
	         } finally {
	            dataSourceTransactionManager.close(pstmt);
	         }
			if(logger.isDebugEnabled()){ logger.debug(" JournalDAOImpl : insertJournal 종료 "); }
	}
	@Override
	public void deleteJournal(JournalBean journalBean) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" JournalDAOImpl : deleteJournal 시작 "); }

		Connection conn = null;
		PreparedStatement pstmt = null;
		dataSourceTransactionManager.beginTransaction();
      try{
         StringBuffer query = new StringBuffer();
         query.append("DELETE FROM JOURNAL WHERE SLIP_NO =? AND JOURNAL_CODE=?");
         conn = dataSourceTransactionManager.getConnection();
         pstmt = conn.prepareStatement(query.toString());
         pstmt.setString(1, journalBean.getSlipNo());
         pstmt.setString(2, journalBean.getJournalCode());
         System.out.println(journalBean.getSlipNo());
         pstmt.executeUpdate();
         dataSourceTransactionManager.commitTransaction();
         System.out.println("		@ 분개 삭제됨");
      }catch(Exception sqle){
    	  logger.fatal(sqle.getMessage());
          throw new DataAccessException(sqle.getMessage());
      }finally{
          dataSourceTransactionManager.close(pstmt);
      }
      if(logger.isDebugEnabled()){ logger.debug(" JournalDAOImpl : deleteJournal 종료 "); }
		
	}
	@Override
	public void updateJournal(JournalBean journalBean) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" JournalDAOImpl : updateJournal 시작 "); }
		 Connection conn = null;
	        PreparedStatement pstmt = null;
	        try{
	           StringBuffer query = new StringBuffer();
	           query.append("UPDATE JOURNAL SET ");
	           query.append("BALANCE_DIVISION=?, ACCOUNT_CODE=?, ACCOUNT_NAME=?, CUSTOMER_CODE=?,");
	           query.append("CUSTOMER_NAME=?, PRICE=?, VOUCHER_CODE=?, VOUCHER_NAME=?,");
	           query.append("DESC_CODE=?, DESC_NAME=?");
	           query.append("WHERE SLIP_NO =? AND JOURNAL_CODE =?");
	           conn = dataSourceTransactionManager.getConnection();
	           pstmt=conn.prepareStatement(query.toString());
	           pstmt.setString(1,journalBean.getBalanceDivision());
	           pstmt.setString(2,journalBean.getAccountCode());
	           pstmt.setString(3,journalBean.getAccountName());
	           pstmt.setString(4,journalBean.getCustomerCode());
	           pstmt.setString(5,journalBean.getCustomerName());
	           pstmt.setString(6,journalBean.getPrice());
	           pstmt.setString(7,journalBean.getVoucherCode());
	           pstmt.setString(8,journalBean.getVoucherName());
	           pstmt.setString(9,journalBean.getDescCode());
	           pstmt.setString(10,journalBean.getDescName());
	           pstmt.setString(11,journalBean.getSlipNo());
	           pstmt.setString(12,journalBean.getJournalCode());
	           pstmt.executeUpdate();
	           System.out.println("		@ 분개 수정됨");
	        } catch(Exception sqle) {
	        	logger.fatal(sqle.getMessage());
	           throw new DataAccessException(sqle.getMessage());
	         } finally {
	            dataSourceTransactionManager.close(pstmt);
	         }
	        if(logger.isDebugEnabled()){ logger.debug(" JournalDAOImpl : updateJournal 종료 "); }
		
	}

}
