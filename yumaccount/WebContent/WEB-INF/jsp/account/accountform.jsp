<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style>
</style>
<script>

var emptyAccountControl = {}; // 관리항목을 저장하는 변수
var accountList = [];
var controlSelection;
var controlStatus; // 관리항목 GRID에서 선택한 ROW의 상태를 저장하는 변수

var accountControlList;

$(document).ready(function(){
	
	$("button").button();
	$("#btnAddControl").click(addEmptyControl);
	$("#btnDelControl").click(delAccountControl);
	$("#btnSaveControl").click(saveAccountControl);
	
	showAccountGrid();
	showAccountControlGrid();
});

function showAccountGrid() {

	$.jgrid.gridUnload("#accountGrid"); /*리로드*/
	$("#accountGrid")
			.jqGrid(
					{
						url : "${pageContext.request.contextPath}/account/account.do",
						datatype : "json",
						postData : {
							"method" : "findAccountList"
						},
						jsonReader : {
							root : "accountList" /*list이름*/
						},
						colNames : [ "그룹코드", "그룹이름", "상위계정과목코드", "계정코드",
								"계정과목명", "레벨", "상태", "계정과목별 관리코드",
								"리프노드여부", "펼치기여부", "로드여부" ],
						colModel : [ {
							name : "groupCode", 
							width : 0.7,
							hidden : true
						}, {
							name : "groupCodeName",  
							width : 100,
							align : "center",
							hidden : true
						}, {
							name : "superAccountCode",
							width : 0.7,  
							editable : true,
							align : "right"
						}, {
							name : "accountCode",
							key : true,
							width : 150,
							align : "center",
							editable : true
						}, {
							name : "accountName",
							width : 200,
							editable : true
						}, {
							name : "level",
							hidden : true
						}, {
							name : "status",
							hidden : true
						}, {
							name : "accountControlList",
							hidden : true
						}, {
							name : "leaf",
							hidden : true
						}, {
							name : "expanded",
							hidden : true
						}, {
							name : "loaded",
							hidden : true
						} ],
						caption : "계정과목 목록",
						width : 500,
						hoverrows : false,
						viewrecords : false, 
						gridview : true,
						height : 500,
						/* treeGrid 옵션*/
						treeGrid : true,
						/* tree구조로 만들 컬럼명을 value자리에 적는다.(colModel의 name에 입력한 값과 동일해야한다.)*/
						ExpandColumn : "accountName", /*확장열*/
						/* ExpandColumn의 값에 적힌 칼럼을 누르면 접히고, 펼치기 기능이 수행된다 */
						ExpandColClick : true, /*확장*/
						treedatatype : "json",
						/* 속성값이 nested면 칼럼을 클릭해서 하위항목이 접히지 않고 adjacency일때만 접힌다. */
						treeGridModel : "adjacency",
						/* 첫번쨰 페이지에 뿌려진 내용만 볼수 있다. */
						loadonce : true,
						rowNum : 100,
						treeReader : {
							parent_id_field : "superAccountCode", // 부모값 null이면 부모로노드이다.
							level_field : "level", /*레벨*/
							leaf_field : "leaf",  /*true, false 밖에 못옴*/
							
							expanded_field : "collapse", 
							
							loaded : "loaded",
							icon_field : "icon"  
						},
						
						onCellSelect : function(rowid, iCol, cellcontent, e) {
							
							 findAccountControlGrid(rowid);
						},
						beforeProcessing : function(data) { 
							emptyAccountControl = data.emptyAccountControl; 
						},
						reloadNode : function(rc) {  
							return this.each(function() {
										if (!this.grid || !this.p.treeGrid) {
											return;
										}

										var rid = this.p.localReader.id;

										$(this).jqGrid("delChildren", rc[rid]);

										var expanded = this.p.treeReader.expanded_field, parent = this.p.treeReader.parent_id_field, loaded = this.p.treeReader.loaded, level = this.p.treeReader.level_field, lft = this.p.treeReader.left_field, rgt = this.p.treeReader.right_field;

										var id = $.jgrid.getAccessor(rc,
												this.p.localReader.id);
										var rc1 = $("#" + id,
												this.grid.bDiv)[0];

										rc[expanded] = true;
										$("div.treeclick", rc1).removeClass(this.p.treeIcons.plus + " tree-plus").addClass(this.p.treeIcons.minus + " tree-minus");
										this.p.treeANode = rc1.rowIndex;
										this.p.datatype = this.p.treedatatype;
										if (this.p.treeGridModel == 'nested') {
											$(this).jqGrid(	"setGridParam",
															{
																postData : {
																	nodeid : id,
																	n_left : rc[lft],
																	n_right : rc[rgt],
																	n_level : rc[level]
																}
															});
										} else {
											$(this).jqGrid( "setGridParam",
															{
																postData : {
																	nodeid : id,
																	parentid : rc[parent],
																	n_level : rc[level]
																}
															});
										}
										$(this).trigger("reloadGrid");
										rc[loaded] = true;
										if (this.p.treeGridModel == 'nested') {
											$(this).jqGrid("setGridParam",
													{
														postData : {
															nodeid : '',
															n_left : '',
															n_right : '',
															n_level : ''
														}
													});
										} else {
											$(this).jqGrid("setGridParam",
													{
														postData : {
															nodeid : '',
															parentid : '',
															n_level : ''
														}
													});
										}
									});
						},
					});
	$("#accountGrid").jqGrid("navGrid", "#pagerAT", {
		edit : true,
		add : true,
		del : true,
		search : true,
		refresh : true
	},
	{
		drag : true,
		resize : true,
		closeOnEscape : true,
		dataheight : 150
	}, {
		drag : true,
		resize : true,
		closeOnEscape : true,
		dataheight : 150
	}).jqGrid("bindKeys");

}




