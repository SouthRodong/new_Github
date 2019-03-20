<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style type="text/css">
 #classificationtitle {
 	width : 100px;
 }
 .form-field-2 {
   border-collapse: collapse;
   padding: 15px;
   
}
.form-container {
   font-family: Consolas;
   text-decoration: none;
   vertical-align: middle;
   min-width: 100px;
   padding: 20px;
   width: 800px;
   height: 750px;
}
.form-field {
   border: 1px solid #EAEAEA;
   background: #ffffff;
   color: #827260;
   padding: 10px;
   width: 255px;
   height: 10px;
}


.form-field:focus {
   background: #F6F6F6;
   color: #0f020f;
}
font{
   	font-size:35px;
   	font-weight:bold;
   }

</style>
<script>
	
/* 이벤트발생시 넘어오는 파라미터를 담기위한 변수 */
var code;  
var codeType; 

var codeName;

var employeeBean; 

$(document).ready(function() {
	$("table").css("color","white");
	$("input[type=text],input[type=password],input[type=button]").button();
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
			yearRange: 'c-99:c+50',
	        changeYear: true,
			yearSuffix : '년'
		}); /*한글로 달력 보이기 위해서 */
	
		
   $("#deptCode").click({
      code : "C002",
      codeName : "부서선택",
      type : "deptCode"
   }, showDialog);  
   $("#positionCode").click({
      code : "C003",
      codeName : "직급선택",
      type : "positionCode"
   }, showDialog);
   $("#authority").click({
      code : "C004",
      codeName : "권한선택",
      type : "authority"
   }, showDialog);
   
   $("#zipCodeList").click(showdialogzipcode);
   
   $("#insertNoBtn").click(insertNoFunc);
   
   $("#insertBtn").click(insertFunc);
   
   $("#enteringDate").datepicker({
       dateFormat : "yy-mm-dd"
    });
   $("#birthday").datepicker({
       dateFormat : "yy-mm-dd"
    });
 
   
   EmptyEmpBean();       
   
});

/*dialog에 새로운 사람을 올린다.*/
 
function EmptyEmpBean(){  
    $.ajax({
           url: "${pageContext.request.contextPath}/hr/hr.do",
           type: "post",
           data: {"method": "EmptyEmpBean"},
           dataType: "json",
           success: function(data, textStatus, jqXHR) {
               if(data.length<=0) {
                   errorDialog("데이터를 가져오는데 실패했습니다","warning");
               } else {
                  employeeBean = data.EmptyEmpBean;
                  
               console.log(JSON.stringify(employeeBean));
               if (employeeBean.imgSrc == null || employeeBean.imgSrc == "")
       			$("#imgSrc")
       					.attr("src",
       							"${pageContext.request.contextPath}/resources/image/base.jpg" );
               }
           }
       });
}


/* 부서번호,권한 입력칸을 선택했을때 띄어지는 dialog */
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

function showdialogzipcode() {
	var option = "width=800px; height=520px; left=400px; top=100px;";
	window.open('${pageContext.request.contextPath}/base/addressform.html',
			"우편번호검색", option);  /*우편번호 검색*/
}

function insertNoFunc(){  /*취소버튼 누를 경우 */
    $(location).attr("href","${pageContext.request.contextPath}/hello.html");
 }


/*사진 미리 보기 창!!*/
function readURL(input) {
    $("#empId").val($("#emp_Code").val());
    if (input.files && input.files[0]) {
       var reader = new FileReader();
       reader.onload = function(e) { 
          $("#imgSrc").attr("src", e.target.result); 
       } 
       reader.readAsDataURL(input.files[0]);
    }
 }
 $("#addImgForm").ajaxForm({
     dataType: "json",
     success: function(responseText, statusText, xhr, $form){
         alert(responseText.errorMsg);
         $("#ImgSrc").val(responseText.url);
        
     }
 });


	/*가입하기 버튼 누를 경우 !! */
function insertFunc(){
		
    employeeBean.empCode = $("#emp_Code").val();
    employeeBean.empPassword = $("#emp_Password").val();
    employeeBean.empName = $("#empName").val();
    employeeBean.positionCode = $("#positionCode").val();
    employeeBean.authority = $("#authority").val();
    employeeBean.deptCode = $("#deptCode").val();
    employeeBean.gender = $("input[name='gender']:checked").val();
    employeeBean.residentNumber = $("#residentNumber").val();
    employeeBean.telephone =  $("#telephone").val();
    employeeBean.email = $("#email").val();
    employeeBean.enteringDate = $("#enteringDate").val();
    employeeBean.birthday = $("#birthday").val();
    employeeBean.education = $("#education").val();
    employeeBean.zipCode = $("#zipCode").val();
    employeeBean.basicAddress =  $("#basicAddress").val();
    employeeBean.detailAddress = $("#detailAddress").val();
    employeeBean.imgSrc=$("#ImgSrc").val();
	var sendEmpBean={"empBean":employeeBean};
    console.log(sendEmpBean);
    $.ajax({
       url: "${pageContext.request.contextPath}/hr/hr.do",
       type: "post",
       data: {"method": "batchEmp", JoinEmployee :JSON.stringify(sendEmpBean) },
       dataType: "json",
       success: function(data, textStatus, jqXHR) {
    	   if(data.errorCode == 1 ){
    		   
    		   alert(data.errorMsg);
    		   successDialog(data.errorMsg);
    		   
    		   
    	   }else{
    		   alert(data.errorMsg+":"+data.errorCode);
    		   failureDialog(data.errorMsg);
    		   
    	   }
       }
    });
 }

