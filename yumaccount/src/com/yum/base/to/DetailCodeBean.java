package com.yum.base.to;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yum.base.to.BaseBean;

@JsonIgnoreProperties(ignoreUnknown = true) 

public class DetailCodeBean extends BaseBean{
	   String divisionCode;
	   String codeNo;
	   String codeName;
	   String codeUseCheck;

	   public String getDivisionCode() {
	      return divisionCode;
	   }
	   public void setDivisionCode(String divisionCode) {
	      this.divisionCode = divisionCode;
	   }
	   public String getCodeNo() {
	      return codeNo;
	   }
	   public void setCodeNo(String codeNo) {
	      this.codeNo = codeNo;
	   }
	   public String getCodeName() {
	      return codeName;
	   }
	   public void setCodeName(String codeName) {
	      this.codeName = codeName;
	   }
	   public String getCodeUseCheck(){
	         return codeUseCheck;
	      }
	   public void setCodeUseCheck(String codeUseCheck){
	         this.codeUseCheck = codeUseCheck;
	      }
}
