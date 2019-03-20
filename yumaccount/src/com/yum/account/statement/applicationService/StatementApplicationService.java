package com.yum.account.statement.applicationService;

import java.util.HashMap;

public interface StatementApplicationService {

	HashMap<String, Object> findTrialBalanceList(String fromDate, String toDate);
    HashMap<String,Object> findincomeStatementList(String fromDate, String toDate);
}
