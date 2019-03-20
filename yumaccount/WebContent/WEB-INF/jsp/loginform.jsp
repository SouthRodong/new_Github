<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>YUM account</title> 
<!-- 제이쿼리,제이쿼리 UI , jqGrid 사용위해 링크검 -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/jqueryUI/jquery-ui.min.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/jqGrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/jqGrid/plugins/ui.multiselect.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/jqGrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/jqGrid/plugins/ui.multiselect.css" />

<script 	src="${pageContext.request.contextPath}/scripts/jquery/jquery-3.3.1.min.js"></script>
<script 	src="${pageContext.request.contextPath}/scripts/jqueryUI/jquery-ui.min.js"></script>
<script 	src="${pageContext.request.contextPath}/scripts/jqGrid/js/jquery.jqGrid.min.js"></script>
<script 	src="${pageContext.request.contextPath}/scripts/jqGrid/js/i18n/grid.locale-kr.js"></script>


<script>

var code;  
var codeType; 
var codeName;
var employeeBean; 

$(document).ready(function(){
	
	$("input[type=button], input[type=submit] , input[type=reset] ,input[type=text] ,input[type=password]").button();  // jquery UI를 지정하기 위함
	
	
	$("#deptCode").click({   /* 부서코드를 누르면 showDialog 이벤트 발생 */
	      code : "C002",
	      codeName : "부서선택",
	      type : "deptCode"
	   }, 
	   showDialog
	   );  
	
	
	/* attr(): 속성의 값을 얻을때
	attr(name,val) : 속성에 값을 세팅할때 */
	
	$("#confirmtable").attr("style","display:none");     /*  confirmtable의 style 속성을 display:none으로 세팅함 */

	if(<%=request.getAttribute("errorCode")%> < 0){
		$("#confirmtable").attr("style","display:block");
	}else{
		$("#confirmtable").attr("style","display:none");
	}
	<%session.invalidate();%>   //세션 완전히 삭제
	
});

function showDialog(event) {
	   /* click이벤트가 발생했을 때 넘어오는 parameter값을 변수에 저장한다 */
	   code = event.data.code;      
	   codeName = event.data.codeName;
	   codeType = event.data.type;

	   /* dialog를 설정하는 부분 */
	   $("<div>", {
	      "id" : "codeDialog",
	      "title" : codeType
	   }).appendTo("body");

	   $("#codeDialog").dialog({
	      "width" : "550",
	      "height" : "420"
	   });

	   /* dialog의 내용으로 띄울 grid 설정하는 부분 */
	   $("<table>", {
	      "id" : "codeList"
	   }).appendTo("#codeDialog");
	   
	   $.jgrid.gridUnload("#codeList");
	   /* 해당하는 code를 넘겨줘서 상세코드값을 받아온다 */
	   $("#codeList").jqGrid({
	      url : "${pageContext.request.contextPath}/base/codeList.do",
	      postData : {
	         "method" : "findDetailCodeList",
	         "code" : code
	      },
	      datatype : 'json',
	      jsonReader : {
	         root : 'detailCodeList'
	      },
	      colNames : [ '코드상세번호', '코드상세이름' ],
	      colModel : [ {
	         name : 'codeNo',
	         width : 200,
	         editable : false
	      }, {
	         name : 'codeName',
	         width : 200,
	         editable : false
	      }, ],
	      width : 500,
	      height : 400,
	      viewrecords : true,
	      rownumbers : true,
	      onSelectRow : function(rowid) {
	         /* codeList의 row를 선택할때마다 선택된 row의 상세코드값이 변수에 저장된다. */
	         var codeNo = $("#codeList").jqGrid("getRowData", rowid).codeNo;
	         /* 동적으로 부서dialog인지 권한 dialog인지 따라 상세코드가 부서입력란 또는 권한 입력란에 들어간다. */
	         $("#" + codeType).val(codeNo);
	         $("#codeDialog").remove();
	         $("#codeDialog").dialog("close");
	      }
	   });
	}

</script>

<style type="text/css">

input[type=text], input[type=password] {
	display: inline;
	width: 50%;
	padding-left: 1%;
	margin-bottom: 10px;
	transition: 0.6s;
	outline: none;
	height: 30px;
}

div {
	text-align: center;
	overflow: hidden;
	height: auto;
	border: 2px outset lightgray;
	border-radius: 55px;
}
#outdiv {
	/* margin: 10px; */
	padding: 10px;
	position:absolute; 
	top:20%; left:60%; 
	width:400px; 
	height:500px;
	border: 3px outset lightgray;
	
}
#backimg {
	
	position: absolute;
	top: 125px; left: 75px;
	border: 0px solid gray;
	width: 900px; height: 700px;
} 
body {
	background-image: url('${pageContext.request.contextPath}/resources/image/back.jpg');
	background-size: cover; 
	
}	
h1{
	color: #FF00DD;
	text-shadow: 2px 3px 5px #FFB2F5;
}

 h3{
 	color:white;
 	text-shadow: 2px 3px 5px gray;
 }

label {
	color : #474747;
}


#confirmtable {	
	color: #FF00DD ; border:0px;
	text-shadow: 1px 1px 1px #F361A6;
}


</style>



</head>
<body>
	<img id=backimg src="${pageContext.request.contextPath}/resources/image/Yum1.gif">
	<div id="outdiv">

		<h1>YUM ACCOUNT</h1>
		<h3>[LOGIN]</h3><br/>
		<form method="post"
			action="${pageContext.request.contextPath}/login.do">
		<input type="text" placeholder="부서코드" name="deptCode" id="deptCode"><br/><br/> 
		<input type="text" placeholder="사원코드" title="사원코드를 입력해주세요" name="empCode" /><br/><br/>
	   <input type="password" placeholder="비밀번호"  title="비밀번호를 입력해주세요"  name="empPassword" /><br/><br/> 
			&nbsp;&nbsp;&nbsp;<input type="reset"  value="취소" />
			<input type="submit" value="로그인" />
		</form>
	<div id="confirmtable">
		<p>로그인 실패 했습니다</p>
		${errorMsg}
	</div>
	</div>
	
</body>
</html>