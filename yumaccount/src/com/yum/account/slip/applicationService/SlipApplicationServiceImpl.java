package com.yum.account.slip.applicationService;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yum.account.slip.applicationService.SlipApplicationService;
import com.yum.account.slip.applicationService.SlipApplicationServiceImpl;
import com.yum.account.slip.dao.JournalDAO;
import com.yum.account.slip.dao.JournalDAOImpl;
import com.yum.account.slip.dao.JournalDetailDAO;
import com.yum.account.slip.dao.JournalDetailDAOImpl;
import com.yum.account.slip.dao.SlipDAO;
import com.yum.account.slip.dao.SlipDAOImpl;
import com.yum.account.slip.to.JournalBean;
import com.yum.account.slip.to.JournalDetailBean;
import com.yum.account.slip.to.SlipBean;
import com.yum.common.exception.DataAccessException;
import com.yum.hr.dao.EmployeeDAO;
import com.yum.hr.dao.EmployeeDAOImpl;

public class SlipApplicationServiceImpl implements SlipApplicationService{
	protected final Log logger = LogFactory.getLog(this.getClass());
	private static SlipDAO slipDAO = SlipDAOImpl.getInstance();
	private static EmployeeDAO employeeDAO = EmployeeDAOImpl.getInstance();  /*DAO */
	private static JournalDAO journalDAO = JournalDAOImpl.getInstance();
	private static JournalDetailDAO journalDetailDAO = JournalDetailDAOImpl.getInstance();
	
	
	private static SlipApplicationService instance;
	private SlipApplicationServiceImpl(){}
	
	public static SlipApplicationService getInstance() {
		// TODO Auto-generated method stub
		if(instance == null){
			instance = new SlipApplicationServiceImpl();			
		}
		return instance;
	}
	

