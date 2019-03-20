package com.yum.common.servlet.controller; 


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yum.common.servlet.controller.AbstractController;
import com.yum.common.servlet.ModelAndView;

public class MultiActionController extends AbstractController{
	@SuppressWarnings("deprecation")
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		  System.out.println("		@ MultiActionController접근");
	      String methodName = request.getParameter("method"); 
	      System.out.println("		@ 메소드명: "+methodName);
	      
	      
	      Class<?>[] parameters = new Class<?>[]{HttpServletRequest.class,HttpServletResponse.class};
	      Class<?> cl = this.getClass();
	      //System.out.println("");
	      try{        

	         ModelAndView modelAndView=(ModelAndView)cl.getMethod(methodName, parameters).invoke(cl.newInstance(),request,response);
       
	         return modelAndView;
	      } catch (Exception e) {
	         e.printStackTrace();
	      }

	      return null;
	   }

}
