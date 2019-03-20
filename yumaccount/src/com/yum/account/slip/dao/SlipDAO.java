package com.yum.account.slip.dao;

import java.util.ArrayList;

import com.yum.account.slip.to.SlipBean;

public interface SlipDAO {

	public ArrayList<SlipBean> selectSlipDataList(String slipDate);

	public void deleteSlip(SlipBean slipBean);

	public void updateSlip(SlipBean slipBean);

	public void insertSlip(SlipBean slipBean);

	public ArrayList<SlipBean> selectRangedSlipList(String fromDate, String toDate);

	public ArrayList<SlipBean> selectDisApprovalSlipList();

}
