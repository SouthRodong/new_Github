package com.yum.account.statement.dao;

import java.util.HashMap;

public interface IncomeStatementDAO {

	public HashMap<String, Object> findincomeStatementList(String fromDate, String toDate);
	
}
