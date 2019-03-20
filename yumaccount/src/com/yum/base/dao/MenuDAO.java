package com.yum.base.dao;

import java.util.ArrayList;

import com.yum.base.to.MenuBean;

public interface MenuDAO {
	ArrayList<MenuBean> selectMenuList(String empCode);

	ArrayList<MenuBean> selectAllMenuList();
	
}
