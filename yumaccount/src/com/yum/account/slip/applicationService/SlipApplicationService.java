package com.yum.account.slip.applicationService;

import java.util.ArrayList;

import com.yum.account.slip.to.SlipBean;

public interface SlipApplicationService {

	public ArrayList<SlipBean> findSlipDataList(String slipDate);

	public void batchListProcess(ArrayList<SlipBean> slipList);

	public ArrayList<SlipBean> findRangedSlipList(String fromDate, String toDate);

	public ArrayList<SlipBean> findDisApprovalSlipList();

}
