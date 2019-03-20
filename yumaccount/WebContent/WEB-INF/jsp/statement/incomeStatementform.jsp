<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>손익계산서</title>  
<script >
var incomeStatementList=[];     //담아둘 배열

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
	
	$("font").css("color" ,"white");

	$( "#from" ).datepicker({
	    defaultDate: "-1w",
	    changeYear: true,
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


	$("#IncomeStateBtn").click(IncomeStateFunc);   // 손익계산서  버튼을 누르면 StatementPositionFunc 함수 발생

	IncomeStatementGrid(); 
});



function IncomeStateFunc() {  //db 가는 함수 

    $.ajax({
        url: "${pageContext.request.contextPath}/statement/incomeStatement.do",  
        type: "post",
        data: {from: $("#from").val(), to: $("#to").val()},      // 날짜 전달
        dataType: "json",
        success: function(data, textStatus, jqXHR) {
            if(data.incomeStatementList.length == 0) {            	
              	//alert(data.errorMsg);
             IncomeStateErrorDialog("조회된 데이터가 없습니다.","warning")
              	incomeStatementList= data.incomeStatementList;    // db에서 받아온 incomeStatementList 할당
               	parseincomeStateData();   // 손익계산서 분석함수 호출
            } else {
            	//alert(data.errorMsg);
            	/* alert(data.incomeStatementList); */
            	IncomeStateErrorDialog("조회 성공 입니다","success")
            	
               	incomeStatementList= data.incomeStatementList; 
            	parseincomeStateData(); 

            }
        },
        error: function(jqXHR, textStatus, error) {
        	IncomeStateErrorDialog("조회 오류 입니다","warning")
        }
    });
}





function mergeObjectProp(obj1,obj2){  
    var obj3 = {};
    for (var attrname in obj1) { obj3[attrname] = obj1[attrname]; }   // attrname은 인덱스임
    console.log(obj3);
    for (var attrname in obj2) { obj3[attrname] = obj2[attrname]; }
    return obj3; 
}


function parseincomeStateData(){
	var incomeStateList=[]; 
	
	$.each(incomeStatementList, function(index, element) { 
		
	    var tempincomeStatement = element;  
        var incomeStatement = mergeObjectProp(element, tempincomeStatement);
        
        var totalDeb = parseInt(incomeStatement.totalDeb);    // 총 차변의 합
        var totalCre = parseInt(incomeStatement.totalCre);      // 총 대변의 합

        if(totalDeb > totalCre) { 
        	incomeStatement.DebitBalancePrice = totalDeb - totalCre; 
        }
        if(totalCre > totalDeb ) {	
        	incomeStatement.CreditBalancePrice =  totalCre - totalDeb;	
        }
        

       
        if(incomeStatement.level==1){
        	incomeStatement.accountName='<h1>①['+(incomeStatement.accountName)+']</h1>'; /*1계층*/
        }else if(incomeStatement.level==2){
        	incomeStatement.accountName='<h2>②{'+(incomeStatement.accountName)+'}</h2>'; /*2계층*/
        }else if(incomeStatement.level==3){
        	if(incomeStatement.leafs==1){
        		
        		incomeStatement.accountName='<h3>③('+(incomeStatement.accountName)+')</h3>';
         	}else{
         		incomeStatement.accountName='<h3>③('+(incomeStatement.accountName)+')</h3>';	/*3계층*/        		
         	}
        }else{
        	incomeStatement.accountName='<h4>④-'+(incomeStatement.accountName)+'-</h4>'; /*4계층*/
        }
        
        
        
        
        incomeStateList.push(incomeStatement); 
	
	});
		showIncomeStatemente(incomeStateList); 
}





function showIncomeStatemente(incomeStateList){
	
	
	var debitTotalPrice=0;
	var creditTotalPrice=0;
	var debitTotalBalance=0; 
	var creditTotalBalance=0; 
    var totalprofit=0;   // 총 수익
    var totalexpense=0;  // 총 비용
	var netprofit=0;   //순이익
    
	$.each(incomeStatementList, function(index, element) {

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
		
		totalprofit=AddComma(creditTotalPrice);
		totalexpense=AddComma(debitTotalPrice);
		netprofit=AddComma(creditTotalPrice-debitTotalPrice);
		
	});


      
	IncomeStatementGrid( debitTotalBalance,creditTotalBalance,totalexpense,totalprofit,netprofit, incomeStateList);
	}


