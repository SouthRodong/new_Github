package com.yum.base.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yum.base.serviceFacade.BaseServiceFacade;
import com.yum.base.serviceFacade.BaseServiceFacadeImpl;
import com.yum.base.to.CodeBean;
import com.yum.base.to.DetailCodeBean;
import com.yum.common.exception.DataAccessException;
import com.yum.common.servlet.ModelAndView;
import com.yum.common.servlet.controller.MultiActionController;

import net.sf.json.JSONObject;


public class CodeListController extends MultiActionController {
	private BaseServiceFacade baseServiceFacade = BaseServiceFacadeImpl.getInstance();

	protected final Log logger = LogFactory.getLog(this.getClass());
	public ModelAndView findDetailCodeList(HttpServletRequest request, HttpServletResponse response){
		if(logger.isDebugEnabled()){ logger.debug(" CodeListController : findDetailCodeList 시작 "); }
		JSONObject json = new JSONObject();
		PrintWriter out = null;
		
		try {
			out = response.getWriter();
			String code = request.getParameter("code"); 
					
			ArrayList<DetailCodeBean> detailCodeList = baseServiceFacade.findDetailCodeList(code);
			json.put("detailCodeList", detailCodeList); 
			json.put("errorCode", 1);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
		out.print(json);
		out.close();
		if(logger.isDebugEnabled()){ logger.debug(" CodeListController : findDetailCodeList 종료 "); }
		return null;
		
	}
	
	public ModelAndView findCodeList(HttpServletRequest request, HttpServletResponse response){
		if(logger.isDebugEnabled()){ logger.debug(" CodeListController : findCodeList 시작 "); }
			JSONObject json = new JSONObject();
			PrintWriter out = null;
			
				try {
					out = response.getWriter(); 
					ArrayList<CodeBean> codeList = baseServiceFacade.findCodeList();
					json.put("emptyCode", new CodeBean()); 
		            json.put("emptyDetailCode", new DetailCodeBean()); 
		            json.put("codeList", codeList); 
		            json.put("errorCode", 1);
		            json.put("errorMsg", "코드를 가져 왔습니다");
				
				
				}catch(IOException e) {
					// TODO Auto-generated catch block
					logger.fatal(e.getMessage());
					json.put("errorCode", -1);
					json.put("errorMsg", "출력오류");
					e.printStackTrace();
				}catch (DataAccessException e) {
					logger.fatal(e.getMessage());
		            json.put("errorCode", -2);
		            json.put("errorMsg", /*e.getMessage()*/ "DB오류");
		            e.printStackTrace();
		         }
		      out.print(json);
		      out.close();
		      if(logger.isDebugEnabled()){ logger.debug(" CodeListController : findCodeList 종료 "); }
			return null;		
	}
	
	public ModelAndView batchCodeProcess(HttpServletRequest request, HttpServletResponse response){
		if(logger.isDebugEnabled()){ logger.debug(" CodeListController : batchCodeProcess 시작 "); }
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        try{
            out = response.getWriter();
            String list = request.getParameter("batchList");
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<CodeBean> codeList = mapper.readValue(list, new TypeReference<ArrayList<CodeBean>>(){});
            baseServiceFacade.batchCodeProcess(codeList);
          
            json.put("errorCode", 1);
            json.put("errorMsg", "데이터를 가져 왔습니다");
        }catch (IOException e){
        	logger.fatal(e.getMessage());
            json.put("errorCode", -1);
            json.put("errorMsg", "출력오류");
            e.printStackTrace();
        }catch (DataAccessException e) {
        	logger.fatal(e.getMessage());
            json.put("errorCode", -2);
            json.put("errorMsg", e.getMessage());
            e.printStackTrace();
         }
        out.println(json);
        out.close(); 
        if(logger.isDebugEnabled()){ logger.debug(" CodeListController : batchCodeProcess 종료 "); }
        return null;
    }
	
	

}