function findAccountControlGrid(accountCode) { 
	$.ajax({
           url: "${pageContext.request.contextPath}/account/accountDetail.do",
           type: "post",
           data: {"method" : "findAccountControlList", "accountCode": accountCode },
           dataType: "json",
           success: function(data, textStatus, jqXHR) { // Ajax 성공 이벤트 핸들러를 지정한다. Function, Array
     			accountControlList=data.accountControlList;
     			console.log(data.accountControlList);
     			showAccountControlGrid();
           }
       });
}

function showAccountControlGrid(){
	$.jgrid.gridUnload("#accountGridControl"); /*리로드*/
	$("#accountGridControl").jqGrid(
					{
						data : accountControlList,								
						datatype : "local",
						jsonReader : {
							root : "accountControlList" /*list이름*/
						},
						editurl : "clientArray",
						colNames : [ "계정코드", "관리코드", "항목이름", "입력유형",
								"필수여부", "상태" ], 
						colModel : [ {
							name : "accountCode",
							hidden : true
						}, {
							name : "accountControlCode",
							width : 100,
							align : "center",
							editable : false
						}, {
							name : "controlName",
							width : 200,
							align : "center",
							editable : false
						}, {
							name : "inputType",
							width : 90,
							align : "center",
							editable : false
						}, {
							name : "accountControlCheck",
							width : 70,
							align : "center",
							editable : true,
							edittype: "select",
							editoptions: {value: "선택:선택;YES:YES;NO:NO"}
						}, {
							name : "status",
							width : 100,
							align : "center",
							editable : false,
							hidden : false
						} ],
						caption : "계정과목별 관리항목 리스트",
						width : 600,
						height : 500,
						hoverrows : true,
						rownumbers : true,
						multiselect : true,
						multiboxonly : true,
						viewrecords: true,
						cache:false,
						rowNum : 50,
						onSelectRow : function(id) {
							
							if (id && id !== controlSelection) {
								$(this).restoreRow(controlSelection);
								controlSelection = id;									
							}
							$(this).editRow(id, true);
							conStatus = $(this).jqGrid ('getCell', id, 'status');
							
						},
						onCellSelect : function(rowid, iCol, cellcontent, e) {
							/* 선택한 ROW의 STATUS를 변수에 저장 */
							controlStatus = $('#accountGridControl')
									.jqGrid('getCell', rowid, 'status');
							
							if (iCol == 3 && controlStatus != "select") {
								
								showCodeDialog(this, rowid, iCol);
								$("#dialogCode").dialog({
									width : "600",
									title : "관리항목선택"
								});
							}
						}
					});
}		

	/*코드 구해 오는 곳 (추가시)*/