function failureDialog(Msg){
			
		$("#errorDialog").html("이미 등록된 사원코드이거나  또는 사원코드, 비밀번호는  비워두지 마십시오");
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
function successDialog(Msg){
	
	$("#errorDialog").html("회원가입에 성공하셨습니다.");
		$(function(){
	 	$("#errorDialog").dialog({
		title : "성공 메세지" ,
		modal : true,
 		width : 500,
 		heigth : 500,
 		buttons : {
  		ok : function(){
  		 $(this).dialog("close");
  		$(location).attr("href","${pageContext.request.contextPath}/hr/employeeform.html");
  		}
 		}
		 });
	});


}
</script>
</head>
<body>

<div class="form-container">
<h1 align="center"><font>사원 등록</font></h1><br>
   <table align="left">
      <tr>
         <td><br><br><br><img src="" id="imgSrc" style="width: 350px; height: 350px">
            <form id='addImgForm' action="${pageContext.request.contextPath}/base/imgFileupload.do" enctype="multipart/form-data" name="addImgForm" method="post">
               <input type="hidden" name="method" value="image" > 
               <input type="hidden" name="empCode" id="empId" > 
               <input type=file id="fileEmpImg" name="fileEmpImg" style="display: none;" onchange="readURL(this)">
               <div id="UpdateImgDiv" align="center">
               <br>
                  <button type="button" onclick="document.getElementById('fileEmpImg').click();" id="btnSearchImg">사진찾기</button>
                  <button type="submit" id="btnSaveImg">저장</button>
               </div>
            </form> <input type="hidden" id="ImgSrc">
         </td>
        
      </tr>
   </table>
   <table class="form-field-2" align="right" cellpadding="0" cellspacing="0" >
      <tr>
      	<td id="classificationtitle">사원코드</td>
         <td><input type="text" class="form-field" id="emp_Code" size="10" placeholder="[필수]사원코드를 입력하세요" ></td>
      </tr>
      <tr>
      	<td id="classificationtitle">비밀번호</td>
         <td><input type="password" class="form-field" id="emp_Password" size="10" placeholder="[필수]비밀 번호를 입력하세요" ></td>
      </tr>
      <tr>
      	<td id="classificationtitle">사원이름</td>
         <td><input type="text" class="form-field" id="empName" size="30" placeholder="사원명을 입력하세요" ></td>
      </tr>
      <tr>
      	 <td id="classificationtitle">직급</td>
         <td><input class="form-field" type="text" name="positionCode" size="30" id="positionCode" placeholder="직급코드를 선택하세요" /></td>
      </tr>
      <tr>
      	 <td id="classificationtitle">권한코드</td>
         <td><input class="form-field" type="text" name="authority" size="30" id="authority" placeholder="권한코드를 선택하세요" /></td>
      </tr>
      <tr>
      	 <td id="classificationtitle">부서코드</td>
         <td><input class="form-field" type="text" name="setdeptCode" size="30" id="deptCode" placeholder="[필수]부서코드를 선택하세요" /></td>
      </tr>
      <tr>	
      	 <td id="classificationtitle">성 별</td>
         <td>
         	남자:<input type="radio" id="male" name="gender" value="male" >
           	여자:<input type="radio" id="female" name="gender" value="female"></td>
      </tr>
      <tr>
      	 <td id="classificationtitle">주민등록번호</td>
         <td><input type="text" class="form-field" id="residentNumber" size="13" placeholder="주민등록번호를 입력하세요 (-없이)" />
      </tr>
      <tr>
      	 <td id="classificationtitle">핸드폰번호</td>
         <td><input type="text" class="form-field" id="telephone" size="11" placeholder="핸드폰번호를 입력하세요 (-없이)" ></td>
      </tr>
      <tr>
      	 <td id="classificationtitle">E-Mail</td>
         <td><input type="text" class="form-field" id="email" size="30" placeholder="E-Mail" ></td>
      </tr>
      <tr>
      	 <td id="classificationtitle">입사일자</td>
         <td><input class="form-field" type="text" name="enteringDate" size="30" id="enteringDate" placeholder="입사일자" /></td>
      </tr>
      <tr>
      	 <td id="classificationtitle">생년원일</td>
         <td><input class="form-field" type="text" name="birthday" size="30" id="birthday" placeholder="생년월일" /></td>
      </tr>
      <tr>
      	 <td id="classificationtitle">최종학력</td>
         <td><input class="form-field" type="text" name="education" size="30" id="education" placeholder="최종학력" /></td>
      </tr>
      <tr>
      	 <td id="classificationtitle">우편번호</td>
         <td><input type="text" class="form-field" id="zipCode" size="30" placeholder="우편번호" >
            <button id="zipCodeList" style="height: 25px;">
               <span class="ui-icon ui-icon-circle-zoomin"></span>
            </button>
         </td>
      </tr>
      <tr>
      	 <td id="classificationtitle">기본주소</td>
         <td><input type="text" class="form-field" id="basicAddress" size="30" placeholder="우편번호를 검색해서 넣어주세요" ></td>
      </tr>
      <tr>
      	 <td id="classificationtitle">상세주소</td>
         <td><input type="text" class="form-field" id="detailAddress" size="30" placeholder="나머지주소를 입력해주세요" ></td>
      </tr>
      <tr>
         <td colspan="2">
         <center>
         <br>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <input type="button" id="insertBtn" value="사원등록">
            <input type="button" id="insertNoBtn" value="취소">
         </center>
         </td>
      </tr>
   </table>
</div>
<div id="errorDialog"></div>
</body>
</html>