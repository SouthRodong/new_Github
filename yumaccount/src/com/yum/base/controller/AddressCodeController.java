package com.yum.base.controller;

import com.yum.base.serviceFacade.BaseServiceFacade;
import com.yum.base.serviceFacade.BaseServiceFacadeImpl;
import com.yum.base.to.AddressBean;
import com.yum.common.servlet.controller.MultiActionController;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AddressCodeController extends MultiActionController{
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	BaseServiceFacade baseServiceFacade = BaseServiceFacadeImpl.getInstance();
	
	public void searchRoadname(HttpServletRequest request, HttpServletResponse response) {
		if(logger.isDebugEnabled()){ logger.debug(" AddressCodeController : searchRoadname 시작 "); }
		HashMap<String, Object> postRoadMap = new HashMap<String, Object>();
		ArrayList<AddressBean> postRoadList = new ArrayList<>();

		try {

			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/json; charset=UTF-8");
			//System.out.println(request.getParameter("sido"));
			String sido = request.getParameter("sido");
			String sigunguname = request.getParameter("sigunguname");
			String roadname = request.getParameter("roadname");

			System.out.println("		@ 시/도: "+sido + ", 시/군/구: " + sigunguname + ", 도로명: " + roadname);
			postRoadList = baseServiceFacade.findRoadList(sido, sigunguname, roadname);
			if (postRoadList.size() == 0)
				postRoadMap.put("datanull", "데이터가 존재 하지 않습니다.");
			postRoadMap.put("postRoadList", postRoadList);
			postRoadMap.put("errorCode", 0);
			postRoadMap.put("errorMsg", "Success!");
			JSONObject json = JSONObject.fromObject(postRoadMap);
			//System.out.println(json);
			response.getWriter().println(json);
			
		} catch (Exception e) {
			logger.fatal(e.getMessage());
			postRoadMap.put("errorCode", -1);
			postRoadMap.put("errorMsg", "Show list failed!");
			JSONObject json = JSONObject.fromObject(postRoadMap);
			try {
				response.getWriter().println(json);
			} catch (IOException e1) {
				logger.fatal(e.getMessage());
				e1.printStackTrace();
			}
		}
		if(logger.isDebugEnabled()){ logger.debug(" AddressCodeController : searchRoadname 종료 "); }
	}

	public void searchSigungu(HttpServletRequest request, HttpServletResponse response) {

		if(logger.isDebugEnabled()){ logger.debug(" AddressCodeController : searchSigungu 시작 "); }
		HashMap<String, Object> postSigunguMap = new HashMap<String, Object>();
		ArrayList<AddressBean> postSigunguList = new ArrayList<>();
		try {
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/json; charset=UTF-8");
			postSigunguList = baseServiceFacade.findSigunguList(request.getParameter("sido"));
			postSigunguMap.put("postSigunguList", postSigunguList);
			postSigunguMap.put("errorCode", 0);
			postSigunguMap.put("errorMsg", "Success!");
			JSONObject json = JSONObject.fromObject(postSigunguMap);
			response.getWriter().println(json);
			
		} catch (Exception e) {
			logger.fatal(e.getMessage());
			postSigunguMap.put("errorCode", -1);
			postSigunguMap.put("errorMsg", "Show list failed!");
			JSONObject json = JSONObject.fromObject(postSigunguMap);
			try {
				response.getWriter().println(json);
			} catch (IOException e1) {
				logger.fatal(e.getMessage());
				e1.printStackTrace();
			}
		}
		if(logger.isDebugEnabled()){ logger.debug(" AddressCodeController : searchSigungu 종료 "); }
	}

	public void searchSido(HttpServletRequest request, HttpServletResponse response) {

		if(logger.isDebugEnabled()){ logger.debug(" AddressCodeController : searchSido 시작 "); }
		HashMap<String, Object> postSidoMap = new HashMap<String, Object>();
		ArrayList<AddressBean> postSidoList = new ArrayList<>();
		try {
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/json; charset=UTF-8");
			postSidoList = baseServiceFacade.findSidoList();
			postSidoMap.put("postSidoList", postSidoList);
			JSONObject json = JSONObject.fromObject(postSidoMap);
			postSidoMap.put("errorCode", 0);
			postSidoMap.put("errorMsg", "Success!");
			response.getWriter().println(json);
			
		} catch (Exception e) {
			logger.fatal(e.getMessage());
			postSidoMap.put("errorCode", -1);
			postSidoMap.put("errorMsg", "Show list failed!");
			JSONObject json = JSONObject.fromObject(postSidoMap);
			try {
				response.getWriter().println(json);
			} catch (IOException e1) {
				logger.fatal(e.getMessage());
				e1.printStackTrace();
			}
		}
		if(logger.isDebugEnabled()){ logger.debug(" AddressCodeController : searchSido 종료 "); }
	}

	public void searchJibun(HttpServletRequest request, HttpServletResponse response) {
		if(logger.isDebugEnabled()){ logger.debug(" AddressCodeController : searchJibun 시작 "); }
		HashMap<String, Object> postListMap = new HashMap<String, Object>();
		ArrayList<AddressBean> postList = new ArrayList<>();
		try {
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/json; charset=UTF-8");
			String dong = request.getParameter("dong");
			postList = baseServiceFacade.findPostList(dong);
			postListMap.put("postList", postList);
			postListMap.put("errorCode", 0);
			postListMap.put("errorMsg", "Success!");
			JSONObject json = JSONObject.fromObject(postListMap);
			response.getWriter().println(json);
			
		} catch (Exception e) {
			logger.fatal(e.getMessage());
			postListMap.put("errorCode", -1);
			postListMap.put("errorMsg", "Show list failed!");
			JSONObject json = JSONObject.fromObject(postListMap);
			try {
				response.getWriter().println(json);
			} catch (IOException e1) {
				logger.fatal(e.getMessage());
				e1.printStackTrace();
			}
		}
		if(logger.isDebugEnabled()){ logger.debug(" AddressCodeController : searchJibun 종료 "); }
	}
	/*
	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response){
		// TODO Auto-generated method stub
		return null;
	}	*/
}
