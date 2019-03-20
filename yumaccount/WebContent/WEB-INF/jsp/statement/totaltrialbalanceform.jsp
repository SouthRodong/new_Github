<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>합계잔액시산표</title>

<script>

var trialBalanceList=[]; //담아둘 배열

$(document).ready(function(){

	$("input[type=text] , input[type=button]").button();
	
	$.datepicker.setDefaults({
	    closeText:"닫기",
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
		yearSuffix : '년'
	}); /* 한글로 달력 보이기 위해서 기본값 설정 */
	
	$("font").css({
		color : "white"
	});

	$( "#from" ).datepicker({
	    defaultDate: "-1w",
	    changeYear:true,
	    changeMonth: true,
	    numberOfMonths: 1,
	    dateFormat : "yy-mm-dd",
	    onClose: function( selectedDate ) {
	      $( "#to" ).datepicker( "option", "minDate", selectedDate );  // 선택한 날짜로 다른 datepicker 최소 선택 가능 날짜 설정
	    }
	});
	$( "#to" ).datepicker({
	    defaultDate: "+1w",
	    changeYear: true,
	    changeMonth: true,
	    numberOfMonths: 1,
	    dateFormat : "yy-mm-dd",
	    onClose: function( selectedDate ) {
	      $( "#from" ).datepicker( "option", "maxDate", selectedDate );
	    }
	});
	/*
		버튼 이벤트
	*/
	$("#trialBalanceBtn").click(trialBalanceFunc);   // 합계잔액시산표보기를 누르면 trialBalanceFunc 함수 발생

	trialBalanceGrid(); 
});

function trialBalanceFunc() {  //db 가는 함수 
	
    $.ajax({
        url: "${pageContext.request.contextPath}/statement/trialBalanceList.do",
        type: "post",
        data: {from: $("#from").val(), to: $("#to").val()},      // 날짜 전달
        dataType: "json",
        success: function(data, textStatus, jqXHR) {
            if(data.trialBalanceList.length == 0) {            	
              	//alert(data.errorMsg);
              	trialBalanceErrorDialog("조회된 데이터가 없습니다.","warning")
              	trialBalanceList= data.trialBalanceList;    // db에서 받아온 trialBalanceList 할당
               	parseTrialBalanceData();   // 합계잔액시산표 분석함수 호출
            } else {
            	//alert(data.errorMsg);
            	//alert(data.trialBalanceList.length);
            	trialBalanceErrorDialog("조회 성공 입니다","success")
            	
               	trialBalanceList= data.trialBalanceList; 
               	parseTrialBalanceData(); 

            }
        },
        error: function(jqXHR, textStatus, error) {
        	trialBalanceErrorDialog("조회 오류 입니다","warning")
        }
    });
}


function mergeObjectProp(obj1,obj2){  
    var obj3 = {};
    for (var attrname in obj1) { obj3[attrname] = obj1[attrname]; }   // attrname은 인덱스임
    for (var attrname in obj2) { obj3[attrname] = obj2[attrname]; }
    return obj3; 
}


function parseTrialBalanceData(){
	var balanceList=[]; 

	$.each(trialBalanceList, function(index, element) { 
		
		
	    var tempTrialBalance = element;  
        var trialBalance = mergeObjectProp(element, tempTrialBalance);
        
        var totalDeb = parseInt(trialBalance.totalDeb);    // 총 차변의 합
        var totalCre = parseInt(trialBalance.totalCre);      // 총 대변의 합

        if(totalDeb > totalCre) { 
        	trialBalance.DebitBalancePrice = totalDeb - totalCre; 
        }
        if(totalCre > totalDeb ) {	
        	trialBalance.CreditBalancePrice =  totalCre - totalDeb;	
        }
		
        
        if(trialBalance.level==1){
            trialBalance.accountName='<h1>①['+(trialBalance.accountName)+']</h1>'; /*1계층*/
        }else if(trialBalance.level==2){
            trialBalance.accountName='<h2>②{'+(trialBalance.accountName)+'}</h2>'; /*2계층*/
        }else if(trialBalance.level==3){
        	if(trialBalance.leafs==1){
        		
           		trialBalance.accountName='<h3>③('+(trialBalance.accountName)+')</h3>';
         	}else{
         		trialBalance.accountName='<h3>③('+(trialBalance.accountName)+')</h3>';	/*3계층*/        		
         	}
        }else{
            trialBalance.accountName='<h4>④-'+(trialBalance.accountName)+'-</h4>'; /*4계층*/
        }
        
        balanceList.push(trialBalance); 
	
	});
		showBalanceSheet(balanceList); 
}

