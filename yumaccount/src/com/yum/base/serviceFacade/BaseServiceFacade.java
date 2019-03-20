package com.yum.base.serviceFacade;

import java.util.ArrayList;
import java.util.HashMap;

import com.yum.base.exception.DeptCodeNotFoundException;
import com.yum.base.exception.IdNotFoundException;
import com.yum.base.exception.PwMissmatchException;
import com.yum.base.to.AddressBean;
import com.yum.base.to.CodeBean;
import com.yum.base.to.DetailCodeBean;
import com.yum.base.to.MenuBean;

public interface BaseServiceFacade {

	public HashMap<String, Object> accessToAuthority(String empCode, String empPassword,String deptCode) throws IdNotFoundException, PwMissmatchException, DeptCodeNotFoundException;

	public ArrayList<MenuBean> findUserMenuList(String empCode);

	public ArrayList<DetailCodeBean> findDetailCodeList(String code);

	public ArrayList<CodeBean> findCodeList();
	
	
	
	public ArrayList<AddressBean> findRoadList(String sido, String sigunguname, String roadname);

	public ArrayList<AddressBean> findSigunguList(String parameter);

	public ArrayList<AddressBean> findSidoList();

	public ArrayList<AddressBean> findPostList(String dong);

	public void batchCodeProcess(ArrayList<CodeBean> codeList);

	
	

}
