package com.yum.base.applicationService;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yum.base.applicationService.CodeListApplicationService;
import com.yum.base.applicationService.CodeListApplicationServiceImpl;
import com.yum.base.dao.CodeDAO;
import com.yum.base.dao.CodeDAOImpl;
import com.yum.base.dao.DetailCodeDAO;
import com.yum.base.dao.DetailCodeDAOImpl;
import com.yum.base.to.CodeBean;
import com.yum.base.to.DetailCodeBean;
import com.yum.common.exception.DataAccessException;

public class CodeListApplicationServiceImpl implements CodeListApplicationService{

	protected final Log logger = LogFactory.getLog(this.getClass());
	
	private CodeDAO codeDAO = CodeDAOImpl.getInstance();
	
	private DetailCodeDAO codeDetailDAO = DetailCodeDAOImpl.getinstance();
	
	private static CodeListApplicationService instance ;
	private CodeListApplicationServiceImpl(){};
	public static CodeListApplicationService getInstance() {
		// TODO Auto-generated method stub
		if(instance ==null){
			instance = new CodeListApplicationServiceImpl();
			System.out.println("		@ CodeListApplicationServiceImpl에 접근");
		}
		return instance;
	}

	
	
	private DetailCodeDAO detailCodeDAO = DetailCodeDAOImpl.getinstance();
	@Override
	public ArrayList<DetailCodeBean> findDetailCodeList(String code) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" CodeListApplicationServiceImpl : findDetailCodeList 시작 "); }
		
		ArrayList<DetailCodeBean> detailCodeList = null;
		try{
		detailCodeList = detailCodeDAO.selectDetailCodeList(code);
		}catch(Exception e){
			logger.fatal(e.getMessage());
			throw e;    
		}
		if(logger.isDebugEnabled()){ logger.debug(" CodeListApplicationServiceImpl : findDetailCodeList 종료 "); }
		return detailCodeList;
	}
	@Override
	public ArrayList<CodeBean> findCodeList() {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" CodeListApplicationServiceImpl : findCodeList 시작 "); }
		ArrayList<CodeBean> codeList = null;
		try{
			codeList = codeDAO.selectCodeList(); 
			
			for(CodeBean codeBean : codeList){						
				codeBean.setCodeDetailList(codeDetailDAO.selectDetailCodeList(codeBean.getDivisionCode()));
			}		
		
		}catch(DataAccessException e){
			System.out.println("		@ 코드어플서비스 조회 오류");
			logger.fatal(e.getMessage());
			throw e;
		}
		if(logger.isDebugEnabled()){ logger.debug(" CodeListApplicationServiceImpl : findCodeList 종료 "); }
		return codeList;
	}
	
	@Override
	public void batchCodeProcess(ArrayList<CodeBean> codeList) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()){ logger.debug(" CodeListApplicationServiceImpl : batchCodeProcess 시작 "); }
		try {
			
			for (CodeBean code : codeList) {
				switch (code.getStatus()) {
				case "insert":
					codeDAO.insertCode(code); 
					break;
				case "update":
					codeDAO.updateCode(code); 
					break;
				case "normal":
					break;
				case "delete":
					codeDAO.deleteCode(code.getDivisionCode());	
				}
				ArrayList<DetailCodeBean> DetailcodeList = code.getCodeDetailList();
				for (DetailCodeBean codeDetailBean : DetailcodeList) {
					switch (codeDetailBean.getStatus()) {
					case "insert":
						codeDetailDAO.insertDetailCode(codeDetailBean);
						break;
					case "update":
						codeDetailDAO.updateDetailCode(codeDetailBean);
						break;
					case "delete":
						codeDetailDAO.deleteDetailCode(codeDetailBean.getCodeNo());
					}
				}

			}
		} catch (DataAccessException e) {
			logger.fatal(e.getMessage());

			throw e;
		}
		if(logger.isDebugEnabled()){ logger.debug(" CodeListApplicationServiceImpl : batchCodeProcess 종료 "); }
	}

	

}
