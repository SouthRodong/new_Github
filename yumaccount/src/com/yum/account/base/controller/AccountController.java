package com.yum.account.base.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yum.account.base.serviceFacade.AccountServiceFacade;
import com.yum.account.base.serviceFacade.AccountServiceFacadeImpl;
import com.yum.account.base.to.AccountBean;
import com.yum.account.base.to.AccountControlBean;
import com.yum.common.exception.DataAccessException;
import com.yum.common.servlet.ModelAndView;
import com.yum.common.servlet.controller.MultiActionController;

import net.sf.json.JSONObject;

public class AccountController extends MultiActionController{
	protected final Log logger = LogFactory.getLog(this.getClass());
	public AccountServiceFacade accountServiceFacade=AccountServiceFacadeImpl.getInstance();
    
	 public ModelAndView findAccountList(HttpServletRequest request, HttpServletResponse response){
		 if(logger.isDebugEnabled()){ logger.debug(" AccountController : findAccountList 시작 "); }
	        JSONObject json = new JSONObject();
	        PrintWriter out = null;
	        try{
	            out = response.getWriter();
	            ArrayList<AccountBean> accountList=accountServiceFacade.findAccountList();
	            json.put("accountList", accountList); 
	            json.put("emptyAccountControl", new AccountControlBean()); 
	            json.put("errorCode", 1);
	            json.put("errorMsg","요류발생");
	        }catch(IOException e){
	        	logger.fatal(e.getMessage());
	            json.put("errorCode", -1);
	            json.put("errorMsg", e.getMessage());
	            e.printStackTrace();
	        }catch (DataAccessException e) {	
	        	logger.fatal(e.getMessage());
	            json.put("errorCode", -2);
	            json.put("errorMsg", e.getMessage());
	            e.printStackTrace();
	         }
	        out.println(json);
	        out.close();
	        if(logger.isDebugEnabled()){ logger.debug(" AccountController : findAccountList 종료 "); }
	        return null;
	    }    

}
