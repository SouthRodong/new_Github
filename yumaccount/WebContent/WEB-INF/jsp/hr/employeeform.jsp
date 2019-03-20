<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title> 회원정보관리</title>
<style type="text/css">


font {
	font-size: 15px;
	font-family: sans-serif;
	
}

.form-field-1 {
	border: 0px solid #ebe5df;
	background: #ffffff;
	-webkit-border-radius: 6px;
	-moz-border-radius: 6px;
	border-radius: 6px;
	color: #827260;
	-webkit-box-shadow: rgba(255, 255, 255, 0.4) 0 0px 0, inset
		rgba(000, 000, 000, 0.7) 0 1px 1px;
	-moz-box-shadow: rgba(255, 255, 255, 0.4) 0 0px 0, inset
		rgba(000, 000, 000, 0.7) 0 1px 1px;
	box-shadow: rgba(255, 255, 255, 0.4) 0 0px 0, inset
		rgba(000, 000, 000, 0.7) 0 1px 1px;
	padding: 8px;
	margin-bottom: 20px;
	/*width: 300px;*/
	height: 13px;
	
}


.form-container {
	border: 5px solid #d7e0d5;
	background: #a1a1a1;
	background: -webkit-gradient(linear, left top, left bottom, from(#ffffff),
		to(#a1a1a1));
	background: -webkit-linear-gradient(top, #ffffff, #a1a1a1);
	background: -moz-linear-gradient(top, #ffffff, #a1a1a1);
	background: -ms-linear-gradient(top, #ffffff, #a1a1a1);
	background: -o-linear-gradient(top, #ffffff, #a1a1a1);
	background-image: -ms-linear-gradient(top, #ffffff 0%, #a1a1a1 100%);
	-webkit-border-radius: 15px;
	-moz-border-radius: 15px;
	border-radius: 15px;
	-webkit-box-shadow: rgba(000, 000, 000, 0.9) 0 0px 2px, inset
		rgba(255, 255, 255, 0.4) 0 0px 0;
	-moz-box-shadow: rgba(000, 000, 000, 0.9) 0 0px 2px, inset
		rgba(255, 255, 255, 0.4) 0 0px 0;
	box-shadow: rgba(000, 000, 000, 0.9) 0 0px 2px, inset
		rgba(255, 255, 255, 0.4) 0 0px 0;
	font-family: Consolas;
	text-decoration: none;
	vertical-align: middle;
	min-width: 300px;
	padding: 20px;
	width: 700px;
	height: 550px;
	
}
.deptdv {
	
}



.form-field {
	border-radius: 6px;
	height: 20px;
	
}

.form-field:focus {
	background: #f7b4ee;
	color: #0f020f;
	
}
/*
.addTable img {
	width: 200px;
	height: 400px;
}

input[type=text] {
	font-weight: bold;
	width: 130px;
}

#basicAddr, #email, #detailAddr {
	width: 270px;
}

#Birthinfo,  #cellphone, #empProbationDate {
	width: 500px;
}*/
.ui-jqgrid .ui-state-highlight { background: #FAED7D; } /*선택한 row 배경 색 변경 하기*/
#imgSrc {
	border: 1px double white;
	border-radius: 10px;
}

</style>
<script>
	var empCode;
	var curEmp = {};
	var empList=[];		// 모든 사원의 정보를 저장할 변수
	var selRowId;		// 사원조회 테이블에서 row를 선택할때마다 rowid를 저장하는 변수  
	var rowStatus;		// 해당 row를 선택했을 때 해당 row의 status를 저장하는 변수
	var EmpInfo;		// 사원 상세정보를 띄우기 위해 사원의 상세정보를 저장하는 변수

	$(document).ready(function() {
		$("input[type=button] , input[type=text]").button();
		$("#zipcodeList").button();
		$("#detailInfomationButton").click(showDetaildetailInformation); 
		$("#modifyButton").click(modifyEmployee);
		$("#removeButton").click(removeEmployee);
		$("#batchButton").click(batchEmployeeProcess);

		$("#zipcodeList").click(showdialogzipcode); 
		$("#exceptionbu").click(function() { $("#employeeretiredate").val("해당없음");	});
		$("#btnBatch").click(batchEmpInfoProcess);
		$("#findEmpData").click(findEmployeeList); 
		$("#findDept").click(showDialog);  

		showEmpGrid();
		/* findEmployeeList(); */
	});

	/* 모든 사원의 정보를 다 가지고 온다. */  
	function findEmployeeList() {
		var deptDetailCode=$("#findDeptCode").val(); 

		$.ajax({  
			url : "${pageContext.request.contextPath}/hr/hr.do",
			type : "post",
			data : {
				"method" : "findEmployeeList",  
				"deptCode": deptDetailCode 
			},
			dataType : "json", 
			success : function(data, textStatus, jqXHR) {
				if (data.errorCode < 0) { 
					alert(data.errorMsg);
				} else {	
					empList=data.empList; 
					 showEmpGrid(empList); 
					 
				}
			}
		});
	}

	/* 사원정보를 보여주는 함수  */
	function showEmpGrid(empList) { 
		$.jgrid.gridUnload("#empList"); 
		$("#empList").jqGrid({
			data : empList,  
			datatype : "local",
			colNames : [ "사원번호", "사원이름", "비밀번호", "부서코드", "입사날짜", "권한", "상태" ],
			colModel : [
			  	{name : "empCode", width : 30, align : "center",	editable : false},
			  	{name : "empName", width : 50, align : "center",	editable : false},
			  	{name : "empPassword", width : 40, align : "center", editable : false},
			  	{name : "deptCode", width : 40, align : "center", editable : false},
			  	{name : "enteringDate",	width : 40,	align : "center", editable : false},
			  	{name : "authority", width : 40, align : "center", editable : true,	edittype : "select", 
			  		editoptions : {	value : {SYS : 'SYS',	HRSYS : 'HRSYS',	ACSYS : 'ACSYS'}}}, 
			  	{name : "status", width : 40, align : "center",	editable : false} 
			],
			width : 900,
			height : 300,
			caption : "사원목록",
			rownumbers : true,
			rowNum : 100,
			
			onSelectRow : function(id) { 
				selRowId = $('#empList').jqGrid('getGridParam', 'selrow'); 
				
				/*alert("selRowId :"+selRowId); /*1번 숫자가 나옴*/
				
			},
			onCellSelect : function(rowid, iCol, cellcontent, e) { 
				rowStatus = $('#empList').jqGrid('getCell', rowid, 'status'); 
				
					/*	alert("rowStatus : "+rowStatus);*/
						
				if (iCol == 4 && rowStatus != "select") {
					$("#codeDialog").dialog({ width : "600", title : "부서선택" });
					showCodeDialog(this, rowid, iCol); 
					// this : 현재 grid테이블의 주소값
				}
			}

		});
		
	}

	/* 부서코드를 선택하기 위한 dialog 띄우는 메서드 */
	function showCodeDialog(grid, rowid, iCol) {  
		$.jgrid.gridUnload("#deptCodeList");
		$("#deptCodeList").jqGrid({
			url : "${pageContext.request.contextPath}/base/codeList.do",
			postData : {
				"method" : "findDetailCodeList",
				"code" : "C002"
			},
			mtype : "post",
			datatype : "json",
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
			caption : "부서 선택",
			align : "center",
			rowNum : 50,
			rownumbers : true,
			recordpos:'center',
			onSelectRow : function(id) {
				var deptCode = $("#deptCodeList").getCell(id, 1); 
				
				$(grid).setCell(rowid, iCol, deptCode);
				$("#codeDialog").dialog("close"); 
			}
		});		
	}
	
	
	/* 사원정보 수정을 눌렀을 때 발생하는 메서드 */
	function modifyEmployee() {
		if (selRowId != null) { 
			/* 선택한 row가 수정모드로 변경된다 */
			$("#empList").jqGrid('editRow', selRowId);
			if (rowStatus == "normal") {
				$("#empList").jqGrid('setRowData', selRowId, {
					deptCode : "부서변경"
				});
				$("#empList").jqGrid('setRowData', selRowId, {
					status : "update"
				});
				
						
			}
			/* 회원정보를 수정하고 키보드를 눌렀을 때 발생하는 이벤트 */
			$("#empList").keydown(keyDownFunc);
		} else {
			alert("수정할 사원을 선택해 주세요");
		}
	}

	/* 키보드를 눌렀을때 발생하는 이벤트 */
	function keyDownFunc() {
		/* keyCode가 13인 enter를 눌렀을때 실행된다. -- 회원정보수정하기 위해 선택한 row를 저장한다 */
		if (event.keyCode == 13) {
			$("#empList").jqGrid('saveRow', selRowId); /*변경 값이 저장된다.*/
		}
	}

	/* 사원을 삭제하는 메서드 */
	function removeEmployee() {
		if (selRowId != null) { 
			if (rowStatus == "normal") {
				$("#empList").jqGrid('setRowData', selRowId, {
					status : "delete"  
				});
			}
		} else {
			alert("삭제할 사원을 선택해 주세요");
		}
	}

	function batchEmployeeProcess() {
		var resultList = [];
		for (ix = 0; ix < empList.length; ix++) {
			if (empList[ix].status != "normal")
				resultList.push(empList[ix]); 
		}

		/* 변수에 담긴 사원객체를 json형태를 문자열형태로 변환한뒤 key이름인 batchList를 덧붙여 변수에 저장한다 */
		
		var sendData = "{'batchList':" + JSON.stringify(resultList) + "}";
		//alert("JSON형태로 변경 시킴 수정된사원만 !!" + resultList);
		alert("batchList:"+sendData);

		$.ajax({
			url : "${pageContext.request.contextPath}/hr/modify.do",
			type : "post",
			data : {
				"batchList" : sendData
			},
			success : function(data, textStatus, jqXHR) {
				alert(" 수정/삭제완료 ");
				location.href = "employeeform.html";
			}
		});
	}

	/* 사원의 상세정보 dialog를 띄우는 함수 */		
	function showDetaildetailInformation() {
		
		
		alert("empCode:"+empList[selRowId - 1].empCode+"   empName:"+empList[selRowId - 1].empName+" 님의 정보 입니다."); /*empList[0] 배열이니깐 첫번째 사람.. 의 정보!!*/
		EmpInfo = empList[selRowId - 1];
		console.log(EmpInfo);  /*이런게 콘솔 창에 뜬다 Object {birthday: "10", zipCode: "52882", education: "고졸", gender: "famale", residentNumber: "9305162222222"…}*/

		$("#empCode").val(EmpInfo.empCode);
		$("#empName").val(EmpInfo.empName);
		$("#telephone").val(EmpInfo.telephone);
		$("#residentNumber1").val(EmpInfo.residentNumber.substring(0, 6));
		$("#residentNumber2").val(EmpInfo.residentNumber.substring(6));
		$("#zipCode").val(EmpInfo.zipCode);
		$("#basicAddress").val(EmpInfo.basicAddress);
		$("#detailAddress").val(EmpInfo.detailAddress);
		$("input:radio[name='gender']").filter( "[value='" + EmpInfo.gender.toLowerCase() + "']").prop("checked", true);
			
		$("#email").val(EmpInfo.email);
		$("#authority").val(EmpInfo.authority); 
		$("#deptCode").val(EmpInfo.deptCode);	
		$("#imgSrc").val(EmpInfo.imgSrc); 
		
		var date = new Date(); 
		if (EmpInfo.imgSrc == null || EmpInfo.imgSrc == ""){
			//alert("기본제공사진");
			$("#imgSrc")		
					.attr("src",  
							"https://www.tenforums.com/geek/gars/images/2/types/thumb__ser.png");
							//"${pageContext.request.contextPath}/resources/image/noimg.gif");
		}else{
			//alert("사진경로존재함");  
			
			
			$("#imgSrc")		
					.attr("src", "${pageContext.request.contextPath}"+EmpInfo.imgSrc + "?time=" + date.getTime());
			$("#empImgSrc").val(EmpInfo.imgSrc);
			$("#testURL").val($("#empImgSrc").val());
			
		}

		

		$("#detailInformation").dialog({
			"width" : "1000",
			"height" : "800",
			"recordpos":'center',
			"title" : empList[selRowId - 1].empName + "님의 상세정보"
		});
		
		$("#detailInformation").css("display", "block");
		
		$("#empList").resetSelection(); 
		

	}
	

	/* 일괄처리하는 함수 */  
	function batchEmpInfoProcess() {
		EmpInfo.empCode = $("#empCode").val();
		EmpInfo.empName = $("#empName").val();
		EmpInfo.residentNumber = $("#residentNumber1").val()+ $("#residentNumber2").val();
		EmpInfo.gender = $(":radio[name='gender']:checked").val();
		EmpInfo.telephone = $("#telephone").val();
		EmpInfo.zipCode = $("#zipCode").val();
		EmpInfo.cellphone = $("#cellphone").val();
		EmpInfo.basicAddress = $("#basicAddress").val();
		EmpInfo.detailAddress = $("#detailAddress").val();
		EmpInfo.email = $("#email").val();
		EmpInfo.imgSrc = $("#empImgSrc").val(); 
		curEmp.EmpInfo = EmpInfo; 

		alert(JSON.stringify(curEmp)); 

		$.ajax({
			url : "${pageContext.request.contextPath}/hr/hr.do",
			type : "post",
			data : {
				"method" : "batchEmpInfo",
				"emp" : JSON.stringify(curEmp)
			},
			dataType : "json",
			success : function(data, textStatus, jqXHR) {
				console.log(data);
				if (data.errorCode < 0) {
					alert(data.errorMsg);
				} else {
					alert(data.errorMsg); 
					location.href = "${pageContext.request.contextPath}/hr/employeeform.html";
				}
			}
		});
	}
	/* 우편번호 window띄우는 함수 */
	function showdialogzipcode() {
		var option = "width=800px; height=520px; left=400px; top=100px;";
		window.open('${pageContext.request.contextPath}/base/addressform.html',
				"우편번호검색", option); 
	}
	
	/*  이미지 찾기 미리보기 실행*/
	function readURL(input) {
		$("#empId").val($("#empCode").val()); 
		if (input.files && input.files[0]) { 
			var reader = new FileReader();	
			reader.onload = function(e) {	
				//이미지 Tag의 SRC속성에 읽어들인 File내용을 지정
				//(아래 코드에서 읽어들인 dataURL형식)
				$("#imgSrc").attr("src", e.target.result); 
				
			}
			//File내용을 읽어 dataURL형식의 문자열로 저장
			reader.readAsDataURL(input.files[0]);
			
		}
	}

		
	$("#addImgForm").ajaxForm({
		dataType : "json",
		success : function(responseText, statusText, xhr, $form) {
			alert(responseText.errorMsg);
			$("#empImgSrc").val(responseText.url);
			$("#testURL").val(responseText.url);
		}
	});
	
	
	
	
	function showDialog(event){ 
	    
	            $("<div>",{
	               "id":"deptCodeDialog",
	               "title":"부서코드"
	            }).appendTo("body");

	            $("#deptCodeDialog").dialog({
	               "width":"550",
	               "height":"420"
	            });

	            $("<table>",{"id":"codeList"}).appendTo("#deptCodeDialog");
	            $.jgrid.gridUnload("#codeList"); 




	    
	   $("#codeList").jqGrid({
	        url:"${pageContext.request.contextPath}/base/codeList.do",
	        postData :{
	        		
	          	"method":"findDetailCodeList",
	        	"code":"C002"  
	        },
	        mtype: "post", 
	        datatype: "json",  
	        jsonReader: { root:'detailCodeList' }, 
	        colNames:['코드상세번호','코드상세이름'],  
			colModel:[
				{name:'codeNo',width:200, editable:false}, 
				{name:'codeName',width:200, editable:false},
			],
	        width: 500, 
	        height: 400,
	        caption: "부서 선택", 
	        align: "center",
	        rowNum:50,  
	        rownumbers: true, 
	       	onSelectRow: function(rowid){ 
	         
	          var codeNo = $("#codeList").jqGrid("getRowData", rowid).codeNo;
	          var codeName = $("#codeList").jqGrid("getRowData", rowid).codeName;
				
	        /*선택된 DATA object  가져오기*/
				
	          $("#findDept").val(codeName); 
	          $("#findDeptCode").val(codeNo);        

	          $("#deptCodeDialog").remove();  
	          $("#deptCodeDialog").dialog("close");
	       }  
	    });
	 }
</script>
</head>
<body>
	<div align="center">
         <font color="white">부서 코드:</font> <input type="text" size="16" id="findDept" name="findDept" readonly title="부서를 선택하세요" > 
         <input type="hidden" size="9" id="findDeptCode" name="findDeptCode"  readonly> 
         <input type="button" id="findEmpData" value="사원 조회" >
	</div><br>
	<!-- 수정,삭제,일괄처리,상세보기를 기능을 구현할 버튼 -->

	<input type="button" id="detailInfomationButton" value="상세정보보기"> 
	<input type="button" id="modifyButton" value="부서코드/권한변경">
	<input type="button" id="removeButton" value="회원정보삭제">
	<input type="button" id="batchButton" value="일괄저장"><br><br>

	<!-- 사원목록을 뿌릴 grid -->
	<table id="empList"></table>
	<!-- 상세정보를 띄우기 위한 태그들 -->
	<div id="detailInformation" style="display: none;" class="form-container" align="center">
		<button id="btnBatch" class='button'>일괄처리</button>
		<div id="tab1">
			<table class="addTable">
				<tr>		
					<td><img src="" id="imgSrc"  
						style="width: 350px; height: 400px">  
						<form id='addImgForm'
							action="${pageContext.request.contextPath}/base/imgFileupload.do"
							enctype="multipart/form-data" name="addImgForm" method="post">
							
							<input type="hidden" name="method" value="image">  
							<input type="hidden" name="empCode" id="empId">  
							
						
							<input type=file id="fileEmpImg" name="fileEmpImg"
								style="display: none;" onchange="readURL(this)"> 
								<br>
							<div id="UpdateImgDiv" align="center">
							&nbsp;
								<button type="button"
									onclick="document.getElementById('fileEmpImg').click();"
									id="btnSearchImg" class='button'>사진찾기</button>&nbsp;
								<button type="submit" id="btnSaveImg" class='button'>저장</button>
							</div>
						</form> <input type="hidden" id="empImgSrc"></td> 
						
						
						
					<td>
						<table class="detailTable">
							<tr>
								<td><label>사원번호 :</label></td>
								<td><input type="text" class="form-field1" id="empCode" readOnly></td>
							</tr>
														<tr>
								<td><label>사원이름 :</label></td>
								<td><input type="text" class="form-field1" id="empName" readOnly></td>
							</tr>
							<tr>
								<td><label>주민등록번호 :</label></td>
								<td><input type="text" class="form-field-1"
									id="residentNumber1" size="7"> - <input type="text"
									class="form-field-1" id="residentNumber2" size="7"></td>
							</tr>
							<tr>
								<td><label>성별 :</label></td>
								<td style="text-align: center;">
									<div id="gender" align="left">
										<input type="radio" id="male" name="gender" value="male"><font>남자</font>
										<input type="radio" id="female" name="gender" value="female"><font>여자</font>
									</div>
								</td>
							</tr>
							<tr>
								<td><label>핸드폰 :</label></td>
								<td><input type="text" class="form-field-1" id="telephone"></td>
							</tr>
							<tr>
								<td><label>우편번호 :</label></td>
								<td><input type="text" class="form-field-1" id="zipCode"
									size="30">
									<button id="zipcodeList" class="button" style="height: 23px;">
										<span class="ui-icon ui-icon-circle-zoomin"></span>
									</button></td>
							</tr>
							<tr>
								<td><label>주소 :</label></td>
								<td><input type="text" class="form-field-1"
									id="basicAddress" size="30"></td>
							</tr>
							<tr>
								<td><label>상세주소 :</label></td>
								<td><input type="text" class="form-field-1"
									id="detailAddress" size="30"></td>
							</tr>
							<tr>
								<td><label>E-Mail :</label></td>
								<td><input type="text" class="form-field-1" id="email"
									size="30"></td>
							</tr>
							
							
							<tr>
								<td><label>사진경로 test:</label></td>
								<td><input type="text" class="form-field-1" id="testURL"
									size="30"></td>
							</tr>
							
						</table>
					</td>
				</tr>
			</table>
		</div>
	</div>
	
	<div id="codeDialog" class="deptdv">
		<table id="deptCodeList" ></table>
	</div>
</body>
</html>
