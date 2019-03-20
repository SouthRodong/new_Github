package com.yum.account.slip.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yum.account.slip.dao.SlipDAO;
import com.yum.account.slip.dao.SlipDAOImpl;
import com.yum.account.slip.to.SlipBean;
import com.yum.common.db.DataSourceTransactionManager;
import com.yum.common.exception.DataAccessException;

public class SlipDAOImpl implements SlipDAO{
	protected final Log logger = LogFactory.getLog(this.getClass());
	private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();
	
	private static SlipDAO instace;
	private SlipDAOImpl(){}
	public static SlipDAO getInstance() {
		// TODO Auto-generated method stub
		if(instace == null){
			instace = new SlipDAOImpl();
			System.out.println("		@ SlipDAOImpl에 접근");
		}
		return instace;
	}
	

	@Override
	public ArrayList<SlipBean> selectSlipDataList(String slipDate) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" SlipDAOImpl : selectSlipDataList 시작 "); }
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<SlipBean> slipList = new ArrayList<>();
		try {
			StringBuffer query = new StringBuffer();
			query.append(" SELECT * FROM SLIP WHERE WRITE_DATE =? ORDER BY SLIP_NO ");
			conn = dataSourceTransactionManager.getConnection();
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, slipDate);
			System.out.println("		@ 조회할 일자: "+slipDate);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				SlipBean slipBean = new SlipBean();
				slipBean.setSlipNo(rs.getString("SLIP_NO"));
				slipBean.setWriteEmpCode(rs.getString("WRITE_EMP_CODE"));
				slipBean.setApprovalEmpCode(rs.getString("APPROVAL_EMP_CODE"));
				slipBean.setRequestName(rs.getString("REQUEST_NAME"));
				slipBean.setRequestCode(rs.getString("REQUEST_CODE"));
				slipBean.setSlipType(rs.getString("SLIP_TYPE"));
				slipBean.setBalanceDiff(rs.getString("BALANCE_DIFF"));
				slipBean.setApprovalSeq(rs.getInt("APPROVAL_SEQ"));
				slipBean.setApprovalState(rs.getString("APPROVAL_STATE"));
				slipBean.setApprovalDate(rs.getString("APPROVAL_DATE"));
				slipBean.setSlipSeq(rs.getInt("SLIP_SEQ"));
				slipBean.setWriteDate(rs.getString("WRITE_DATE"));
				slipList.add(slipBean);
				System.out.println("		@ 전표 조회됨");
			}
			if(logger.isDebugEnabled()){ logger.debug(" SlipDAOImpl : selectSlipDataList 종료 "); }
			return slipList;
		} catch (Exception sqle) {
			logger.fatal(sqle.getMessage());
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt, rs);
		}
	}
	@Override
	public void deleteSlip(SlipBean slipBean) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" SlipDAOImpl : deleteSlip 시작 "); }
		Connection conn = null;
		PreparedStatement pstmt = null;
	    dataSourceTransactionManager.beginTransaction();

		try {
			StringBuffer query = new StringBuffer();
			query.append("DELETE FROM SLIP WHERE SLIP_NO = ?");
			conn = dataSourceTransactionManager.getConnection();
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, slipBean.getSlipNo());
			System.out.println("		@ 전표번호: "+slipBean.getSlipNo());
			pstmt.executeUpdate();
			dataSourceTransactionManager.commitTransaction();
			System.out.println("		@ 전표 삭제됨");
		} catch (Exception sqle) {
			logger.fatal(sqle.getMessage());
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt);
		}
		if(logger.isDebugEnabled()){ logger.debug(" SlipDAOImpl : deleteSlip 종료 "); }
	}
		
	
	@Override
	public void updateSlip(SlipBean slipBean) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" SlipDAOImpl : updateSlip 시작 "); }
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			StringBuffer query = new StringBuffer();
			query.append(" UPDATE SLIP SET ");
			query.append("WRITE_EMP_CODE=?, APPROVAL_EMP_CODE=?, REQUEST_NAME=?, REQUEST_CODE=?, SLIP_TYPE=?, ");
			query.append("BALANCE_DIFF=?, APPROVAL_SEQ=?, APPROVAL_STATE=?, APPROVAL_DATE=?, ");
			query.append("SLIP_SEQ=?, WRITE_DATE=? ");
			query.append("WHERE SLIP_NO=? ");
			conn = dataSourceTransactionManager.getConnection();
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, slipBean.getWriteEmpCode());
			pstmt.setString(2, slipBean.getApprovalEmpCode());
			pstmt.setString(3, slipBean.getRequestName());
			pstmt.setString(4, slipBean.getRequestCode());
			pstmt.setString(5, slipBean.getSlipType());
			pstmt.setString(6, slipBean.getBalanceDiff());
			pstmt.setInt(7, slipBean.getApprovalSeq());
			pstmt.setString(8, slipBean.getApprovalState());
			pstmt.setString(9, slipBean.getApprovalDate());
			pstmt.setInt(10, slipBean.getSlipSeq());
			pstmt.setString(11, slipBean.getWriteDate());
			pstmt.setString(12, slipBean.getSlipNo());
			pstmt.executeUpdate();
			System.out.println("		@ 전표번호: "+slipBean.getSlipNo());
			System.out.println("		@ 전표 수정됨");
		} catch (Exception sqle) {
			logger.fatal(sqle.getMessage());
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt);
		}
		if(logger.isDebugEnabled()){ logger.debug(" SlipDAOImpl : updateSlip 종료 "); }

		
	}
	@Override
	public void insertSlip(SlipBean slipBean) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" SlipDAOImpl : insertSlip 시작 "); }
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			StringBuffer query = new StringBuffer();
			query.append(" INSERT INTO SLIP(SLIP_NO, WRITE_EMP_CODE, APPROVAL_EMP_CODE, REQUEST_NAME, ");
			query.append("REQUEST_CODE, SLIP_TYPE, BALANCE_DIFF, APPROVAL_SEQ,APPROVAL_STATE, APPROVAL_DATE, ");
			query.append("SLIP_SEQ, WRITE_DATE ) VALUES(?,?,?,?,?,?,?,APPROVALSEQ.NEXTVAL,?,?,RECORDSEQ.NEXTVAL,?) ");
			conn = dataSourceTransactionManager.getConnection();
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1, slipBean.getSlipNo());			
			//System.out.println(slipBean.getSlipNo());
			pstmt.setString(2, slipBean.getWriteEmpCode());
			pstmt.setString(3, slipBean.getApprovalEmpCode());
			pstmt.setString(4, slipBean.getRequestName());
			pstmt.setString(5, slipBean.getRequestCode());
			pstmt.setString(6, slipBean.getSlipType());
			pstmt.setString(7, slipBean.getBalanceDiff());			
			pstmt.setString(8, slipBean.getApprovalState());
			pstmt.setString(9, slipBean.getApprovalDate());
			pstmt.setString(10, slipBean.getWriteDate()); 
			//pstmt.setInt(11, slipBean.getSlipSeq());
			
			pstmt.executeUpdate();
			System.out.println("		@ 전표번호: "+slipBean.getSlipNo());
			System.out.println("		@ 전표 추가됨");
		} catch (Exception sqle) {
			logger.fatal(sqle.getMessage());
				throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt);
		}
		if(logger.isDebugEnabled()){ logger.debug(" SlipDAOImpl : insertSlip 종료 "); }
		
	}
	@Override
	public ArrayList<SlipBean> selectRangedSlipList(String fromDate, String toDate) {	//분개장 보기
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" SlipDAOImpl : selectRangedSlipList 시작 "); }
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<SlipBean> slipList = new ArrayList<>();
		try {
			StringBuffer query = new StringBuffer();
			query.append("SELECT * FROM SLIP WHERE WRITE_DATE BETWEEN ? AND ? ");
			query.append("AND APPROVAL_STATE = '승인완료' ");
			query.append("ORDER BY WRITE_DATE ");
			conn = dataSourceTransactionManager.getConnection();
			pstmt = conn.prepareStatement(query.toString());
			System.out.println("		@ 조회 일자");
			System.out.println("		==========" + fromDate + "부터");
			System.out.println("		==========" + toDate + "까지");
			
			pstmt.setString(1, fromDate);
			pstmt.setString(2, toDate);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				SlipBean slipBean = new SlipBean();
				slipBean.setSlipNo(rs.getString("SLIP_NO"));
				slipBean.setWriteEmpCode(rs.getString("WRITE_EMP_CODE"));
				slipBean.setApprovalEmpCode(rs.getString("APPROVAL_EMP_CODE"));
				slipBean.setRequestName(rs.getString("REQUEST_NAME"));
				slipBean.setRequestCode(rs.getString("REQUEST_CODE"));
				slipBean.setSlipType(rs.getString("SLIP_TYPE"));
				slipBean.setBalanceDiff(rs.getString("BALANCE_DIFF"));
				slipBean.setApprovalSeq(rs.getInt("APPROVAL_SEQ"));
				slipBean.setApprovalState(rs.getString("APPROVAL_STATE"));
				slipBean.setApprovalDate(rs.getString("APPROVAL_DATE"));
				slipBean.setSlipSeq(rs.getInt("SLIP_SEQ"));
				slipBean.setWriteDate(rs.getString("WRITE_DATE"));
				slipList.add(slipBean);
			}
		} catch (Exception sqle) {
			logger.fatal(sqle.getMessage());
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(pstmt, rs);
		}
		if(logger.isDebugEnabled()){ logger.debug(" SlipDAOImpl : selectRangedSlipList 종료 "); }
		return slipList;
	}
	@Override
	public ArrayList<SlipBean> selectDisApprovalSlipList() {	//미승인,승인 전표 조회
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" SlipDAOImpl : selectDisApprovalSlipList 시작 "); }
		 Connection conn = null;
	     PreparedStatement pstmt = null;
	     ResultSet rs = null;
	     ArrayList<SlipBean> disApprovalSlipList = new ArrayList<>();
	     try{
	    	 StringBuffer query = new StringBuffer();
	         query.append("SELECT * FROM SLIP S WHERE S.APPROVAL_STATE='승인완료'OR S.APPROVAL_STATE='승인취소' OR S.APPROVAL_DATE IS NULL ORDER BY APPROVAL_SEQ");
	         conn = dataSourceTransactionManager.getConnection();
	         pstmt=conn.prepareStatement(query.toString());
	         rs = pstmt.executeQuery();
	         while(rs.next()) {
	        	 SlipBean slipBean = new SlipBean();
	                slipBean.setSlipNo(rs.getString("SLIP_NO"));
					slipBean.setWriteDate(rs.getString("WRITE_DATE"));
					slipBean.setSlipSeq(rs.getInt("SLIP_SEQ"));
					slipBean.setRequestCode(rs.getString("REQUEST_CODE"));
					slipBean.setRequestName(rs.getString("REQUEST_NAME"));
					slipBean.setSlipType(rs.getString("SLIP_TYPE"));
					slipBean.setApprovalState(rs.getString("APPROVAL_STATE"));
					slipBean.setApprovalSeq(rs.getInt("APPROVAL_SEQ"));
					slipBean.setBalanceDiff(rs.getString("BALANCE_DIFF"));
					slipBean.setWriteEmpCode(rs.getString("WRITE_EMP_CODE"));
					slipBean.setApprovalEmpCode(rs.getString("APPROVAL_EMP_CODE"));
					slipBean.setApprovalDate(rs.getString("APPROVAL_DATE"));
					disApprovalSlipList.add(slipBean);
					System.out.println("		@ 승인/미승인 전표 조회");
	         }
	     }catch(Exception sqle){
	    	 logger.fatal(sqle.getMessage());
	    	 throw new DataAccessException(sqle.getMessage());
	     }finally{
	    	 dataSourceTransactionManager.close(pstmt, rs);
	     }
	     if(logger.isDebugEnabled()){ logger.debug(" SlipDAOImpl : selectDisApprovalSlipList 종료 "); }
		return disApprovalSlipList;
	}

}
