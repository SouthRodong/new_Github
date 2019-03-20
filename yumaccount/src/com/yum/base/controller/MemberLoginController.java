package com.yum.base.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yum.base.exception.DeptCodeNotFoundException;
import com.yum.base.exception.IdNotFoundException;
import com.yum.base.exception.PwMissmatchException;
import com.yum.base.serviceFacade.BaseServiceFacade;
import com.yum.base.serviceFacade.BaseServiceFacadeImpl;
import com.yum.base.to.MenuBean;
import com.yum.common.servlet.ModelAndView;
import com.yum.common.servlet.controller.MultiActionController;
import com.yum.hr.to.EmployeeBean;

public class MemberLoginController extends MultiActionController{

	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@SuppressWarnings("unchecked")
	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response){
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" MemberLoginController : handleRequestInternal 시작 "); 
		
		}
		
		
		String viewName = null;
		HashMap<String, Object> model = new HashMap<String, Object>();
		try{
			BaseServiceFacade baseServiceFacade = BaseServiceFacadeImpl.getInstance();
			System.out.println("		@ BaseServiceFacade의 객체 주소를 가져옴");
			HttpSession session = request.getSession();
			System.out.println("		@ session 생성");
			String empCode = request.getParameter("empCode").toUpperCase();
			System.out.println("		@ 로그인 폼에서 파라메터로 받아온 empCode: "+empCode);
			String empPassword = request.getParameter("empPassword");
			System.out.println("		@ 로그인 폼에서 파라메터로 받아온 empPassword: "+empPassword);
			String deptCode = request.getParameter("deptCode").toUpperCase();
			System.out.println("		@ 로그인 폼에서 파라메터로 받아온 deptCode: "+deptCode);
			
			
			HashMap<String,Object> result = baseServiceFacade.accessToAuthority(empCode, empPassword ,deptCode);
			System.out.println("		@ BaseServiceFacade에서 접근 권한을 얻어옴");
			if (result != null) {			
				session.setAttribute("empCode", ((EmployeeBean)result.get("employeeBean")).getEmpCode());			
				session.setAttribute("empName", ((EmployeeBean)result.get("employeeBean")).getEmpName());
			
				session.setAttribute("authority", ((ArrayList<MenuBean>)result.get("menuList")).get(0).getAuthorityName()); 
				session.setAttribute("positionCode", ((EmployeeBean)result.get("employeeBean")).getPositionCode()); 
				session.setAttribute("masterMenuList", result.get("masterMenuList"));
				viewName = "redirect:hello.html"; 
			}
			System.out.println("		@ 뷰네임: "+viewName);
		} catch (IdNotFoundException e) {
			model.put("errorCode", -1);
			model.put("errorMsg", /*e.getMessage()*/ "존재하지 않는 계정입니다");
			viewName = "loginform";
		}catch(DeptCodeNotFoundException e){
			model.put("errorCode", -2);
			model.put("errorMsg", /*e.getMessage()*/ "부서코드가 맞지 않습니다");
			viewName = "loginform";
		} catch (PwMissmatchException e) {		
						
			model.put("errorCode", -3);
			model.put("errorMsg", /*e.getMessage()*/ "비밀번호가 맞지 않습니다");
			viewName = "loginform";
		} catch (Exception e) {
			model.put("errorCode", -4);
			model.put("errorMsg", /*e.getMessage()*/ "오류발생");
			viewName = "loginform"; 
		}
		if(logger.isDebugEnabled()){ logger.debug(" MemberLoginController : handleRequestInternal 종료 "); }
		System.out.println("		@ 로그인 되었습니다");
		ModelAndView modelAndView = new ModelAndView(viewName, model);
		return modelAndView;
	}

}
