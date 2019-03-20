package com.yum.base.applicationService;

import java.util.ArrayList;

import com.yum.base.to.CodeBean;
import com.yum.base.to.DetailCodeBean;

public interface CodeListApplicationService {

	public ArrayList<DetailCodeBean> findDetailCodeList(String code);

	public ArrayList<CodeBean> findCodeList();

	public void batchCodeProcess(ArrayList<CodeBean> codeList);

	

}
