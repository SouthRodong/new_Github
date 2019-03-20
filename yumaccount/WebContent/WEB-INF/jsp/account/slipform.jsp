<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title> 전표입력</title>
<script>

var empCode = "${sessionScope.empCode}";
var empName = "${sessionScope.empName}";
var empAuthority = "${sessionScope.authority}";
var slipList = [];
var emptySlip = {};
var emptyJournal = {};
var emptyJournalDetail = {};   	// 분개상세 빈 객체
var slipjour = {};				//extend(카피)를 하기위한 멤버 셋팅
var serchSlipDate;
var slipStatus;        			// 전표 상태
var slipNo;            			// 전표 번호
var slipRow;        			// 전표 row
var slipId;
var journalStatus;      		// 분개 상태
var journalCode;      			// 분개 코드
var journalRow;         		// 분개 row
var selJournalId;
var slipSelection;       		// 전표에서 선택한 row
var journalSelection;   		// 분개에서 선택한 row
var debitTotalPrice = 0;
var creditTotalPrice=0;
var debitList = [];
var creditList = [];

var checkapprovalState;

   $(document).ready(function(){
	   $.datepicker.setDefaults({
			dateFormat : 'yy-mm-dd',
			prevText : '이전 달',
			nextText : '다음 달',
			monthNames : [ '1월', '2월', '3월', '4월', '5월', '6월', '7월',
					'8월', '9월', '10월', '11월', '12월' ],
			monthNamesShort : [ '1월', '2월', '3월', '4월', '5월', '6월',
					'7월', '8월', '9월', '10월', '11월', '12월' ],
			dayNames : [ '일', '월', '화', '수', '목', '금', '토' ],
			dayNamesShort : [ '일', '월', '화', '수', '목', '금', '토' ],
			dayNamesMin : [ '일', '월', '화', '수', '목', '금', '토' ],
			showMonthAfterYear : true,
			changeMonth: true,
	        changeYear: true,
			yearSuffix : '년'
		}); /*한글로 달력 보이기 위해서 */

		$("#reStartButton").hide();
		$("#reStartButton").click(reOpenFunc);
		
      today = $.datepicker.formatDate($.datepicker.ATOM, new Date()); //jQuery datepicker(달력)&오늘날짜입력
      $("#slipDate").val(today);

       /* 버튼이벤트  */
      $("input[type=button]").button();
      $("#slipDate").button().datepicker({dateFormat: "yy-mm-dd"});
      $("#slipSearchButton").click(slipSearchDataFunc);
      $("#slipUpdateButton").click(slipUpdateFunc);
      $("#slipInsertButton").click(slipInsertFunc);
      $("#journalInsertButton").click(journalInsertFunc);
      $("#journalUpdateButton").click(journalUpdateFunc);
      $("#journalDeleteButton").click(journalDeleteFunc);
      $("#slipDeleteButton").click(slipDeleteFunc);
      $("#batchSaveButton").click(batchSaveFunc);
      $("#pdfOpenButton").click(pdfOpenFunc);

      
      
      
      showSlipSearch();
      showJournalList();
      showCreditGrid(creditTotalPrice,creditList);
      showDebitGrid(debitTotalPrice,debitList);
      
   });

   function slipSearchDataFunc() {
      serchSlipDate=$("#slipDate").val();
      findSlipDataList(serchSlipDate);
      
     
      $("#slipDate").hide(); 
      $("#slipSearchButton").hide(); 

      $("#reStartButton").show();            

      
}
   
   function reOpenFunc(){
	   location.href="${pageContext.request.contextPath}/account/slipform.html";
   }
   
   /* PDF 보기 */
   function pdfOpenFunc(){
        window.open("${pageContext.request.contextPath}/base/report.html?slipNo="+slipNo, "window", "width=1600, height=1000");
   }

   /* 데이타 가져오기  */
   function findSlipDataList(serchSlipDate) {
	   	 
       $.ajax({
           url: "${pageContext.request.contextPath}/account/slip.do",
           type: "post",
           data: {"method" : "findSlipDataList", slipDate: serchSlipDate },
           dataType: "json",
           success: function(data, textStatus, jqXHR) {

              console.log(data);
              console.log(data.slipList);
              console.log(data.emptySlip);
              console.log(data.emptyJournal);
              console.log(data.emptyJournalDetail);
              console.log(data.emptyJournalDetail);
              console.log(creditTotalPrice);
              console.log(creditList);
              console.log(debitTotalPrice);
              console.log(debitList);
				
               if(data.errorCode < 0) {
                   alert(data.errorMsg);
               } else {
               if(data.slipList.length == 0){
            	   errorCheckDialog("전표정보가 없습니다.","warning");
               }
              
        	           	   
                  slipList = data.slipList;   // 날짜에 존재하는 전표데이터 가져옴
                  emptySlip = data.emptySlip;
                  emptyJournal = data.emptyJournal;
                  emptyJournal.journalDetailBean = data.emptyJournalDetail;
                  emptyJournalDetail = data.emptyJournalDetail;//새로운 전표 or 분개 생성위해  빈객체 가져옴
                   // 조회후 빈 객체 내용 추가
              	  generateBase();
                   // 조회된 전표 출력
                  showSlipSearch();
                  showJournalList();
                  showCreditGrid(creditTotalPrice,creditList);
                  showDebitGrid(debitTotalPrice,debitList);
               }
           },
           error: function(jqXHR, textStatus, error) {
               alert("전표 조회 오류입니다");
           }
       });
   }

   /* 전표,분개,분개상세를 추가하기 위한 빈객체를 세팅하는 작업 */
   function generateBase() {
      emptySlip.status = "insert";
      emptySlip.writeDate = $("#slipDate").val();
      emptySlip.writeEmpCode =empCode;
      emptySlip.writeEmpName = empName;
      emptySlip.slipType = "선택";
      emptySlip.approvalState = "승인대기";
      emptySlip.balanceDiff = "0";
      emptySlip.approval_seq = "0";
      emptyJournal.status = "insert";
      emptyJournal.balanceDiff = "선택";
      emptyJournal.price = "0";
      emptyJournal.descCode="코드찾기";
      emptyJournal.voucherCode="코드찾기";
      emptyJournal.accountCode="코드찾기";
      emptyJournal.customerCode="코드찾기";
   }

   /* 전표 그리드 */ /* 화면에 얻어온 전표목록을 띄우는 함수 */
   function showSlipSearch(){
	 
      $.jgrid.gridUnload("#slipGrid");
      $("#slipGrid").jqGrid({
         data : slipList,
         datatype : "local",
         jsonReader :{
         root : "slipList"
         },
           colNames: ["순번","전표번호","결의일자","품의코드","품의내역","전표유형","작성자코드","작성자이름","대차차액","승인자","승인상태","승인일자","상태","분개"],
           colModel: [
                      {name: "slip_seq", hidden: true, width: 20, editable: false},
                   {name :"slipNo",hidden: true,width :140,editable:true},
                    {name :"writeDate",width :150 , align :"center",editable:false},
                    {name :"requestCode",width :150 , align :"center",editable:false},
                    {name :"requestName",width :170 , align :"center",editable:false},
                    {name :"slipType",width :150,align: "center", editable: true, edittype: "select", editoptions: {value: "선택:선택;일반:일반;매입:매입;매출:매출;수금:수금;반제:반제"} },  //, edittype: "select", editoptions: {value: "선택:선택;일반:일반;매입:매입;매출:매출;수금:수금;반제:반제"}
                                        
                    {name :"writeEmpCode",hidden: true,width :120 ,editable:false},
                    {name :"writeEmpName",width :150 , align :"center",editable:false},
                    {name :"balanceDiff",width :170 , align :"center",editable:false, formatter:'currency', formatoptions:{thousandsSeparator:",", decimalPlaces:0}},
                    {name :"approvalEmpName",width :170 , align :"center",editable:false},
                    {name :"approvalState",width :170 , align :"center",editable:false},
                    {name :"approvalDate",width :170 , align :"center",editable:false},
                    {name :"status",width :150 , align :"center",editable:false},
                    {name :"journalBean",hidden: true ,width :500 , align :"center",editable:false}
           ],
         height:250,
         width:1100,
         multiselect : true,
         multiboxonly : true,
         sortname: "slip_seq",
         sortorder: "asc",
         cmTemplate: { sortable: false },
         cache:false,
         rownumbers: true,
         viewrecords: true,
         rowNum :30,
         caption:"전표 리스트",
         onSelectRow: function(id) {
        	
            saveJournalDetail(slipjour["slip"], slipjour["jour"]);
            
            if(id && id !== slipId) {
                   $(this).restoreRow(slipId);           
                   slipId = id;                          
            }
            
            slipStatus = $(this).jqGrid ('getCell', id, 'status');
            slipNo=$(this).jqGrid ('getCell', id, 'slipNo');
            
            
            slipRow= $(this).jqGrid('getGridParam', 'selrow');
            checkapprovalState = $(this).jqGrid ('getCell', id, 'approvalState');
            
           
            if(checkapprovalState != '승인완료'){
            	$(this).editRow(id, true);
            }else{}
            //$(this).editRow(id, true);
            
            //jQuery('#slipGrid').jqGrid('editRow',id,true);    //퍼옴



           },
           onCellSelect: function(rowid, iCol, cellcontent, e) {
              var status = $(this).getCell(rowid,"status");
              if(status == "normal"){
              }else if (status == "update" || status == "insert"){
                  if(iCol == 5) {
                     showCodeDialog(this ,rowid , iCol , "C019","품의내역");
                  }
              }
            console.log(slipjour["slip"]);
            console.log(slipjour["jour"]);
            console.log(slipId);
            console.log(rowid);
            console.log(iCol);
            console.log(cellcontent);
            console.log(e);

            showJournalList(rowid);
            showBalanceSheet();
           }
           
       }).bind("jqGridInlineAfterSaveRow", function (e, rowid, orgClickEvent) {
           var status = $(this).getCell(rowid, "status");
           if(status != "insert")  {
               $(this).setCell(rowid, "status", "update");
               $("#slipGrid").setCell(rowid, "status", "update");
           }

       });
   }

   /* 분개  그리드 */
   function showJournalList(slipId){
	  
      $.jgrid.gridUnload("#journalGrid");
      $("#journalGrid").jqGrid({
         data :  (isNaN(slipId)) ? [] : slipList[slipId-1].journalList,
         datatype : "local",
         jsonReader :{
         root : "journalBean"
         },
           colNames: ["전표번호", "분개순번", "대차구분", "계정코드", "계정과목", "증빙코드", "증빙", "금액", "적요코드", "적요명", "거래처코드", "거래처", "상태", "분개상세"],
           colModel: [
                  {name: "slipNo", hidden: true,width: 20, align :"center",editable: false},
                  {name :"journalCode", hidden: true , width :20,align :"center",editable: false},
                  {name :"balanceDivision",width :90 , align :"center",editable:true, edittype: "select", editoptions: {value: "선택:선택;대변:대변;차변:차변"}},
                  {name :"accountCode",width :100 , align :"center",editable:false},
                  {name :"accountName",width :180 , align :"center",editable:false},
                  {name :"voucherCode",width :90 ,align: "center", editable:false},
                  {name :"voucherName",width :180,align :"center",editable:false},
                  {name :"price",width :160 , align :"center",editable:true, formatter:'currency', formatoptions:{thousandsSeparator:",", decimalPlaces:0}},
                  {name :"descCode",width :90 , align :"center",editable:false},
                  {name :"descName",width :170,editable:true},
                  {name :"customerCode",width : 90,align:"center",editable:false},
                  {name :"customerName",width :170 , align :"center",editable:true},
                  {name :"status",width :100 , align :"center",editable:false},
                  {name :"journalDetailBean",hidden: true,width :100 , align :"center",editable:false}
           ],
         height:250,
         width:1100,
         multiselect : true,
         multiboxonly : true,
         cmTemplate: { sortable: false },
         cache:false,
         rownumbers: true,
         viewrecords: true,
         rowNum :30,
         caption:"분개 리스트",
         onSelectRow: function(id){
        	
             saveJournalDetail(slipjour["slip"], slipjour["jour"]); 
        	 if(id && id !== selJournalId) {
                      $(this).restoreRow(selJournalId);
                      selJournalId = id;
                  }
                  //$(this).editRow(id, true);
            journalStatus = $(this).jqGrid ('getCell', id, 'status');
            journalCode = $(this).getRowData(id).journalCode;
            journalRow = $(this).jqGrid('getGridParam', 'selrow');
            var status = $("#slipGrid").getCell(slipId, "status");
            
            if(checkapprovalState != '승인완료'){
            	$(this).editRow(id, true);
            	 if(status != "insert"){
                     $("#slipGrid").jqGrid('setRowData',slipRow,{status:"update"});
                 }
            }else{
            	
            }
         },
         
         
         
           onCellSelect: function(rowid, iCol, cellcontent, e) {
        	 
              console.log("선택된 전표의 iCol:" + iCol);
              var status = $(this).getCell(rowid,"status");
              if(status == "select"){
              }else if (status == "update" || status == "insert"){
                  if(iCol == 5) {
                     showAccountCodeDialog(this,rowid,iCol);
                  }else if(iCol==7){
                     showCodeDialog(this ,rowid , iCol , "C020","증빙");
                  }else if(iCol==10){
                     showCodeDialog(this ,rowid , iCol , "C006","적요");
                  }else if(iCol==12){
                     showCodeDialog(this,rowid,iCol,"C007","거래처");
                  } 
              }
              var accountCode = $(this).getCell(rowid, "accountCode");
              if(accountCode != null && accountCode!= ""){
                 $.ajax({
                      url: "${pageContext.request.contextPath}/account/accountDetail.do",
                      type: "post",
                      data: {"method" : "findAccountControlList", accountCode: accountCode },
                      dataType: "json",
                       success: function(data) {
                           if(data.errorCode < 0) alert(data.errorMsg);
                         
                           showJournalDetailGrid(slipId, rowid, data.accountControlList);
                           
                       }
                   });
               }
           }
           
       }).bind("jqGridInlineAfterSaveRow", function (e, rowid, orgClickEvent) {
           var status = $(this).getCell(rowid, "status");
           if(status != "insert")  {
               $(this).setCell(rowid, "status", "update");
               $("#journalGrid").setCell(rowid, "status", "update");
           }
           // 대차차액 계산
           balanceDiffCalculator(slipId);
           // 대차 대조표 갱신
           showBalanceSheet()
       });
   }

   /* 분개 상세 보기 */
   function showJournalDetailGrid(slipId, journalId, accountControlList){
	   
       saveJournalDetail(slipjour["slip"], slipjour["jour"]);
	   slipjour["slip"] = slipId; 
       slipjour["jour"] = journalId;
       
       var journalDetailData = slipList[slipId-1].journalList[journalId-1].journalDetailBean;
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
    	   
    	   if(element.controlName.length==2){
    		   $("#journalDetailList").append("<font style='color:white'>"+element.controlName + " : </font> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
               $("<input type='text' style='height:20px; width:500px;' id='" + element.accountControlCode + "' value='" + journalDetailvalueArray[index] + "'>").appendTo("#journalDetailList").button();
    	   }else if(element.controlName.length==3){
    		   $("#journalDetailList").append("<font style='color:white'>"+element.controlName + " : </font> &nbsp;&nbsp;&nbsp;");
               $("<input type='text' style='height:20px; width:500px;' id='" + element.accountControlCode + "' value='" + journalDetailvalueArray[index] + "'>").appendTo("#journalDetailList").button();
    	   }else{
    		   $("#journalDetailList").append("<font style='color:white'>"+element.controlName + " : </font> ");
               $("<input type='text' style='height:20px; width:500px;' id='" + element.accountControlCode + "' value='" + journalDetailvalueArray[index] + "'>").appendTo("#journalDetailList").button();
    	   }
    	   
           if(checkapprovalState != '승인완료'){
        	   if(element.inputType == "CALENDAR") {
                   $("#"+element.accountControlCode).datepicker({dateFormat : "yy-mm-dd"});
               } else if(element.inputType == "CODE") {

                  $("#"+element.accountControlCode).click(function(event) {
                      showCodeDialog(this, null, null, element.accountControlCode , element.controlName);
                   });
               } else if(element.inputType == "TEXT") {
               }
               
           }else{
        	   $("input[type=text]").attr("readonly",true);// readonly추가
           }
           $("#journalDetailList").append("&nbsp;&nbsp;&nbsp;<br/>");
          
           
       });
       
            
    	   saveJournalDetail(slipjour["slip"], slipjour["jour"]);
    	   $("#journalDetailList").children("input[type=text]").bind("change paste keyup", function() {

               var slipStatus = $("#slipGrid").getCell(slipId, "status");
               if(slipStatus != "insert")
                   $("#slipGrid").setCell(slipId, "status", "update");

               var journalStatus = $("#journalGrid").getCell(journalId, "status");
               if(journalStatus != "insert")
                   $("#journalGrid").setCell(journalId, "status", "update");
           });
      
       
      
       
   }

   //분개상세 저장
   
   function saveJournalDetail(slipId, journalId) {
	   /* var xxx=$("#journalGrid").find("td").children("input:text"); */
       if(slipId  === undefined) return;
       var saveData = $.extend(true, saveData, emptyJournalDetail);
       var inputs = $("#journalDetailList").children("input");
       
       
       $.each(inputs, function(index, element) { 
           saveData["item"+(index+1)] = $(element).attr("id");
           saveData["value"+(index+1)] = $(element).val();
           
       });
       
       var journalDetailBean = slipList[slipId-1].journalList[journalId-1].journalDetailBean;
        saveData["slipNo"] = journalDetailBean.slipNo;
        saveData["journalCode"] = journalDetailBean.journalCode;
       $.extend(true, journalDetailBean, saveData);
		
      }

   //일괄 저장하기
   
   function batchSaveFunc(){
	   
       var resultList = [];  //resultList에 저장할 값을 담음
       
       if(checkapprovalState != '승인완료'){

		  for(index = 0; index < slipList.length; index++) {
               if(slipList[index].status != "normal") {
                  // 수정이 되면 재승인을 받아야 한다
                  if(slipList[index].status =="update"){
                     slipList[index].approvalState = "재승인";
                     slipList[index].approvalDate = "";
                     slipList[index].approvalEmpName = "";
                     slipList[index].approvalEmpCode = "";
                  }
                  if(empAuthority == "관리자" ){
                	  //작성자가 관리자면 바로 승인완료 된다
                	 slipList[index].approvalState = "승인완료";
                	 slipList[index].approvalEmpCode = empCode;
                	 slipList[index].approvalEmpName = empName;
                	 slipList[index].approvalDate = $("#slipDate").val();
                  }
                  resultList.push(slipList[index]);
               }
           }
		  
       //대변 합계와 차변 합계가 0이 될때 일괄저장

    for(index = 0; index < slipList.length; index++) { 	
    if(resultList[index].balanceDiff != 0){
       console.log(resultList[index]); //내용물 확인차 콘솔 출력
       alert("대차차액이 0이 아닙니다.");
    }
    else{
    alert(JSON.stringify(resultList));	// 최종적으로 넘어갈 데이터 보기

        $.ajax({
           url: "${pageContext.request.contextPath}/account/savenapproval.do",
           type: "post",
           data: {"method" : "batchListProcess", batchList: JSON.stringify(resultList)},
           success: function(data, textStatus, jqXHR) {
        	   errorCheckDialog("정보가 일괄처리 되었습니다","success");
           }
           
       });
      }    
	}
       }else{ 
    	   errorCheckDialog("승인완료상태입니다.","warning");  //완료 상태에서는 수정불가능
       }
       
   }

   //전표,분개 추가
   //전표 추가
   function slipInsertFunc(){

      var nextSeq = Number($("#slipGrid").getGridParam("records"))+1;
      var slipNoArray = serchSlipDate.split("-");

      emptySlip.slipNo = slipNoArray[1]+slipNoArray[2]+nextSeq;

         $("#slipGrid").addRowData(nextSeq, emptySlip);
         slipList[nextSeq-1]["journalList"]=[];
      }

   //분개 추가
   function journalInsertFunc(){

	   if(checkapprovalState != '승인완료'){
		 
		   var nextSeq = Number($("#journalGrid").getGridParam("records"))+1;
	         var journalArray = serchSlipDate.split("-");

	         emptyJournal.journalCode = 'A0'+journalArray[2]+nextSeq;
	         emptyJournal.slipNo = slipList[slipRow-1].slipNo;
	         var tempJournal = {};
	         emptyJournal.journalDetailBean["slipNo"]= slipList[slipRow-1].slipNo;
	         emptyJournal.journalDetailBean["journalCode"]= emptyJournal.journalCode;
	         $.extend(true, tempJournal, emptyJournal);
	         tempJournal[""] = {};
	         $("#journalGrid").addRowData(nextSeq, tempJournal);
	         var status = $("#slipGrid").getCell(slipRow, "status");
	         if(status!="insert"){
	            $("#slipGrid").setCell(slipRow, "status", "update");
	         }
		   
	   }else{ 
    	   errorCheckDialog("승인완료상태입니다.","warning");
       }
	   
	   
         
   }

   
   //전표,분개 수정
   //전표 수정
   function slipUpdateFunc(){
	   if(checkapprovalState != '승인완료'){
		   $("#slipGrid").setCell(slipRow, "status", "update");
		   $("#slipGrid").editRow(slipRow, true);
       }else{ 
    	   errorCheckDialog("승인완료상태입니다.","warning");
       }
    
   }

   //분개 수정
   function journalUpdateFunc(){
	   
	   if(checkapprovalState != '승인완료'){
		  
		   if(slipNo == null){
		    	  errorCheckDialog("선택한 전표가 없습니다","warning");
		      }else{
		         var status = $("#journalGrid").getCell(journalRow, "status");
		         if(status =="insert"){
		            $("#journalGrid").setCell(journalRow, "status", "insert");
		         }else{
		          $("#journalGrid").setCell(journalRow, "status", "update");
		         }
		          $("#journalGrid").editRow(journalRow, true);
		      }
		   
       }else{ 
    	   errorCheckDialog("승인완료상태입니다.","warning");
       }
	   
     
   }

   /* 전표 , 분개 삭제하기 */

   function slipDeleteFunc(){

      if(slipNo == null){
    	  errorCheckDialog("삭제할 전표를 지정해주세요","warning");
      }else if(slipNo != null){
         $("#slipGrid").jqGrid('editRow',slipNo);
         $("#slipGrid").jqGrid('setRowData',slipRow,{status:"delete"});

         if(slipList[slipRow-1].journalList.length != 0){
        	 errorCheckDialog(" 해당 전표의 분개리스트를 삭제 하시겠습니까? ","slipDelete");
         }
      }
   }

   function journalDeleteFunc(){
      var selectDetailRow = $("#journalGrid").jqGrid('getGridParam', 'selrow');
      var deleteJournalCode = $("#journalGrid").getRowData(selectDetailRow).journalCode;
      if(selectDetailRow==null){
    	 
    	  errorCheckDialog("삭제 할 분개를 선택해 주세요","warning");
    	
      }else if(deleteJournalCode!=null){
         $("#journalGrid").jqGrid('editRow',deleteJournalCode);
         $("#journalGrid").jqGrid('setRowData',selectDetailRow,{status:"delete"});
         $("#slipGrid").jqGrid('setRowData',slipRow,{status:"update"});
      }
   }

   /* 대차 대조표 */
   function showBalanceSheet(){
      var debitTotalPrice=0;
      var creditTotalPrice=0;
      var debitList =[];
      var creditList =[];
      var codeList = [];

      function Summary(accountName , price){
         this.accountName = accountName;
         this.price = price;
      }
      $.each($("#journalGrid").jqGrid("getDataIDs"),function(index,value){
         codeList.push(jQuery("#journalGrid").getRowData(index+1));
         });

      $.each(codeList, function(index, element) {
         var summary = new Summary(element.accountName, element.price);

         if(element.balanceDivision == "차변") {
            debitTotalPrice += parseInt(element.price);
            debitList.push(summary);
         } else if(element.balanceDivision == "대변") {
            creditTotalPrice += parseInt(element.price);
            creditList.push(summary);
         }
      });

      showDebitGrid(debitTotalPrice,debitList);
      showCreditGrid(creditTotalPrice,creditList);

   }


   /*
      대변 차변 목록
   */

   //차변 그리드
   function showDebitGrid(debitTotalPrice, debitList) {

       $.jgrid.gridUnload("#debitGrid");
       $("#debitGrid").jqGrid({
           datatype: "local",
           data: debitList,
           colNames: ["계정과목", "금액"],
           colModel: [
               {name: "accountName", editable: false, align: "center"},
               {name: "price", editable: false, align: "right", formatter:'currency', formatoptions:{thousandsSeparator:",", decimalPlaces:0}},
           ],
           width : 550,
           height : 130,
           caption : "차변",
           footerrow : true
       });
       $("#debitGrid").jqGrid("footerData", "set", {accountName: "합계", price: debitTotalPrice});
   }




   // 대변 그리드
   function showCreditGrid(creditTotalPrice, codeList) {

       $.jgrid.gridUnload("#creditGrid");
       $("#creditGrid").jqGrid({
           datatype: "local",
           data: codeList,
           colNames: ["계정과목", "금액"],
           colModel: [
               {name: "accountName", editable: false, align: "center"},
               {name: "price", editable: false, align: "right", formatter:'currency', formatoptions:{thousandsSeparator:",", decimalPlaces:0}},
           ],
           width : 550,
           height : 130,
           caption : "대변",
           footerrow : true
       });
       $("#creditGrid").jqGrid("footerData", "set", {accountName: "합계", price: creditTotalPrice});
}


   /*
         계정과목 보기
   */
   function showAccountCodeDialog(grid,rowid,iCol){

   $("#codeDialog").dialog({
         title : "계정 과목 리스트",
         "width":"650",
         "height":"550",
           open: function(event, ui) {
           $(".ui-dialog-titlebar-close", $(this).parent()).show();
            }
          });
     $.jgrid.gridUnload("#codeGrid");
     $("#codeGrid").jqGrid({
         url:"${pageContext.request.contextPath}/account/account.do?method=findAccountList&type=slip",
         datatype: "json",
         jsonReader: { root:"accountList"},
         colNames:["계정코드", "계정과목명"],
          colModel:[
              {name:"accountCode",width:200, align :"center",editable:false},
               {name:"accountName",width:200, align :"center", editable:false},
           ],
             width: 605,
             height: 400,
             caption: "계정 과목 선택",
             align: "center",
             viewrecords:true,
             rownumbers: true,
             rowNum:300,
             onSelectRow: function(id) {

                 var detailCode=$("#codeGrid").getCell(id, 1);
                 var detailName=$("#codeGrid").getCell(id, 2);

                   $(grid).setCell(rowid, iCol, detailCode);
                   $(grid).setCell(rowid, iCol+1, detailName);

               $("#codeDialog").dialog("close");
         }
      });
   }
   /*
      대차 차액 계산
   */
   function balanceDiffCalculator(slipId){

      var balance = 0;
       var rows = $("#journalGrid").getDataIDs();
       for(index = 0; index < rows.length; index++) {
          var data = $("#journalGrid").getRowData(rows[index]);
          if(data.balanceDivision == "차변")
              balance += parseInt(data.price);
          else
              balance -= parseInt(data.price);
       }
       //총합계산을 전표쪽에 갱신
       $("#slipGrid").setCell(slipId, "balanceDiff", balance);
       // 상태 변경
       var status = $("#slipGrid").getCell(slipId, "status");
       if(status == "select") $("#slipGrid").setCell(slipId, "status", "update");
   }

   /*
         코드 상세 보기
   */

   function showCodeDialog(grid, rowid, iCol, detailCodeNo, detailCodeName){
      $("#codeDialog").dialog({
            title : detailCodeName ,
            "width":"650",
            "height":"550",
            open: function(event, ui) {
              $(".ui-dialog-titlebar-close", $(this).parent()).show();  } /*닫기 버튼 제거.. */
      });
        $.jgrid.gridUnload("#codeGrid");
        $("#codeGrid").jqGrid({
            url:"${pageContext.request.contextPath}/base/codeList.do?method=findDetailCodeList&code="+detailCodeNo,
            datatype: "json",
            jsonReader: { root:"detailCodeList"},
            colNames:['상세코드번호','상세코드이름','사용여부'],
             colModel:[
                 {name:'codeNo',width:200, align :"center",editable:false},
                  {name:'codeName',width:200, align :"center", editable:false},
                 {name:'codeUseCheck',width:200, align :"center",editable:false},
              ],
                width: 605,
                height: 400,
                caption: detailCodeName,
                align: "center",
                viewrecords:true,
                rownumbers: true,
                onSelectRow: function(id) {

                    var detailCode=$("#codeGrid").getCell(id, 1);
                    var detailName=$("#codeGrid").getCell(id, 2);
                 if(rowid ==null && iCol ==null){
                     $("#"+detailCodeNo).val(detailName);
                 }
                    $(grid).setCell(rowid, iCol, detailCode);
                    if(detailCode !="DEC03"){
                       $(grid).setCell(rowid, iCol+1, detailName);
                    }else{
                       $("#journalGrid").jqGrid('setCell',rowid,'descName, ""', 'editable-cell');
                    }


               $("#codeDialog").dialog("close");
              }
            });
   }

   /*
      에러 다이얼로그
   */
   function errorCheckDialog(errorMsg,alerts){

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
               $(location).attr("href","${pageContext.request.contextPath}/account/slipform.html");
                $(this).dialog("close");
               }
              }
             });
          });
      }

      if(alerts == "slipDelete"){
         $("#errorDialog").html(errorMsg);
            $(function(){
             $("#errorDialog").dialog({
             title : "경고 메세지" ,
             modal : true,
              width : 500,
              heigth : 500,
              buttons : {
               ok : function(){
               var journalList = slipList[slipRow-1].journalList;
                for(var index = 0 ; index < journalList.length ; index++ ){
                  $("#journalGrid").setCell(index+1, "status", "delete");
                }
                $(this).dialog("close");
               },
                close : function(){
                $("#slipGrid").jqGrid('setRowData',slipRow,{status:"select"});
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
 <table id="layoutSlipForm">
   <tr>
      <td colspan="2">
        <!-- 전표 -->
        <!-- 달력 --><input type="text" id="slipDate" >
        <input type="button" id="slipSearchButton" value="전표조회">
        
        <input type="button" id="reStartButton" value ="다른 날짜 선택 ">

		<input type="button" id="slipInsertButton" value ="전표추가">
        <input type="button" id="slipUpdateButton" value="전표수정">
        <input type="button" id="slipDeleteButton" value="전표삭제">
        <input type="button" id="batchSaveButton" value="일괄저장">
        <input type="button" id="pdfOpenButton" value="PDF보기">
      
        <table id="slipGrid"></table>
        </td>
    </tr>
    <tr>
       <td colspan="2">
          <!-- 분개 -->
         <input type="button" id="journalInsertButton" value = "분개추가">
         <input type="button" id="journalUpdateButton" value = "분개수정">
         <input type="button" id="journalDeleteButton" value = "분개삭제">
         <table id="journalGrid" style="z-index:3"></table>
        </td>
   </tr>
   <tr>
      <td colspan="2">
          <!-- 분개상세 -->
          <div id = "journalDetailList"></div>
        </td>
    </tr>
    <tr>
        <!-- 대차대조표 -->
        <td>
            <table id="debitGrid"></table>
        </td>
        <td>
            <table id="creditGrid"></table>
        </td>
    </tr>
    </table>
<div id="errorDialog"></div>
<div id="codeDialog" title="Dialog Title">
<table id="codeGrid"></table>
</div>
</body>
</html>