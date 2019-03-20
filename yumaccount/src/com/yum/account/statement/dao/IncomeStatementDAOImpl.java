package com.yum.account.statement.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yum.account.statement.to.IncomeStatementBean;
import com.yum.base.to.IncomeStatementResultBean;
import com.yum.common.db.DataSourceTransactionManager;
import com.yum.common.exception.DataAccessException;

import oracle.jdbc.internal.OracleTypes;



public class IncomeStatementDAOImpl implements IncomeStatementDAO{
	protected final Log logger = LogFactory.getLog(this.getClass());
	private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();
	
	private static IncomeStatementDAO instacne;
	private IncomeStatementDAOImpl(){}
	public static IncomeStatementDAO getInstance() {
		// TODO Auto-generated method stub
		if(instacne == null){
			instacne = new IncomeStatementDAOImpl();
			System.out.println("		@ IncomeStatementDAOImpl에 접근");
		}
		return instacne;
		
	}
	@Override
	public HashMap<String, Object> findincomeStatementList(String fromDate, String toDate) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" IncomeStatementDAOImpl : findincomeStatementList 시작 "); }
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		HashMap<String, Object> resultList = new HashMap<String, Object>(); 
		ArrayList<IncomeStatementBean> incomeStatementList = new ArrayList<>();
		System.out.println("		@ "+fromDate+" 부터 ");
		System.out.println("		@ "+toDate+" 까지 ");
		try {
			conn = dataSourceTransactionManager.getConnection();
			cstmt = conn.prepareCall("{call STATEMENT.INCOME(?,?,?,?,?)}");
			System.out.println("		@ 프로시저 호출");
			cstmt.setString(1, fromDate); 
			cstmt.setString(2, toDate);
			cstmt.registerOutParameter(3, OracleTypes.CURSOR);   /*프로시저에서 result였던것! 즉, 재무상태표 결과 */
			cstmt.registerOutParameter(4, OracleTypes.NUMBER);  /*에러코드*/
			cstmt.registerOutParameter(5, OracleTypes.VARCHAR);  /*에러메세지*/
			cstmt.execute();
			rs = (ResultSet) cstmt.getObject(3);    /*커서 매개변수에 대해서만 CallableStatement.getObject() 또는 CallableStatement.getString()을 호출할 수 있음 */

			while (rs.next()) {
				IncomeStatementBean incomeStatementBean = new IncomeStatementBean();
				incomeStatementBean.setAccountCode(rs.getString("ACCOUNT_CODE"));
				incomeStatementBean.setAccountName(rs.getString("ACCOUNT_NAME"));
				incomeStatementBean.setTotalCre(rs.getString("TOTAL_CRE"));
				incomeStatementBean.setTotalDeb(rs.getString("TOTAL_DEB")); 
				incomeStatementBean.setLevel(rs.getString("lv"));	
				incomeStatementBean.setLeafs(rs.getString("LEAFS"));	
				incomeStatementBean.setRootCode(rs.getString("SUPER_ACCOUNT_CODE"));	
				incomeStatementList.add(incomeStatementBean);
			}
			
			System.out.println("		@ 에러코드: " + cstmt.getInt(4));
			System.out.println("		@ 에러메시지: " + cstmt.getString(5));

			IncomeStatementResultBean resultBean = new IncomeStatementResultBean(); 
			resultBean.setErrorCode(cstmt.getInt(4)); 
			resultBean.setErrorMsg(cstmt.getString(5));
			resultList.put("incomeStatementList", incomeStatementList);  
			resultList.put("result", resultBean);
			
			
		} catch (Exception sqle) {
			logger.fatal(sqle.getMessage());
			throw new DataAccessException(sqle.getMessage());
		} finally {
			dataSourceTransactionManager.close(cstmt, rs);
		}
		if(logger.isDebugEnabled()){ logger.debug(" IncomeStatementDAOImpl : findincomeStatementList 종료 "); }
		return resultList; 
	}

}