function showBalanceSheet(balanceList){

var debitTotalPrice=0;
var creditTotalPrice=0;
var debitTotalBalance=0; 
var creditTotalBalance=0; 

$.each(balanceList, function(index, element) {

	if(element.level == "1" && element.totalDeb != 0 ) { 
		debitTotalPrice += parseInt(element.totalDeb);
	}
	if(element.level == "1" && element.totalCre != 0) {
		creditTotalPrice += parseInt(element.totalCre);
	}
	if(element.level == "1" && element.DebitBalancePrice !=undefined){ 
		debitTotalBalance += parseInt(element.DebitBalancePrice);
	}
	if(element.level == "1" && element.CreditBalancePrice !=undefined){
		creditTotalBalance += parseInt(element.CreditBalancePrice);
	}
});
trialBalanceGrid(debitTotalPrice,creditTotalPrice,debitTotalBalance,creditTotalBalance,balanceList);
}

/* 합계 시산표 보기 */
function trialBalanceGrid(deToPr,creToPr,deToBa,creToBa ,balanceList) {

    $.jgrid.gridUnload("#trialBalanceGrid");    /* 데이터 날려버리고 다시 그려주는 방식 */

    console.log(balanceList); 

    $("#trialBalanceGrid").jqGrid({
    	data: balanceList,
        datatype: "local",
        colNames: [ "잔액","합계","계정코드","계정과목","합계","잔액","레벨"],
        colModel: [
            {name: "DebitBalancePrice", width: 30, align: "center", editable: true, formatter:'currency', formatoptions:{thousandsSeparator:",", decimalPlaces:0}},
            {name: "totalDeb", width: 30, editable: false, align: "center", formatter:'currency', formatoptions:{thousandsSeparator:",", decimalPlaces:0} },
            {name: "accountCode" , hidden: true, width: 20, align: "center", editable: false},  //계정코드는 숨김
            {name: "accountName", width: 30, editable: false, align: "center"},
            {name: "totalCre", width: 30, editable: false,align: "center", formatter:'currency', formatoptions:{thousandsSeparator:",", decimalPlaces:0}},
            {name: "CreditBalancePrice", width: 30, align: "center", editable: true, formatter:'currency', formatoptions:{thousandsSeparator:",", decimalPlaces:0}},
    		{name: "level" , width: 5, align: "center", editable: true }
        ],
        caption: "합계 잔액 시산표",
        width: 1050,
        height: 'auto',  //값에 따라 자동으로 늘어나게
        footerrow : true,
        hoverrows : false,
        loadComplete : function() {	//계정과목 레벨별로 bg색상 변경
            var ids = $("#trialBalanceGrid").getDataIDs() ;
            $.each( ids, function(idx, rowId) {
                 rowData = $("#trialBalanceGrid").getRowData(rowId) ;
                 if ( rowData.level == 1 ){
                     $("#trialBalanceGrid").setRowData(rowId, false, {background:"#99004C"});
                 }else if(rowData.level == 2){
                     $("#trialBalanceGrid").setRowData(rowId, false, {background:"#D9418C"});
                 }else if(rowData.level == 3){
                	 $("#trialBalanceGrid").setRowData(rowId, false, {background:"#F361A6"});
                 }else if(rowData.level == 4){
                     $("#trialBalanceGrid").setRowData(rowId, false, {background:"#FFB2D9"});
                 }
            }) ;             
       }

    });
    
    $("#trialBalanceGrid").jqGrid("footerData", "set", {DebitBalancePrice : deToBa , totalDeb : deToPr , accountName : "<h2>합계</h2>", totalCre : creToPr , CreditBalancePrice : creToBa, formatter:'currency', formatoptions:{thousandsSeparator:",", decimalPlaces:0}});
	
    $("#trialBalanceGrid").setGroupHeaders({ useColSpanStyle: true,
    	groupHeaders: [ {startColumnName: "DebitBalancePrice", numberOfColumns: 2, titleText: "<em>차변</em>"},
    	                {startColumnName: "totalCre", numberOfColumns: 2, titleText: "<em>대변</em>"} ]
    });
    
}
/* 에러 다이얼로그 */
 	function trialBalanceErrorDialog(errorMsg,alerts){

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
	<input type="text" id="from" name="from">&nbsp;&nbsp;&nbsp;<B><font>부터</font></B>&nbsp;&nbsp;&nbsp;
    <input type="text" id="to" name="to">&nbsp;&nbsp;&nbsp;<B><font>까지</font></B>&nbsp;&nbsp;&nbsp;
    <input type="button" id="trialBalanceBtn" value="합계잔액시산표 보기"><br><br>
    <table id="trialBalanceGrid"></table>
</body>
</html>