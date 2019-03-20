package com.yum.account.slip.dao;

import com.yum.account.slip.to.JournalDetailBean;

public interface JournalDetailDAO {

	public JournalDetailBean selectJournalDetailList(String slipNo, String journalCode);

	public void deleteJournalDetail(JournalDetailBean journalDetailBean);

	public void updateJournalDetail(JournalDetailBean journalDetailBean);

	public void insertJournalDetail(JournalDetailBean journalDetailBean);

}
