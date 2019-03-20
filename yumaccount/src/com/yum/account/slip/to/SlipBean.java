package com.yum.account.slip.to;

import java.util.ArrayList;

import com.yum.account.slip.to.JournalBean;
import com.yum.base.to.BaseBean;

public class SlipBean extends BaseBean{
	   private String slipNo;
	   private String writeEmpCode;
	   private String approvalEmpCode;
	   private String writeDate;
	   private int slipSeq;
	   private String requestCode;
	   private String requestName;
	   private String slipType;
	   private String balanceDiff;
	   private int approvalSeq; 
	   private String approvalState;
	   private String approvalDate;
	   private String writeEmpName;
	   private String approvalEmpName;
	   private ArrayList<JournalBean> journalList;

	   public String getSlipNo() {
	      return slipNo;
	   }
	   public void setSlipNo(String slipNo) {
	      this.slipNo = slipNo;
	   }
	   public String getWriteDate() {
	      return writeDate;
	   }
	   public void setWriteDate(String writeDate) {
	      this.writeDate = writeDate;
	   }
	   public int getSlipSeq() {
	      return slipSeq;
	   }
	   public void setSlipSeq(int slipSeq) {
	      this.slipSeq = slipSeq;
	   }
	   public String getRequestCode() {
	      return requestCode;
	   }
	   public void setRequestCode(String requestCode) {
	      this.requestCode = requestCode;
	   }
	   public String getSlipType() {
	      return slipType;
	   }
	   public void setSlipType(String slipType) {
	      this.slipType = slipType;
	   }
	   public String getApprovalState() {
	      return approvalState;
	   }
	   public void setApprovalState(String approvalState) {
	      this.approvalState = approvalState;
	   }
	   public int getApprovalSeq() {
	      return approvalSeq;
	   }
	   public void setApprovalSeq(int approvalSeq) {
	      this.approvalSeq = approvalSeq;
	   }
	   public String getBalanceDiff() {
	      return balanceDiff;
	   }
	   public void setBalanceDiff(String balanceDiff) {
	      this.balanceDiff = balanceDiff;
	   }
	   public String getWriteEmpCode() {
	      return writeEmpCode;
	   }
	   public void setWriteEmpCode(String writeEmpCode) {
	      this.writeEmpCode = writeEmpCode;
	   }
	   public String getApprovalEmpCode() {
	      return approvalEmpCode;
	   }
	   public void setApprovalEmpCode(String approvalEmpCode) {
	      this.approvalEmpCode = approvalEmpCode;
	   }
	   public String getApprovalDate() {
	      return approvalDate;
	   }
	   public void setApprovalDate(String approvalDate) {
	      this.approvalDate = approvalDate;
	   }
	   public String getRequestName() {
	      return requestName;
	   }
	   public void setRequestName(String requestName) {
	      this.requestName = requestName;
	   }
	   public String getWriteEmpName() {
	      return writeEmpName;
	   }
	   public void setWriteEmpName(String writeEmpName) {
	      this.writeEmpName = writeEmpName;
	   }
	   public String getApprovalEmpName() {
	      return approvalEmpName;
	   }
	   public void setApprovalEmpName(String approvalEmpName) {
	      this.approvalEmpName = approvalEmpName;
	   }
	   public ArrayList<JournalBean> getJournalList() {
	      return journalList;
	   }
	   public void setJournalList(ArrayList<JournalBean> journalList) {
	      this.journalList = journalList;
	   }

}