function showCodeDialog(grid, rowid, iCol) {
		$.jgrid.gridUnload("#gridCode");
		$("#gridCode")
				.jqGrid(
						{
							url : "${pageContext.request.contextPath}/account/accountDetail.do",
							mtype : "post",
							postData : {
								"method" : "findAccountDetailList"
							},
							datatype : "json",
							jsonReader : {
								root : "controlItemList"
							},
							colNames : [ "관리항목코드", "관리항목명", "입력유형"],
							colModel : [ {
								name : "accountControlCode",
								width : 100,
								editable : false
							}, {
								name : "accountControlName",
								width : 150,
								editable : false
							}, {
								name : "accountType",
								width : 150,
								editable : false
							}],
							width : 500,
							height : 400,							
							caption : "계정과목별 관리항목 선택",
							align : "center",
							rownumbers : true,
							viewrecords: true,
							rowNum : 100,
							
							onSelectRow : function(id) {
								
								
								var adminCode = $("#gridCode").getCell(id, 1);
								var adminName = $("#gridCode").getCell(id, 2);
								var dataType = $("#gridCode").getCell(id, 3);
								
								
								
								$(grid).setCell(rowid, iCol, adminCode);
								$(grid).setCell(rowid, iCol + 1, adminName);
								$(grid).setCell(rowid, iCol + 2, dataType);
									
								$("#dialogCode").dialog("close");
							}
						});
	}

/* 관리항목 추가 */
function addEmptyControl() {
	
	var nextSeq = Number($("#accountGridControl").getGridParam("records")) + 1;
	/* 추가할수있는 관리항목을 5개로 제한한다. */
	if (nextSeq > 5) {
		alert("계정과목당 최대 관리항목은 5개입니다");
		return;
	}
	/* 관리항목 추가를 누르면 아래와 같은 값을 세팅해준다. */
	emptyAccountControl.accountControlCode = "코드선택";
	emptyAccountControl.status = "insert";
	emptyAccountControl.accountCode = $("#accountGrid").getGridParam("selrow");		
	/* 값을 추가해준다. */
	$("#accountGridControl").addRowData(nextSeq, emptyAccountControl);
}			

/* 관리항목 삭제 */
function delAccountControl() {
	
	var selectedAccountControlIds = $("#accountGridControl").getGridParam("selarrrow");
	selectedAccountControlIds.sort(function(a, b){
         return b - a;
      });

	for (var ix = 0; ix < selectedAccountControlIds.length; ix++) {
		
		var data = $("#accountGridControl").getRowData(selectedAccountControlIds[ix]);
		
		if (data.status == "normal" ) 
			$("#accountGridControl").setCell(selectedAccountControlIds[ix], "status", "delete");
	
		else if (data.status == "delete")
			$("#accountGridControl").setCell(selectedAccountControlIds[ix], "status", "normal");
		else
		
			$("#accountGridControl").delRowData(selectedAccountControlIds[ix--]);
	}
}
/* 관리항목 저장 */
function saveAccountControl() {
	console.log(JSON.stringify(accountControlList));
	var resultList = [];
	 for (ix = 0;ix < accountControlList.length; ix++){
         if (accountControlList[ix].status!="normal"){
            resultList.push(accountControlList[ix]);
         }
      }
	 console.log(JSON.stringify(resultList));
        alert(JSON.stringify(resultList));

	
	$.ajax({
				url : "${pageContext.request.contextPath}/account/accountDetail.do",
				data : {
					"method":"batchAccountControlListProcess",
					controlList : JSON.stringify(resultList)
				},
				dataType: "json",
				success : function(data) {
					//alert("에러코드의 값"+data.errorMsg);
					if (data.errorCode < 0) {
						alert("실패");
					} else {
						alert("성공");
						location.href = "accountform.html";
					}
				}
			});
}




</script>

</head>
<body>
<table>	
		<colgroup>
			<col width="40%">
			<col width="60%">
		</colgroup>
		<tr>
			<td>
				<!-- 계정과목 grid -->
				<div style="height: 40px">&nbsp;</div>
				<table id="accountGrid"></table>		
			</td>
			<td>
				<!-- 계정과목별 관리내역 grid -->
				<div id="panelAccountControl" align="center">
					<button id="btnAddControl">추가</button>
					<button id="btnDelControl">삭제</button>
					<button id="btnSaveControl">저장</button>
				</div>
				<table id="accountGridControl"></table>
			</td>
		</tr>
	</table>
	<!-- 코드 dialog -->
	<div id="dialogCode">
		<table id="gridCode"></table>
	</div>
</body>
</html>