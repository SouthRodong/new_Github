package com.yum.base.dao;

import java.util.ArrayList;

import com.yum.base.to.DetailCodeBean;

public interface DetailCodeDAO {

	ArrayList<DetailCodeBean> selectDetailCodeList(String code);

	void insertDetailCode(DetailCodeBean codeDetailBean);

	void updateDetailCode(DetailCodeBean codeDetailBean);

	void deleteDetailCode(String codeNo);

}
