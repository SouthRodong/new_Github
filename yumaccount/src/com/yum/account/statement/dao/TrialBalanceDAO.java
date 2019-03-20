package com.yum.account.statement.dao;

import java.util.HashMap;

public interface TrialBalanceDAO {

	
	public HashMap<String, Object> findTrialBalanceList(String fromDate, String toDate);

}
