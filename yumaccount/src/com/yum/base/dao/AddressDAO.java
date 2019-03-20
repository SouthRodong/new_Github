package com.yum.base.dao;

import java.util.ArrayList;

import com.yum.base.to.AddressBean;

public interface AddressDAO {
	/*public ArrayList<AddressBean> searchAddressList(String dong);*/

	public ArrayList<AddressBean> selectRoadList(String sido, String sigunguname, String roadname);

	public ArrayList<AddressBean> selectSigunguList(String parameter);

	public ArrayList<AddressBean> selectSidoList();

	public ArrayList<AddressBean> selectPostList(String dong);
}
