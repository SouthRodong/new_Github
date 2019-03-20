package com.yum.base.applicationService;

import java.util.ArrayList;
import java.util.HashMap;

import com.yum.base.exception.DeptCodeNotFoundException;
import com.yum.base.exception.IdNotFoundException;
import com.yum.base.exception.PwMissmatchException;
import com.yum.base.to.AddressBean;
import com.yum.base.to.MenuBean;
import com.yum.common.exception.DataAccessException;

public interface BaseApplicationService {

	public HashMap<String, Object> accessToAuthority(String empCode, String empPassword,String deptCode) throws IdNotFoundException,DeptCodeNotFoundException,PwMissmatchException, DataAccessException;

	public ArrayList<MenuBean> findMenuCodeList(String empCode);

	
	
	public ArrayList<AddressBean> findRoadList(String sido, String sigunguname, String roadname);

	public ArrayList<AddressBean> findSigunguList(String parameter);

	public ArrayList<AddressBean> findSidoList();

	public ArrayList<AddressBean> findPostList(String dong);

	

}
