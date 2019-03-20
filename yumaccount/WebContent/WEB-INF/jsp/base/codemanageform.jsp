<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>코드 관리</title>
<style type="text/css">
#actionbutton {
	display: block;
	height: 50px;
}

.overflow {
	height: 130px;
}

font {
	color: white;
	font-size: 35px;
	font-weight: bold;
}
</style>
<script>
	var codeList = []; 
	var emptyCode = {}; 
	var emptyDetailCode = {}; 

	var codeSelection; 
	var detailSelection; 

	$(document).ready(function() {
		$("input:button").button();

		$("#addCode").click(addEmptyCode);
		$("#deleteCode").click(deleteCode);

		$("#addDetailCode").click(addemptyDetailCode);

		$("#rsultSave").click(batchCodeProcess);
		$("#deletDetailCode").click(deleteDetailCode);

		findCode();
	});

	function findCode() {
		$.ajax({
			url : "${pageContext.request.contextPath}/base/codeList.do",
			type : "post",
			data : {
				method : "findCodeList"
			},
			dataType : "json",
			success : function(data, textStatus, jqXHR) { 
				if (data.errorCode < 0) {
					alert(data.errorMsg);
				} else {
					emptyCode = data.emptyCode; 
					emptyDetailCode = data.emptyDetailCode; 
					codeList = data.codeList; 

					basicState(); 
					showCodeList();
					showDetailCodeList(); 
				}

			},
			error : function(jqXHR, textStatus, error) {
				alert("코드 조회 오류입니다");
			}
		

		});

	}

	function showCodeList() {
		$.jgrid.gridUnload("#codelist"); 
		$("#codelist").jqGrid({
			data : codeList,
			datatype : "local", 
			editurl : "clientArray", 
			
			colNames : [ "코드", "코드이름", "사용여부", "상태", "상세" ],
			colModel : [ {
				name : "divisionCode",
				width : 100,
				editable : true
			}, {
				name : "codeName",
				width : 200,
				editable : true
			},{
				name : "codeUseCheck",
				width : 100,
				
				editable : true ,/*추가*/
				edittype: "select", 
				editoptions: {value: "선택:선택;YES:YES;NO:NO"}
			},{
				name : "status",
				width : 60,
				editable : false
			}, {
				name : "codeDetailList",
				hidden : true
			
			} ],
			caption : "코드메뉴", 
			width : 500,
			height : 'auto',
			rowNum : 50, 
			rownumbers : true, 
			viewrecords : true,
			multiselect : true, 
			multiboxonly : true, 
			onSelectRow : function(id) {
				if (id && id !== codeSelection) { 
					$(this).jqGrid("restoreRow", codeSelection);
					$(this).jqGrid("editRow", id, { 
						keys : true
					
					});
					codeSelection = id;
				}
			}, /*선택셀행번호, 선택 셀의 열번호, 선택한 셀의 값*/
			onCellSelect : function(rowid, iCol, cellcontent, e) { 
				if (cellcontent != "&nbsp;") {
					//alert(codeList[rowid-1].codeName+" 코드의 상세정보 입니다");
				}
				showDetailCodeList(rowid); 
			} 
		}).bind("jqGridInlineAfterSaveRow", function(e, rowid, orgClickEvent) {
			var status = $(this).getCell(rowid, "status");
			if (status != "insert") {
				$(this).setCell(rowid, "status", "update");
			}
			if (status == "normal") {		//노말로 바꿔주는 로직
				$(this).setCell(rowid, "status", "update");
			}

		});

	}

	function showDetailCodeList(codeId) {
		$.jgrid.gridUnload("#detaillist");
		$("#detaillist").jqGrid({ 
			data : isNaN(codeId) ? [] : codeList[codeId - 1].codeDetailList,
			datatype : "local",
			editurl : "clientArray",
			colNames : [ "코드", "상세코드", "코드이름", "사용여부", "상태" ],
			colModel : [ {
				name : "divisionCode",
				width : 100,
				editable : false
			}, {
				name : "codeNo",
				width : 100,
				editable : true
			}, {
				name : "codeName",
				width : 300,
				editable : true
			},{
				name : "codeUseCheck",
				width : 100,
				editable : true ,	/*추가*/
				edittype: "select", 
				editoptions: {value: "선택:선택;YES:YES;NO:NO"}
			},{
				name : "status",
				width : 100,
				editable : false
			}, ],
			caption : "상세코드내역",
			width : 700,
			height : 450,
			rownumbers : true,
			rowNum : 50,
			viewrecords : true,
			multiselect : true,
			multiboxonly : true,
			onSelectRow : function(id) {
				if (id && id !== detailSelection) {
					$(this).jqGrid("restoreRow", detailSelection);
					$(this).jqGrid("editRow", id, {
						keys : true
					});
					detailSelection = id;
				}
			},
			onCellSelect : function(rowid, iCol, cellcontent, e) {
				// do nothing
			}
		}).bind("jqGridInlineAfterSaveRow", function(e, rowid, orgClickEvent) {
			var status = $(this).getCell(rowid, "status");
			if (status != "insert") {
				$(this).setCell(rowid, "status", "update");
				$("#codelist").setCell(codeId, "status", "normal");
			}
		});
	}
	
	function basicState() {
		
		emptyCode.status = "insert";
		emptyDetailCode.status = "insert";

	}
	function addEmptyCode() {
		var nextSeq = Number($("#codelist").getGridParam("records")) + 1; 
		$("#codelist").addRowData(nextSeq, emptyCode);
	}

	function deleteCode() {
		var selectedCodeIds = $("#codelist").getGridParam("selarrrow");
		
		selectedCodeIds.sort(function(a, b) {
			return b - a;
		});
		for (var ix = 0; ix < selectedCodeIds.length; ix++) {
			var codeData = $("#codelist").getRowData(selectedCodeIds[ix]);
			if (codeData.status == "normal" || codeData.status == "update")
				$("#codelist").setCell(selectedCodeIds[ix], "status", "delete"); 
			else if (codeData.status == "delete")
				$("#codelist").setCell(selectedCodeIds[ix], "status", "update"); 
			else
				$("#codelist").delRowData(selectedCodeIds[ix--]);
		}
	}

	function addemptyDetailCode() {
		// 코드그리드 강제세이브
		$("#codelist").saveRow(codeSelection);
		var nextSeq = Number($("#detaillist").getGridParam("records")) + 1;
		// 셀렉트된 로우들고와서 emptyDetailCode에 controlCode에 세팅
		var codeId = $("#codelist").getGridParam("selrow");
		//alert(codeId); 
		var controlCode = $("#codelist").getCell(codeId, 2);
		//alert(controlCode); 
		emptyDetailCode.divisionCode = controlCode; 
		$("#detaillist").addRowData(nextSeq, emptyDetailCode);
	} 



	function deleteDetailCode() {
		var selectedCodeIds = $("#codelist").getGridParam("selarrrow");
		var selectedDetailIds = $("#detaillist").getGridParam("selarrrow");
		selectedCodeIds.sort(function(a, b) {
			return b - a;
		});

		for (var ix = 0; ix < selectedDetailIds.length; ix++) {
			$("#codelist").setCell(selectedCodeIds, "status", "normal");
			var codeData = $("#detaillist").getRowData(selectedDetailIds[ix]);
			if (codeData.status == "normal" || codeData.status == "update") {
				$("#detaillist").setCell(selectedDetailIds[ix], "status",
						"delete");
			} else if (codeData.status == "delete")
				$("#detaillist").setCell(selectedDetailIds[ix], "status",
						"update");
			else
				$("#detaillist").delRowData(selectedDetailIds[ix--]);
		}
	}

	function batchCodeProcess() {
		console.log(codeList);
		var resultList = [];
		for (ix = 0; ix < codeList.length; ix++) {
			if (codeList[ix].status != "select") {
				resultList.push(codeList[ix]); 
			}
		}
		console.log(JSON.stringify(resultList));
		alert(JSON.stringify(resultList)); 

		$.ajax({
			url : "${pageContext.request.contextPath}/base/codeList.do",
			type : "post",
			dataType : "json",
			data : {
				method : "batchCodeProcess",
				batchList : JSON.stringify(resultList)
			},
			success : function(data, textStatus, jqXHR) {
				if (data.errorCode < 0) {
					alert(data.errorMsg);
				} else {
					alert("코드가 저장되었습니다");
					location.href = "codemanageform.html";
				}
			}
		});
	}
</script>
</head>
<body>
	<div align="center">
		<h1>코드관리설정</h1>
	</div>

	<table>
		<tr>
			<td><input type="button" id="addCode" value="코드추가"> <input
				type="button" id="deleteCode" value="코드삭제"> <input
				type="button" id="rsultSave" value="일괄처리">

				<table id="codelist">
				</table></td>

			<td><input type="button" id="addDetailCode" value="상세추가">
				<input type="button" id="deletDetailCode" value="상세삭제">
				<table id="detaillist">
				

				</table></td>
		</tr>

	</table>

</body>
</html>