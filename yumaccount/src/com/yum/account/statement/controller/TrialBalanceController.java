package com.yum.account.statement.controller;

import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yum.account.statement.serviceFacade.StatementServiceFacade;
import com.yum.account.statement.serviceFacade.StatementServiceFacadeImpl;
import com.yum.base.to.TrialBalanceResultBean;
import com.yum.common.exception.DataAccessException;
import com.yum.common.servlet.ModelAndView;
import com.yum.common.servlet.controller.MultiActionController;

import net.sf.json.JSONObject;

public class TrialBalanceController extends MultiActionController{

	protected final Log logger = LogFactory.getLog(this.getClass());
	private StatementServiceFacade statementServiceFacade = StatementServiceFacadeImpl.getInstance();
	@Override	
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" TrialBalanceController : handleRequestInternal 시작 "); }

		response.setContentType("application/json; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		JSONObject json = new JSONObject();
		PrintWriter out = null;
		System.out.println("		@ TrialBalanceController에 접근");
		try {
			String fromDate = request.getParameter("from");
			String toDate = request.getParameter("to");
			out = response.getWriter();
			System.out.println("		@ 조회일자: "+fromDate+"부터 "+toDate+" 까지");
			HashMap<String, Object> trialBalanceList = statementServiceFacade.findTrialBalanceList(fromDate, toDate);

			TrialBalanceResultBean error = (TrialBalanceResultBean) trialBalanceList.get("result");
			if (error.getErrorCode() != 0) {
				json.put("errorCode", error.getErrorCode());
				json.put("errorMsg", error.getErrorMsg());
			} else {
				json.put("trialBalanceList", trialBalanceList.get("trialBalanceList"));
				json.put("errorCode", error.getErrorCode());
				json.put("errorMsg", error.getErrorMsg());
			}
		} catch (DataAccessException e) {
			logger.fatal(e.getMessage());
			json.put("errorCode", -2);
			json.put("errorMsg", e.getMessage());
			e.printStackTrace();
		} catch (Exception error) {
			logger.fatal(error.getMessage());
			json.put("errorCode", -1);
			json.put("errorMsg", error.getMessage());
			error.printStackTrace();
		}
		out.println(json);
		out.close();
		if(logger.isDebugEnabled()){ logger.debug(" TrialBalanceController : handleRequestInternal 종료 "); }
		return null;
	}
	
}
