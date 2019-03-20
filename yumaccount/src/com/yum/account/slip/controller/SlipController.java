package com.yum.account.slip.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yum.account.slip.serviceFacade.SlipServiceFacade;
import com.yum.account.slip.serviceFacade.SlipServiceFacadeImpl;
import com.yum.account.slip.to.JournalBean;
import com.yum.account.slip.to.JournalDetailBean;
import com.yum.account.slip.to.SlipBean;
import com.yum.common.exception.DataAccessException;
import com.yum.common.servlet.ModelAndView;
import com.yum.common.servlet.controller.MultiActionController;

import net.sf.json.JSONObject;

public class SlipController extends MultiActionController{
	protected final Log logger = LogFactory.getLog(this.getClass());
	private SlipServiceFacade slipServiceFacade = SlipServiceFacadeImpl.getInstance();
	
	public ModelAndView findSlipDataList(HttpServletRequest request, HttpServletResponse response) throws IOException{
		if(logger.isDebugEnabled()){ logger.debug(" SlipController : findSlipDataList 시작 "); }
	      ArrayList<SlipBean> slipList=null;
	      JSONObject json=new JSONObject();
	      PrintWriter out=null;
	      try{
	         out = response.getWriter();
	         String slipDate = request.getParameter("slipDate");
	         slipList=slipServiceFacade.findSlipDataList(slipDate);
	         json.put("slipList", slipList);  
	         json.put("emptySlip", new SlipBean());	 
	         json.put("emptyJournal", new JournalBean()); 
	         json.put("emptyJournalDetail", new JournalDetailBean()); 
	         json.put("errorCode", 1);
	         json.put("errorMsg","데이터 조회 성공");
	      }catch(IOException e){
	    	  logger.fatal(e.getMessage());
	         json.put("errorCode", -1);
	         json.put("errorMsg","데이터 조회 실패");
	      
	      }catch (DataAccessException e) {
	    	  logger.fatal(e.getMessage());
	          json.put("errorCode", -2);
	          json.put("errorMsg", e.getMessage());
	          e.printStackTrace();
	      }
	      out.println(json);
	      out.close();
	      if(logger.isDebugEnabled()){ logger.debug(" SlipController : findSlipDataList 종료 "); }
	       return null;
	   }
	
    
     public ModelAndView findRangedSlipList(HttpServletRequest request, HttpServletResponse response){
    	 if(logger.isDebugEnabled()){ logger.debug(" SlipController : findRangedSlipList 시작 "); }
        JSONObject json=new JSONObject();
        PrintWriter out=null;
        try{
           out=response.getWriter();
           String fromDate=request.getParameter("from");
           String toDate=request.getParameter("to");
           List<SlipBean> slipList=slipServiceFacade.findRangedSlipList(fromDate,toDate);
           json.put("slipList",slipList);
           json.put("errorCode", 1);
           json.put("errorMsg", "데이터 조회 성공");
             }catch(IOException ie){
            	 logger.fatal(ie.getMessage());
                json.put("errorCode", -1);
                 json.put("errorMsg", "오류발생");
                 ie.printStackTrace();
             }catch (DataAccessException e) {
            	 logger.fatal(e.getMessage());
                 json.put("errorCode", -2);
                 json.put("errorMsg", e.getMessage());
                 e.printStackTrace();
              }
             out.println(json);
             out.close();    
             if(logger.isDebugEnabled()){ logger.debug(" SlipController : findRangedSlipList 종료 "); }
             return null;
         }
}
