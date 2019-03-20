package com.yum.base.to;

import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yum.base.to.BaseBean;
import com.yum.base.to.DetailCodeBean;
			
@JsonIgnoreProperties(ignoreUnknown = true)
public class CodeBean extends BaseBean{
	   String divisionCode;
	   String codeName;
	   String codeUseCheck;
	   ArrayList<DetailCodeBean> codeDetailList;
	   
	   public String getCodeUseCheck() {
	      return codeUseCheck;
	   }

	   public void setCodeUseCheck(String codeUseCheck) {
	      this.codeUseCheck = codeUseCheck;
	   }

	   public String getDivisionCode() {
	      return divisionCode;
	   }

	   public void setDivisionCode(String divisionCode) {
	      this.divisionCode = divisionCode;
	   }

	   public String getCodeName() {
	      return codeName;
	   }

	   public void setCodeName(String codeName) {
	      this.codeName = codeName;
	   }

	   public ArrayList<DetailCodeBean> getCodeDetailList() {
	      return codeDetailList;
	   }

	   public void setCodeDetailList(ArrayList<DetailCodeBean> codeDetailList) {
	      this.codeDetailList = codeDetailList;
	   }
}