	@Override
	public ArrayList<SlipBean> findSlipDataList(String slipDate) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" SlipApplicationServiceImpl : findSlipDataList 시작 "); }
		ArrayList<SlipBean> slipList = null;
		try {
			slipList = slipDAO.selectSlipDataList(slipDate);  
			for (SlipBean slip : slipList) { 
				slip.setWriteEmpName(employeeDAO.selectEmployee(slip.getWriteEmpCode()).getEmpName()); 
				if (slip.getApprovalEmpCode() != null) {
					slip.setApprovalEmpName(employeeDAO.selectEmployee(slip.getApprovalEmpCode()).getEmpName());
				}
				ArrayList<JournalBean> journalList = journalDAO.selectJournalList(slip.getSlipNo());

				for (JournalBean journal : journalList) {
										
					JournalDetailBean journalDetail = journalDetailDAO.selectJournalDetailList(journal.getSlipNo(),journal.getJournalCode());
					journal.setJournalDetailBean(journalDetail);
				}
				slip.setJournalList(journalList);
			}
		} catch (DataAccessException e) {
			logger.fatal(e.getMessage());
			throw e;
		}
		if(logger.isDebugEnabled()){ logger.debug(" SlipApplicationServiceImpl : findSlipDataList 종료 "); }
		return slipList;
	}

	@Override
	public void batchListProcess(ArrayList<SlipBean> slipList) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" SlipApplicationServiceImpl : batchListProcess 시작 "); }
		String totalStatus=null;
		try {
			for (SlipBean slipBean : slipList) {
				String slipStatus = slipBean.getStatus();	 			
				if(slipBean.getJournalList().isEmpty()){ 
					
				totalStatus=slipStatus;
				
					switch (totalStatus) {
					case "insert":
						registerSlip(slipBean);
						break;
					case "update":
						modifySlip(slipBean);
						break;
					case "delete" : 
						removeSlip(slipBean);
						break;
					}
				}else{
					if(slipStatus.equals("insert")){
						registerSlip(slipBean);
					}
					
					ArrayList<JournalBean> journalList = slipBean.getJournalList(); 
					for (JournalBean journalBean : journalList) {
						JournalDetailBean journalDetailBean = journalBean.getJournalDetailBean(); 
						String journalStatus = journalBean.getStatus();
						totalStatus=slipStatus+journalStatus;
						switch (totalStatus) {
						case "insertinsert":			  			
							registerJournal(journalBean);
							registerJournalDetail(journalDetailBean);
							break;
						case "updateinsert": 			  
							modifySlip(slipBean);
							registerJournal(journalBean);
							registerJournalDetail(journalDetailBean);
							break;
						case "updateupdate": 				
							modifySlip(slipBean);
							modifyJournal(journalBean);
							modifyJournalDetail(journalDetailBean);
							break;
						case "updatenormal": 			  
							modifySlip(slipBean);
							modifyJournal(journalBean);
							modifyJournalDetail(journalDetailBean);
							break;
						case "updatedelete":			  
							modifySlip(slipBean);
							removeJournalDetail(journalDetailBean);
							removeJournal(journalBean);	
							break;	
						case "deletedelete": 			 	
							removeJournalDetail(journalDetailBean);
							removeJournal(journalBean);							
							break;
						}
						
					}
					if(slipStatus.equals("delete")){
						removeSlip(slipBean);
					}
				}
			}
		} catch (DataAccessException e) {
			logger.fatal(e.getMessage());
			throw e;
		}
		if(logger.isDebugEnabled()){ logger.debug(" SlipApplicationServiceImpl : batchListProcess 종료 "); }
	}



	private void removeSlip(SlipBean slipBean) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" SlipApplicationServiceImpl : removeSlip 시작 "); }
		try {
			slipDAO.deleteSlip(slipBean);
		} catch (DataAccessException e) {
			logger.fatal(e.getMessage());
			throw e;
		}
		if(logger.isDebugEnabled()){ logger.debug(" SlipApplicationServiceImpl : removeSlip 종료 "); }
	}

	private void modifySlip(SlipBean slipBean) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" SlipApplicationServiceImpl : modifySlip 시작 "); }
		try {
			slipDAO.updateSlip(slipBean);
		} catch (Exception e) {
			logger.fatal(e.getMessage());
			throw e;
		}
		if(logger.isDebugEnabled()){ logger.debug(" SlipApplicationServiceImpl : modifySlip 종료 "); }
	}

	private void registerSlip(SlipBean slipBean) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" SlipApplicationServiceImpl : registerSlip 시작 "); }
		try {
			slipDAO.insertSlip(slipBean);
		} catch (Exception e) {
			logger.fatal(e.getMessage());
			throw e;
		}
		if(logger.isDebugEnabled()){ logger.debug(" SlipApplicationServiceImpl : registerSlip 종료 "); }
	}
		
	private void registerJournal(JournalBean journalBean) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" SlipApplicationServiceImpl : registerJournal 시작 "); }
		try {
			journalDAO.insertJournal(journalBean);
		} catch (DataAccessException e) {
			logger.fatal(e.getMessage());
			throw e;
		}
		if(logger.isDebugEnabled()){ logger.debug(" SlipApplicationServiceImpl : registerJournal 종료 "); }
	}
	
	private void removeJournal(JournalBean journalBean) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" SlipApplicationServiceImpl : removeJournal 시작 "); }
		try {
			journalDAO.deleteJournal(journalBean);
		} catch (DataAccessException e) {		
			logger.fatal(e.getMessage());
			throw e;
		}
		if(logger.isDebugEnabled()){ logger.debug(" SlipApplicationServiceImpl : removeJournal 종료 "); }
	}
	
	private void modifyJournal(JournalBean journalBean) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" SlipApplicationServiceImpl : modifyJournal 시작 "); }
		try {
			journalDAO.updateJournal(journalBean);
		} catch (DataAccessException e) {
			logger.fatal(e.getMessage());
				throw e;
		}
		if(logger.isDebugEnabled()){ logger.debug(" SlipApplicationServiceImpl : modifyJournal 종료 "); }
	}

	private void removeJournalDetail(JournalDetailBean journalDetailBean) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" SlipApplicationServiceImpl : removeJournalDetail 시작 "); }
		try {
			journalDetailDAO.deleteJournalDetail(journalDetailBean);
		} catch (DataAccessException e) {
			logger.fatal(e.getMessage());
			throw e;
		}
		if(logger.isDebugEnabled()){ logger.debug(" SlipApplicationServiceImpl : removeJournalDetail 종료 "); }
	}

	private void modifyJournalDetail(JournalDetailBean journalDetailBean) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" SlipApplicationServiceImpl : modifyJournalDetail 시작 "); }
		try {
			journalDetailDAO.updateJournalDetail(journalDetailBean);
		} catch (Exception e) {
			logger.fatal(e.getMessage());
			throw e;
		}
		if(logger.isDebugEnabled()){ logger.debug(" SlipApplicationServiceImpl : modifyJournalDetail 종료 "); }
	}

	private void registerJournalDetail(JournalDetailBean journalDetailBean) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" SlipApplicationServiceImpl : registerJournalDetail 시작 "); }
		try {
			journalDetailDAO.insertJournalDetail(journalDetailBean);
		} catch (DataAccessException e) {
			logger.fatal(e.getMessage());
			throw e;
		}
		if(logger.isDebugEnabled()){ logger.debug(" SlipApplicationServiceImpl : registerJournalDetail 종료 "); }
	}

	@Override
	public ArrayList<SlipBean> findRangedSlipList(String fromDate, String toDate) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" SlipApplicationServiceImpl : findRangedSlipList 시작 "); }
		ArrayList<SlipBean> slipList = null;
		try {
			slipList = slipDAO.selectRangedSlipList(fromDate, toDate);
			for (SlipBean slip : slipList) {
				/* if (slip.getApprovalEmpCode() != null) {
					slip.setApprovalEmpName(employeeDAO.selectEmployee(slip.getApprovalEmpCode()).getEmpName());
				}*/
				ArrayList<JournalBean> journalList = journalDAO.selectJournalList(slip.getSlipNo());
				slip.setJournalList(journalList);
			}
		} catch (DataAccessException e) {
			logger.fatal(e.getMessage());
			throw e;
		}
		if(logger.isDebugEnabled()){ logger.debug(" SlipApplicationServiceImpl : findRangedSlipList 종료 "); }
		return slipList;
	}

	@Override
	public ArrayList<SlipBean> findDisApprovalSlipList() {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" SlipApplicationServiceImpl : findDisApprovalSlipList 시작 "); }
		ArrayList<SlipBean> disApprovalSlipList = null;
		disApprovalSlipList=slipDAO.selectDisApprovalSlipList();
		for(SlipBean slip : disApprovalSlipList){
			slip.setWriteEmpName(employeeDAO.selectEmployee(slip.getWriteEmpCode()).getEmpName());
			 ArrayList<JournalBean> journalList = journalDAO.selectJournalList(slip.getSlipNo());
			 for(JournalBean journal : journalList) {
				 JournalDetailBean journalDetail = journalDetailDAO.selectJournalDetailList(journal.getSlipNo(), journal.getJournalCode());
				 journal.setJournalDetailBean(journalDetail);
			}
			 slip.setJournalList(journalList);
		}
		if(logger.isDebugEnabled()){ logger.debug(" SlipApplicationServiceImpl : findDisApprovalSlipList 종료 "); }
		return disApprovalSlipList;	}

	

}
