package com.yum.account.base.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yum.account.base.serviceFacade.AccountServiceFacade;
import com.yum.account.base.serviceFacade.AccountServiceFacadeImpl;
import com.yum.account.base.to.AccountControlBean;
import com.yum.account.base.to.AccountDetailBean;
import com.yum.common.exception.DataAccessException;
import com.yum.common.servlet.ModelAndView;
import com.yum.common.servlet.controller.MultiActionController;

import net.sf.json.JSONObject;

public class AccountDetailController extends MultiActionController{
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	public AccountServiceFacade accountServiceFacade=AccountServiceFacadeImpl.getInstance();
	
	
	public ModelAndView findAccountControlList(HttpServletRequest request, HttpServletResponse response){
		if(logger.isDebugEnabled()){ logger.debug(" AccountDetailController : findAccountControlList 시작 "); }
		JSONObject json=new JSONObject();
        PrintWriter out=null;
        try{
            out=response.getWriter();
            String accountCode=request.getParameter("accountCode");
            System.out.println("		@ 계정코드: "+accountCode);
            ArrayList<AccountControlBean> accountControlList=accountServiceFacade.findAccountControlList(accountCode);
            json.put("accountControlList", accountControlList);
            json.put("errorCode", 1);
            json.put("errorMsg", "findAccountControlList에서 오류 발생");
           // System.out.println(json);
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
        if(logger.isDebugEnabled()){ logger.debug(" AccountDetailController : findAccountControlList 종료 "); }
        return null;
    }
	
	public ModelAndView findAccountDetailList(HttpServletRequest request, HttpServletResponse response){
		if(logger.isDebugEnabled()){ logger.debug(" AccountDetailController : findAccountDetailList 시작 "); }
		JSONObject json=new JSONObject();
        PrintWriter out=null;
        
        try{
            out=response.getWriter();
            ArrayList<AccountDetailBean> controlItemList = accountServiceFacade.findAccountDetailList();
            json.put("controlItemList", controlItemList);
            json.put("errorCode", 1);
            json.put("errorMsg","데이터를 가져 왔습니다");
           // System.out.println(json);
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
        if(logger.isDebugEnabled()){ logger.debug(" AccountDetailController : findAccountDetailList 종료 "); }
        return null;
    }
	
		
	public ModelAndView batchAccountControlListProcess(HttpServletRequest request, HttpServletResponse response){
		if(logger.isDebugEnabled()){ logger.debug(" AccountDetailController : batchAccountControlListProcess 시작 "); }
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        try{
            out = response.getWriter();

            String jsonList = request.getParameter("controlList");
            System.out.println("		@ 컨트롤리스트: "+jsonList);
            ObjectMapper mapper = new ObjectMapper();
           
            ArrayList<AccountControlBean> accountControlList = mapper.readValue(jsonList, new TypeReference<ArrayList<AccountControlBean>>(){});

            accountServiceFacade.batchAccountControlListProcess(accountControlList);

            json.put("errorCode", 1);
            json.put("errorMsg", "데이터를 가져 왔습니다");
           

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
        if(logger.isDebugEnabled()){ logger.debug(" AccountDetailController : batchAccountControlListProcess 종료 "); }

        return null;
    }    

}
