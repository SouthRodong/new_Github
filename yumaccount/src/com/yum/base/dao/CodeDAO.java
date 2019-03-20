package com.yum.base.dao;

import java.util.ArrayList;

import com.yum.base.to.CodeBean;

public interface CodeDAO {

	public ArrayList<CodeBean> selectCodeList();

	public void insertCode(CodeBean codeBean);

	public void updateCode(CodeBean codeBean);

	public void deleteCode(String divisionCode);

}
