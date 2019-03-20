package com.yum.hr.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yum.common.exception.DataAccessException;
import com.yum.common.servlet.ModelAndView;
import com.yum.common.servlet.controller.MultiActionController;
import com.yum.hr.serviceFacade.HRServiceFacade;
import com.yum.hr.serviceFacade.HRServiceFacadeImpl;
import com.yum.hr.to.EmployeeBean;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class EmployeeModifyController extends MultiActionController {
	
	protected final Log logger = LogFactory.getLog(this.getClass());

	private static HRServiceFacade hrServiceFacade =  HRServiceFacadeImpl.getInstance();
	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response){
		if(logger.isDebugEnabled()){ logger.debug(" EmployeeModifyController : handleRequestInternal 시작 "); }
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		PrintWriter out = null;
		ArrayList<EmployeeBean> empList = new ArrayList<EmployeeBean>();
		try{
		
		JSONObject jsonObject = JSONObject.fromObject(request.getParameter("batchList"));
		JSONArray jsonEmployeeArray = jsonObject.getJSONArray("batchList");
	
		
		for (Object obj : jsonEmployeeArray) {
			
			EmployeeBean employeeBean = (EmployeeBean) JSONObject.toBean((JSONObject) obj, EmployeeBean.class);
			empList.add(employeeBean);
		}
		out = response.getWriter(); 
		hrServiceFacade.batchEmployee(empList);
		
		json.put("errorCode",1);
		json.put("errorMsg","성공");
		}catch( IOException e){
			json.put("errorCode", -1);
			json.put("errorMsg", "실패");
			logger.fatal(e.getMessage());
		
		}catch(DataAccessException e){
			json.put("errorCode", -2);
			json.put("errorMsg", "DB오류"/*e.getMessage()*/);
			logger.fatal(e.getMessage());
			
			
		}
		out.println(json);
		out.close();

		if(logger.isDebugEnabled()){ logger.debug(" EmployeeModifyController : handleRequestInternal 종료 "); }
		return null;
	}
	
	

}
