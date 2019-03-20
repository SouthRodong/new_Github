<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/scripts/menu/menu.css" />
<script 	src="${pageContext.request.contextPath}/scripts/menu/menu.js"></script>

<style>
	.ui-jqgrid .ui-jqgrid-hdiv {
		font-size: 0.8em;
		height: 35px;
	}
	.ui-jqgrid .ui-widget-header {
		height: 37px;
		font-size: 0.9em;
	}
	.ui-jqgrid .ui-jqgrid-bdiv {
		overflow-x: auto;
		overflow-y: scroll;
	}


h1{
	text-shadow: 2px 2px 2px #FFB2F5;
	color: #F361A6;
	font-weight: bold;
}

h5 {
	text-shadow: 5px 5px 2px #4C4C4C; 
	color : #ffffff;
	font-weight: bold;
}




</style>
<script>

function alertError(title, message) {
	
	// error-dialog 보이게 하기
	$("#error-dialog").attr("style", "display:block");

	$("#error-dialog").dialog({   // jqueryUI dialog 위젯 적용
		autoOpen : true,  // 자동으로 열리도록
		modal : true,     // 외부 클릭 못하게
		title : title,   // error-dialog 폼 제목
		width : 'auto',
		height : 'auto',
		position : {    // 폼 열릴 때 위치
			my : "center center",  
			at : "center-120 center-30"   // 폼 열릴 때, 대강 화면 중앙에 오도록
		},
		buttons : {  // 버튼 이벤트 적용
			"확인" : function() {
				$("#error-dialog").attr("style", "display:none");
				$("#error-dialog").dialog("close");
			}
		}
	});
	$("#error-dialog #errorMsg").html(message);

}

	var userMenuList;
	
	function setUserMenuList(){
		
		$.ajax({
			 url:"${pageContext.request.contextPath}/base/menuList.do",  
	         data:{
	            method:"findUserMenuList",   
	            empCode:"${sessionScope.empCode }"
	         },
	         dataType:"json",
	         success : function(data){
	      	   if(data.errorCode<0){
	      		   errorDialog(data.errorMsg,"warning");     		   
	      	   }else{
	            userMenuList = data.userMenuList;
	            
	      	   }
	         }
	      });
		}

	function permitted(menuCode,url){

		   alert(menuCode);	  
		   var permitted = false;    
		   
		   for(var index in userMenuList){
			  
		      if(userMenuList[index].menuCode==menuCode){
		    	  
		         var permitted = true;   
		      }
		   }
		   if(permitted)
			  
			 location.href=url;
		  	
		   else{
			  //alert("실패 실행");
		      errorDialog("해당 권한이 없습니다.\n관리자에게 문의하세요","warning");
		   }
		}


	function errorDialog(errorMsg,alerts){ 
		if(alerts == "warning"){
	     $("#errorDialog").html(errorMsg);
	        $(function(){
	         $("#errorDialog").dialog({
	         title : "경고 메세지" ,
	         modal : true,
	          width : 500,
	          heigth : 500,
	          buttons : {
	           	닫기 : function(){
	            $(this).dialog("close");
	           }
	          }
	         
	         });
	      });
		}
	}

	
		$(document).ready(function(){
		$("#main").click(function(){ location.href="<%=request.getContextPath()%>/hello.html"; });
		$("#logout").click(function(){ location.href="<%=request.getContextPath()%>/logout.do"; });
		
		setUserMenuList();
		
		$("button").button();
		
		});

		
</script>
<body>

<h1 align="center">
<img src="${pageContext.request.contextPath}/resources/image/yumyum.gif">
</h1>

<h5 align="center">
	권한 : ${sessionScope.authority} &emsp;
	사원번호 : ${sessionScope.empCode} &emsp;
	직급 : ${positionCode} &emsp;
	사원이름 : ${sessionScope.empName}님 &emsp; &emsp;
	<button id="logout" class='button'>로그아웃</button> &emsp; &emsp;
</h5>


<div id="cssmenu" align="center">
   <ul>
   
     <li class='active'><a href='${pageContext.request.contextPath}/hello.html'>Home</a></li>
     
     
     <li class='has-sub'><a href='#'><span>인사 등록 관리</span></a>
       <ul> 
           
           <li onclick="permitted('HRS00','${pageContext.request.contextPath}/hr/empinsertform.html')">
           <a href='#'><span>사원 등록</span></a>
           </li>
				
           <li onclick="permitted('HRS00','${pageContext.request.contextPath}/hr/employeeform.html')">
           <a href='#'><span>사용자 권한 설정</span></a>
           </li>
           
           </ul>
           </li>
           
        <li class='has-sub'><a href='#'><span>계정/코드 관리</span></a>
          <ul>
          
           <li onclick="permitted('ACS00','${pageContext.request.contextPath}/account/accountform.html')">
           <a href='#'><span>계정과목</span></a>
           </li>
           
           <li onclick="permitted('SYS00','${pageContext.request.contextPath}/base/codemanageform.html')">
           <a href='#'><span>코드 관리</span></a>
           </li>
           
          </ul>
          </li>
        
      <li class='has-sub'><a href="#"><span>전표 관리</span></a>
         <ul>
          <li onclick="permitted('ACS00','${pageContext.request.contextPath}/account/slipform.html')">
          <a href='#'><span>일반전표입력</span></a>
          </li>
          <li onclick="permitted('SYS00','${pageContext.request.contextPath}/base/approvalmanager.html')">
          <a href='#'><span>전표승인/해제</span></a></li>
         </ul>
         </li>
      <li class='has-sub'><a href="#"><span>장부 관리</span></a>
         <ul>
            <li onclick="permitted('ACS00','${pageContext.request.contextPath}/account/journalform.html')">
            <a href='#'><span>분개장</span></a>
            </li>
            
         </ul>
         
         <li class='has-sub'><a href='#'><span>재무 재표 관리</span></a>
          <ul> 
          <li onclick="permitted('ACS00','${pageContext.request.contextPath}/statement/totaltrialbalanceform.html')">
           <a href='#'><span>합계잔액시산표</span></a>
           </li>
           <li onclick="permitted('ACS00','${pageContext.request.contextPath}/statement/incomeStatementform.html')">
           <a href='#'><span>손익계산서</span></a>
           </li> 
           </ul>
          
         </li>
   </ul>
</div>
<div id="error-dialog">
	<p id="errorMsg"></p>
</div> 
</body>