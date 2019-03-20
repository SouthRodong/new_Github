<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script>
var empCode;
var empName;

var disApprovalSlipList=[]; 
var emptySlipList=[]; 

/*선택한 전표행의 정보 */
var slipNo; 
var slipStatus;
var slipRow;
/*현재 날짜 -> 승인 날짜가 됨*/
var today;

	$(document).ready(function(){
		today = $.datepicker.formatDate($.datepicker.ATOM, new Date());
		$("#journalListDialog").css({ 
			visibility : "hidden"
		});
		empCode = "${sessionScope.empCode}";
		empName = "${sessionScope.empName}";
		
		getDisApprovalDataFunc();
		
		$("#disApprovalInfoBtn").click(getDisApprovalInfoFunc).button(); 
		$("#approvalSuccessBtn").click(approvalSucessFunc).button(); 
		$("#approvalCancelBtn").click(approvalCancelFunc).button(); 
		$("#closeBtn").click(closeFunc).button(); 
		$("#batchSaveBtn").click(batchSaveFunc).button();
		
	});
	
	
	
	function getDisApprovalDataFunc(){
		//미승인 전표 정보 가져오기
	    $.ajax({
	        url: "${pageContext.request.contextPath}/account/savenapproval.do",
	        type: "post",
	        data: {"method": "findDisApprovalSlipList"},
	        dataType: "json",
	        success: function(data, textStatus, jqXHR) {
	        	
	        	if(data.errorCode < 0) {
	                alert(data.errorMsg);
	            } else {
	                if(data.disApprovalSlipList.length == 0){
	                	errorMsgDialog("미승인전표가없습니다.","warning");
	                }
	            }
	        	
	       for(index=0;index<data.disApprovalSlipList.length;index++){
	    		data.disApprovalSlipList[index].approvalEmpCode = empCode;
	    	  	data.disApprovalSlipList[index].approvalEmpName = empName;
	       }
	            disApprovalSlipList = data.disApprovalSlipList;
	            console.log(disApprovalSlipList);
	          	getDisApprovalSlipList();

	        },
	        error: function(jqXHR, textStatus, error) {
	            errorDialog("전표 조회 오류입니다","warning");
	        }
	    });
	}
	    function getDisApprovalSlipList(){
			$.jgrid.gridUnload("#disApprovalSlipGrid");
			$("#disApprovalSlipGrid").jqGrid({
				data : disApprovalSlipList, 
				datatype : "local",
				editurl : "clientArray", 
				jsonReader :{
					root : "disApprovalSlipList"
				},
		        colNames: ["순번","전표번호","결의일자","품의코드","품의내역","전표유형","작성자코드","작성자이름","대차차액","승인자코드","승인자","승인상태","승인일자","상태","분개"],
		        colModel: [
		                {name: "slip_seq", hidden: true, width: 20, editable: false},
				 		{name :"slipNo",hidden: true,width :140,editable:true},
		               	{name :"writeDate",width :150 , align :"center",editable:false},
		               	{name :"requestCode",width :120 , align :"center",editable:false},
		           		{name :"requestName",width :180 , align :"center",editable:false},
		           		{name :"slipType",width :80,align: "center", editable: true}, 
		           		{name :"writeEmpCode",hidden: true,width :170 ,editable:false},
		           		{name :"writeEmpName",width :100 , align :"center",editable:false},
		           		{name :"balanceDiff",width :100 , align :"center",editable:false},
		           		{name :"approvalEmpCode",hidden: true,width :100,editable:false},
		           		{name :"approvalEmpName",width : 160,align:"center",editable:false},
		           		{name :"approvalState",width :130 , align :"center",editable:false},
		           		{name :"approvalDate",width :140 , align :"center",editable:false},
		           		{name :"status",width :90 , align :"center",editable:false},
		           		{name :"journalBean",hidden: true ,width :100 , align :"center",editable:false}
		        ],
				height:"auto",
				width:1200,
				multiselect : true,
				multiboxonly : true,
		       
				cmTemplate: { sortable: true },
				cache:false,
			    rownumbers: true,
			    viewrecords: true,
			    rowNum :30,
			    
				caption:"승인 대기 리스트",
		        onSelectRow: function(id) {
					slipStatus = $(this).jqGrid ('getCell', id, 'status');
					slipNo=$(this).getRowData(id).slipNo;
					slipRow= $(this).jqGrid('getGridParam', 'selrow');
		        }
		          
		    });
		}
		
		
		//미승인 전표의 분개보기
		function getDisApprovalInfoFunc(){

			$("#journalListDialog").dialog({
				title : "분개 정보 보기",
					"width":"1470",
					"height":"550",
				  open: function(event, ui) {
				  $(".ui-dialog-titlebar-close", $(this).parent()).hide();
				   }	
				 });

			$("#journalListDialog").css({
				visibility : "visible"  
			});

			$.jgrid.gridUnload("#journalListGrid"); /*리로드 실행*/
			$("#journalListGrid").jqGrid({
				data :  (isNaN(slipRow)) ? [] : disApprovalSlipList[slipRow-1].journalList, 
				datatype : "local",
				jsonReader :{
				root : "journalBean" /*선택한 전표의 journalBean 실행함*/
				},
		        colNames: ["대차구분", "계정코드", "계정과목", "증빙코드", "증빙", "금액", "적요코드", "적요명", "거래처코드", "거래처"],
		        colModel: [

		               	{name :"balanceDivision",width :90 , align :"center",editable:true},		               	
		               	{name :"accountCode",width :100 , align :"center",editable:false},
		           		{name :"accountName",width :180 , align :"center",editable:false},
		           		{name :"voucherCode",width :90 ,align: "center", editable:false},
		           		{name :"voucherName",width :180,align :"center",editable:false},
		           		{name :"price",width :160 , align :"center",editable:false, formatter:'currency', formatoptions:{thousandsSeparator:",", decimalPlaces:0}},
		           		{name :"descCode",width :90 , align :"center",editable:false},
		           		{name :"descName",width :170,editable:false},
		           		{name :"customerCode",width : 90,align:"center",editable:false},
		           		{name :"customerName",width :170 , align :"center",editable:false},
		        ],
				height:170,
				width:"auto",
				multiselect : true,
				multiboxonly : true,
				cmTemplate: { sortable: false },
				cache:false,
			    rownumbers: true,
			    viewrecords: true,
			    rowNum :30,
				caption:"분개 리스트",
				onSelectRow: function(id){ /*분개 상세 보기*/
		        	var accountCode = $(this).getCell(id, "accountCode");
		        	if(accountCode != null && accountCode!= ""){
		        		$.ajax({
		        	        url: "${pageContext.request.contextPath}/account/accountDetail.do",
		        	        type: "post",
		        	        data: {"method": "findAccountControlList", accountCode: accountCode },
		        	        dataType: "json",
		                    success: function(data) {
		                        if(data.errorCode < 0) alert(data.errorMsg);
		                        console.log(data.accountControlList);
		                        showJournalDetailList(id, data.accountControlList);
		                        
		                    }
		                });
		            }
				}
				
			});

		}
		
		function showJournalDetailList(id , accountControlList){

			var journalDetailData = disApprovalSlipList[slipRow-1].journalList[id-1].journalDetailBean;
			
			var journalDetailvalueArray=[];
			    if(journalDetailData!=null){
		        	journalDetailvalueArray.push(journalDetailData.value1);
		        	journalDetailvalueArray.push(journalDetailData.value2);
		        	journalDetailvalueArray.push(journalDetailData.value3);
		        	journalDetailvalueArray.push(journalDetailData.value4);
		        	journalDetailvalueArray.push(journalDetailData.value5);
		        }else {
					for(var i=0; i<5; i++) {
						journalDetailvalueArray.push("");
					}
		    	 }
			    $("#journalDetailList").empty();
			    $.each(accountControlList, function(index, element) {
			        $("#journalDetailList").append("<font style='color:#000000'>"+element.controlName + " : </font> &nbsp;&nbsp;");
			        $("<input type='text' style='height:20px; width:500px;' id='" + element.accountDetailCode + "' value='" + journalDetailvalueArray[index] + "'>")
			        .appendTo("#journalDetailList").button();

			        $("#journalDetailList").append("&nbsp;&nbsp;&nbsp;<br/>");
				});
		}
		
		
		
		function approvalSucessFunc(){
			 $("#disApprovalSlipGrid").setCell(slipRow, "status", "update");
			 $("#disApprovalSlipGrid").setCell(slipRow, "approvalState", "승인완료");
			 
			 $("#disApprovalSlipGrid").setCell(slipRow, "approvalDate", today);
			 $("#journalListDialog").dialog("close");
			 $("#journalDetailList").empty();
		}
		
		function approvalCancelFunc(){
			 $("#disApprovalSlipGrid").setCell(slipRow, "status", "update");
			 $("#disApprovalSlipGrid").setCell(slipRow, "approvalState", "승인취소");
			 $("#journalListDialog").dialog("close");
			 $("#journalDetailList").empty();
		}
		
		function closeFunc(){
			 $("#journalListDialog").dialog("close");
			 $("#journalDetailList").empty();
		}
		
		
		/*일괄 저장*/
		function batchSaveFunc() {

		    var resultList = []; 

		    for(index = 0; index < disApprovalSlipList.length; index++) {

		        if(disApprovalSlipList[index].status == "update"){
		        	resultList.push(disApprovalSlipList[index]);
		        }
		    }
		    
		    console.log(JSON.stringify(resultList));
		    // 최종적으로 넘어갈 데이터 보기
		    $.ajax({
		        url: "${pageContext.request.contextPath}/account/savenapproval.do",
		        type: "post",
		        data: {"method": "batchListProcess", batchList: JSON.stringify(resultList)},
		        dataType: "json",
		        success: function(data, textStatus, jqXHR) {
		        	alert(data.errorCode);
					if(data.errorCode<0){
						errorMsgDialog("일괄처리실패","warning");
					}else{
						errorMsgDialog("일괄처리성공","success");
					}		            
		        }
		    });
		}
		
		
		
		/*경고 창*/
		function errorMsgDialog(errorMsg,alerts){
		
			if(alerts == "warning"){
				$("#errorDialog").html(errorMsg);
		   		$(function(){
		   	 	$("#errorDialog").dialog({
		    		title : "경고 메세지" ,
		    		modal : true,
		     		width : 500,
		     		heigth : 500,
		     		buttons : {
		      		ok : function(){
		      		 $(this).dialog("close");
		      		}
		     		}
		   		 });
		    	});
			}
			
			
			if(alerts == "success"){
				$("#errorDialog").html(errorMsg);
		   		$(function(){
		   	 	$("#errorDialog").dialog({
		    		title : "성공 메세지" ,
		    		modal : true,
		     		width : 500,
		     		heigth : 500,
		     		buttons : {
		      		ok : function(){
		    	     $(location).attr("href","${pageContext.request.contextPath}/base/approvalmanager.html");
		      		 $(this).dialog("close");
		      		}
		     		}
		   		 });
		    	});
			}
			
		}
		
	

</script>

</head>
<body>
	<table>
		<tr>
			<td>
				<!-- 전표 -->
       			<input type="button" id="disApprovalInfoBtn" value = "전표상세보기">
      		 	<input type="button" id="batchSaveBtn" value="일괄저장">
      		 	<br><br>
        		<table id="disApprovalSlipGrid"></table> 
			</td>
		</tr>		
	</table>


    	<div id="journalListDialog"> 
			<table id="journalListGrid"></table>
		<!-- 분개 상세 보기 [동적]-->
		<div id = "journalDetailList"></div>
		<br/><br/><br/>
		<center>
			<input type="button"  id="approvalSuccessBtn" value="승인 완료">
			<input type="button" id="approvalCancelBtn" value="승인 취소">
			<input type="button" id="closeBtn" value = "닫기">
			<div id="pager" style="z-index: 0"></div>
		</center>
		</div>
		<div id ="errorDialog"></div>
	
</body>
</html>