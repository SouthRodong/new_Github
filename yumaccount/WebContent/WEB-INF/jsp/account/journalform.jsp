<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>분개장</title>
<script>

var slipList=[]; 
var journalList=[]; 


	$(document).ready(function(){
		$("input[type=text], input[type=button]").button(); 
		
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
			yearSuffix : '년'
		}); /*한글로 달력 보이기 위해서 */
		
		$( "#from" ).datepicker({			  
	          defaultDate: "-1w", 
	          changeMonth: true,
	          changeYear: true,
	          numberOfMonths: 1, 
	          onClose: function( selectedDate ) { 
	            $( "#to" ).datepicker( "option", "minDate", selectedDate );
	          }	  
	      });
		
		$( "#to" ).datepicker({
	          defaultDate: "+1w",
	          changeMonth: true,
	          changeYear: true,
	          numberOfMonths: 1,
	          onClose: function( selectedDate ) {
	            $( "#from" ).datepicker( "option", "maxDate", selectedDate );
	          }	  
	      });
		

		  /*  버튼 이벤트     */
		      $("#journalSearch").click(journalSearchFunc);

		      showJournalGrid();
	});
	
	/*기간별 승인 완료 된 분개 찾기!*/
	 function journalSearchFunc() {

	       $.ajax({
	           url: "${pageContext.request.contextPath}/account/slip.do",
	           type: "post",
	           data: {"method" : "findRangedSlipList", from: $("#from").val(), to: $("#to").val()}, /*선택된 날짜를 넘겨줌!!*/
	           dataType: "json",
	           success: function(data, textStatus, jqXHR) {
	        	   
	               if(data.errorCode < 0) {
	                 
	                   msgDialog(data.errorMsg,"warning");
	               } else {	            	               	   
	                   //alert(JSON.stringify(data.slipList));
	                   slipList = data.slipList; 
	                   journalList = []; 
	               	   console.log(slipList);
	                   parseJournalData();
	                   showJournalGrid();
	                   if(data.slipList.length == 0){
	                	 msgDialog("데이터가 없습니다. 기간은 재설정해 주세요","warning");
	                   }else{
	                  	 msgDialog(data.errorMsg,"success");	
	                   }
	               }
	           },
	           error: function(jqXHR, textStatus, error) {
	        	   
	        	   msgDialog("조회 오류 입니다","warning");
	           }
	       });
	   }
	 function parseJournalData() {
	       $.each(slipList, function(index, element) {
	           var slip = element;
				console.log(slip);
				console.log(index);
				console.log(element);
				console.log(element.journalList);
	           $.each(element.journalList, function(index, element) {
	               var journal = mergeObjectProp(element, slip); 
	               delete journal.journalList; 
	               delete journal.journalDetailBean;	

	               if(journal.balanceDivision == "차변") {
	                   journal.debitAccountName = journal.accountName;
	                   journal.debitPrice = journal.price;
	               } else if(journal.balanceDivision == "대변") {
	                   journal.creditAccountName = journal.accountName;
	                   journal.creditPrice = journal.price;
	               }
	               journalList.push(journal);
	           });
	       });
	    
	   }

	   function mergeObjectProp(journalObj,slipObj){ 
	       var resultObj = {};
	       for (var attrname in journalObj) {
	    	   resultObj[attrname] = journalObj[attrname]; // {accountCode: "10106", accountName: "외상매출금", balanceDivision: "차변", customerCode: "CUS01", customerName: "현대건설"…}
	       }
	       for (var attrname in slipObj) {
	    	   resultObj[attrname] = slipObj[attrname]; // {approvalDate: "", approvalEmpCode: "", approvalEmpName: "", approvalSeq: 0, approvalState: ""…}
	       }
	       return resultObj; /* == journal*/
	   }
	 
	 /*
     분개장 보기
  */
  function showJournalGrid() {
      $.jgrid.gridUnload("#journalGrid");
      $("#journalGrid").jqGrid({
          data: journalList,  
          datatype: "local",
          //editurl: "clientArray",
          colNames: [ "결의일자", "전표번호", "전표유형", "계정과목", "금액", "계정과목", "금액", "품의내역", "코드", "거래처명"],
          colModel: [
              {name: "writeDate", width: 100, align: "center", editable: false, edittype: "text"},
              {name: "slipNo", width: 100, editable: false, align: "center"},
              {name: "slipType", width: 70, align: "center", editable: false, edittype: "text"},
              {name: "debitAccountName", width: 150, editable: false},
              {name: "debitPrice", width: 100, align: "center", editable: true, formatter:'currency', formatoptions:{thousandsSeparator:",", decimalPlaces:0}},
              {name: "creditAccountName", width: 150, editable: false},
              {name: "creditPrice", width: 100, align: "center", editable: true, formatter:'currency', formatoptions:{thousandsSeparator:",", decimalPlaces:0}},
              {name: "requestName", width: 200, editable: false,align: "center"},
              {name: "customerCode", width: 70, editable: false,align: "center"},
              {name: "customerName", width: 150, editable: false,align: "center"}

          ],

          caption: "분개장",
          width: 1100,
          height: 'auto',
          sortname : "slipNo",
          sortorder : "desc",
          pager : $('#page'),
          emptyrecords : "레코드가 없습니다.",
          rowNum : 10,
          rowList : [10,20,30],
          viewrecords : true
      });

      $("#journalGrid").setGroupHeaders({ /*setGroupHeaders 셀 병합하기!!*/
          useColSpanStyle: true,
          groupHeaders: [/*startColumnName -> 병합시작할 칼럼명, numberOfColumns -> 병합할 칼럼 갯수, titleText -> 병합될 칼럼명*/
              {startColumnName: "writeDate", numberOfColumns: 2, titleText: "<em>결의구분</em>"},
              {startColumnName: "debitAccountName", numberOfColumns: 2, titleText: "<em>차변</em>"},
              {startColumnName: "creditAccountName", numberOfColumns: 2, titleText: "<em>대변</em>"},
              {startColumnName: "customerCode", numberOfColumns: 2, titleText: "<em>거래처</em>"}
          ],
      });
  }
	 
 	 /*	 에러 다이얼로그  */
	function msgDialog(errorMsg,alerts){

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

	<input type="text" id="from">&nbsp;<font>부터</font>&nbsp;
	<input type="text" id="to">&nbsp;<font>까지</font> &nbsp;&nbsp;&nbsp;
	<input type="button" id="journalSearch" value="분개장 보기"><br><br>
    <table id="journalGrid"></table>
    <div id="page" style="text-align: center;"></div>

</body>
</html>