/* 손익계산서 그리드 띄우기 */
function IncomeStatementGrid(debitTotalBalance,creditTotalBalance,totalexpense,totalprofit,netprofit, incomeStateList){
     

	if(totalexpense==undefined || totalprofit==undefined || netprofit==undefined ){
		totalexpense=" ";
		totalprofit=" ";
		netprofit=" ";
		}
	
	
	
	   	   
	   $.jgrid.gridUnload("#IncomeStatementGrid");    /* 데이터 날려버리고 다시 그려주는 방식 */

	    $("#IncomeStatementGrid").jqGrid({
	    	data: incomeStateList,
	        datatype: "local",
	        colNames:["계정코드","계정과목","차변금액","대변금액","레벨"] , 
	        colModel:[
	        	{name: "accountCode",hidden:true,width:20,align:"center",editable:false },
	        	{name: "accountName", width: 30, editable: false, align: "center"},
	        	{name: "DebitBalancePrice", width: 30, align: "center", editable: true, formatter:'currency', formatoptions:{thousandsSeparator:",", decimalPlaces:0}},
	        	{name: "CreditBalancePrice", width: 30, align: "center", editable: true, formatter:'currency', formatoptions:{thousandsSeparator:",", decimalPlaces:0}},
	        	{name: "level" , width: 5, align: "center", editable: true }
	        ],
	        caption: "손익계산서",
	        width: 1050,
	        height: 'auto',  //값에 따라 자동으로 늘어나게
	        footerrow : true,
	        hoverrows : false,
	        loadComplete : function() {	// 그리드가 모두 로드된후  레벨별로 bg색상 변경
	            var ids = $("#IncomeStatementGrid").getDataIDs() ;
	            $.each( ids, function(idx, rowId) {
	                 rowData = $("#IncomeStatementGrid").getRowData(rowId) ;
	                 if ( rowData.level == 1 ){
	                     $("#IncomeStatementGrid").setRowData(rowId, false, {background:"#99004C"});
	                 }else if(rowData.level == 2){
	                     $("#IncomeStatementGrid").setRowData(rowId, false, {background:"#D9418C"});
	                 }else if(rowData.level == 3){
	                	 $("#IncomeStatementGrid").setRowData(rowId, false, {background:"#F361A6"});
	                 }else if(rowData.level == 4){
	                     $("#IncomeStatementGrid").setRowData(rowId, false, {background:"#FFB2D9"});
	                 }
	            }) ;                   
	       }

	    });
	   
	  
	   
	    $("#IncomeStatementGrid").jqGrid(
	    		"footerData", "set",
	    		{accountName:"<h3>총 수익:  </h3>"+totalprofit+"<h3>총 비용:  </h3>"+totalexpense+"<h3>순이익:  </h3>"+netprofit }
	    );
	    
	    
        $('table.ui-jqgrid-ftable   td:eq(2)').hide();
        $('table.ui-jqgrid-ftable   td:eq(3)').hide();
        $('table.ui-jqgrid-ftable   td:eq(4)').hide();

	    
	    $("#IncomeStatementGrid").setGroupHeaders({ useColSpanStyle: true,
	    	groupHeaders: [ {startColumnName: "DebitBalancePrice", numberOfColumns: 2, titleText: "<b>금액</b>"} ] 
	    });
	   
   }
   


function AddComma(data_value) {

	return Number(data_value).toLocaleString('en');

}
   
   
   

   /*  에러 다이얼로그 */
	function IncomeStateErrorDialog(errorMsg,alerts){  

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
    <input type="button" id="IncomeStateBtn" value="손익계산서 보기"><br><br>
    <table id="IncomeStatementGrid"></table> 
</body>
</html>