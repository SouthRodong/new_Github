<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>주소검색</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="${pageContext.request.contextPath}/scripts/jqGrid/js/jquery-1.11.0.min.js"></script>
   <script src="${pageContext.request.contextPath}/scripts/jqGrid/js/i18n/grid.locale-en.js"></script>
   <link rel="stylesheet" href="${pageContext.request.contextPath}/scripts/jqGrid/css/ui.jqgrid.css" />
   <script src="${pageContext.request.contextPath}/scripts/jqGrid/js/jquery.jqGrid.min.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/jqueryUI/jquery-ui.min.css" />
   <script src="${pageContext.request.contextPath}/scripts/jqueryUI/jquery-ui.min.js"></script>
<script>
	var sido;
	var sidoname;
	var sigunguname;
	$(document).ready(function() {
		
		$("input[type=button],input[type=text]").button();
		
		$("body").css("font-size", "12px");
		$("#addrForm").css({
			"height" : "500px",
			"overflow" : "auto"
		});
		$("#listContainerJibun").css({
			"height" : "250px",
			"overflow" : "auto",
			"font-size" : "12px"
		});
		$("#listContainerRoad").css({
			"height" : "250px",
			"overflow" : "auto",
			"font-size" : "12px"
		});
		$("#addrComplete").css({
			"height" : "120px",
			"overflow" : "auto",
			"font-size" : "12px"
		});
		$("#addrCompleteRoad").css({
			"height" : "120px",
			"overflow" : "auto",
			"font-size" : "12px"
		});

		$('#searchJibun').button();
		$('#searchJibun').click(function() {
			searchJibun();
		});
		$('#searchRoad').button();
		$('#searchRoad').click(function() {
			searchRoad();
		});
		
		$('#tabs').tabs();
		$('#tabs-1').tabs();
		$('#tabs-2').tabs();
		$('#dong').focus();
		setSido();
	});
	function keyDownHandler(e) {
		if (window.event.keyCode == 13) {
			searchRoad();
		}
	}
	function searchJibun() {
		var dong = $('#dong').val();
		$.ajax({
			url : '${pageContext.request.contextPath}/base/addresscode.do',

			data : {
				"method":"searchJibun",
				"dong":dong
			},
			type : 'post',
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			cache : false,
			dataType : 'json',
			success : function(data) {
				if (data.errorCode < 0) {
					alert(data.errorMsg);
				}
				if (data.postList) {
					console.log(JSON.stringify(data.postList));
					dataSet = data.postList;
					printAddrList(dataSet);
				}
			},
			error : function() {
				alert("에러발생");
			}
		});
	}

	function printAddrList(dataSet) {
		$('#listContainerJibun').html("");
		var array = [];
		array.push("<tr><td><b>주소를 선택하세요.</b></td></tr>");
		$.each(dataSet, function(index, postBean) {
			array.push("<tr><td onclick='insertAddr(this)'><font color='pink'>"
					+ postBean.zipNo + "/" + postBean.sido);
			array.push(" " + postBean.sigungu);
			array.push(" " + postBean.dong);
			array.push(" " + postBean.ri + "</font></td></tr>");
		});
		$('<table/>', {
			html : array.join(''),
			'class' : 'listTable',
			'width' : '400px'
		}).appendTo('#listContainerJibun');
	}
	function insertAddr(addr) {

		var array = [];
		$('#addrComplete').html('');
		var addr = $(addr).text();
		var zip = addr.split("/");
		array.push("<b>상세주소를 입력하세요.</b><br>");
		array.push("우편번호 : <input type='text' size='10' id='zipcode' value='"
                  + zip[0] + "' disabled><br>");
		array.push("기본주소 : <input type='text' size='50' id='baseAddr' value='"
                     + zip[1] + "' disabled><br>");
		array.push("상세주소 : <input type='text' size='50' id='detailAddr'><br>");
		array.push("<input type='button' id='done' class='doneBT' onclick='completeAddr()' value='입력완료'>");
		$('#addrComplete').html(array.join(""));
		$('#detailAddr').focus();
	}
	function completeAddr() {
		opener.document.getElementById("basicAddress").value = $('#baseAddr').val();
		opener.document.getElementById("detailAddress").value = $('#detailAddr').val();
		opener.document.getElementById("zipCode").value = $('#zipcode').val();
		self.close();
	}
	
	function setSido() {
		$.ajax({
			url : '${pageContext.request.contextPath}/base/addresscode.do',
			data : {
				"method" : "searchSido"
			},
			type : 'post',
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			cache : false,
			dataType : 'json',
			success : function(data) {
				if (data.errorCode < 0) {
					alert(data.errMsg);
				}
				if (data.postSidoList) {
					dataSet = data.postSidoList;
					printSidoList(dataSet);
				}
			},
			error : function() {
				alert("에러발생");
			}
		});
	}
	function printSidoList(dataSet) {
		$('#listContainerSido').html("");
		var array = [];
		array.push("<select id=selSido onchange='setSigungu(this.value)'>");
		array.push("<option> ======== </option>");
		$.each(dataSet,function(index, postBean) {
			array.push("<option value='" + postBean.sido+"/"+postBean.sidoname + "'>"
						+ postBean.sidoname + "</option>");
						});
		array.push("</select>");
		$('#listContainerSido').append(array.join(''));
	}
	function setSigungu(sidocode) {
		sido = sidocode.split("/")[0];
		sidoname = sidocode.split("/")[1];
		$.ajax({
			url : '${pageContext.request.contextPath}/base/addresscode.do',
			data : {
				"method" : "searchSigungu",
				"sido" : sido
			},
			type : 'post',
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			cache : false,
			dataType : 'json',
			success : function(data) {
				if (data.errorCode < 0) {
					alert(data.errMsg);
				}
				if (data.postSigunguList) {
					dataSet = data.postSigunguList;
					printSigunguList(dataSet);
				}
			},
			error : function() {
				alert("에러발생");
			}
		});
	}
	function printSigunguList(dataSet) {
		$('#listContainerSigungu').html("");
		var array = [];
		array.push("<select id=selSigungu onchange='setSigunguname(this.value)'>");
		array.push("<option> ======== </option>");
		$.each(dataSet, function(index, postBean) {
			array.push("<option value='"+ postBean.sidoname+"'>"
					+ postBean.sidoname + "</option>");
		});
		array.push("</select>");
		$('#listContainerSigungu').append(array.join(''));
	}
	function setSigunguname(sigungu) {
		sigunguname = sigungu;
	}
	function searchRoad() {
		var roadname = $('#roadname').val();
		var selsido = $('#selSido').val();
		var selsigungu = $('#selSigungu').val();
		if ($.trim(selsido) == "========") {
			alert('시도를 선택하세요.');
			return;
		}
		if ($.trim(selsigungu) == "========") {
			alert('시군구를 선택하세요.');
			return;
		}
		if (!$.trim(roadname)) {
			alert('상세주소를 입력하세요.');
			return;
		}
		alert(sido + ',' + sigunguname + ',' + roadname);
		$.ajax({
			url : '${pageContext.request.contextPath}/base/addresscode.do?check=searchRoadname',
			data : {
				"method":"searchRoadname",
				'sido' : sido,
				'sigunguname' : sigunguname,
				'roadname' : roadname
			},
			type : 'post',
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			cache : false,
			dataType : 'json',
			success : function(data) {
				if (data.errorCode < 0) {
					alert(data.errorMsg);
				} else {
					if (data.datanull) {
						alert(data.datanull);
					} else if (data.postRoadList) {
						dataSet = data.postRoadList;
						printRoadList(dataSet);
					}
				}
			},
			error : function() {
				alert("에러발생");
			}
		});
	}

	function printRoadList(dataSet) {
		$('#listContainerRoad').html("");
		var array = [];
		array.push("<tr><td><b>주소를 선택하세요.</b></td></tr>");
		$.each(dataSet,function(index, postBean) {
			array.push("<tr><td onclick='insertRoadAddr(this)'><font color='pink'>"
					+ postBean.zipcode + "/" + sidoname);
			array.push(" " + sigunguname);
			array.push(" " + postBean.roadname);
			array.push(" " + postBean.buildingcode1 + "-"
					+ postBean.buildingcode2 + "</font></td></tr>");
		});
		$('<table/>', {
			html : array.join(''),
			'class' : 'listTable',
			'width' : '400px'
		}).appendTo('#listContainerRoad');
	}
	function insertRoadAddr(addr) {
		var str = "";
		$('#addrCompleteRoad').html('');
		var addr = $(addr).text();
		var zip = addr.split("/");
		str += "<b>상세주소를 입력하세요.</b><br>";
		str += "우편번호 : <input type='text' size='10' id='zipcodeRoad' value='"
                  + zip[0] + "' disabled><br> ";
		str += "기본주소 : <input type='text' size='50' id='baseAddrRoad' value='"
                     + zip[1] + "' disabled><br>";
		str += "상세주소 : <input type='text' size='50' id='detailAddrRoad'><br>";
		str += "<input type='button' id='done' class='doneBT' onclick='completeAddrRoad()' value='입력완료'>";
		$('#addrCompleteRoad').html(str);
		$('#detailAddrRoad').focus();
	}
	function completeAddrRoad() {
		alert($('#baseAddrRoad').val());
		alert($('#detailAddrRoad').val());
		opener.document.getElementById("basicAddress").value = $('#baseAddrRoad').val()
		opener.document.getElementById("detailAddress").value = $('#detailAddrRoad').val();
		opener.document.getElementById("zipCode").value = $('#zipcodeRoad').val();
		self.close();
	}
</script>
</head>
<body>
	<div id="tabs">
		<ul>
			<li><a href="#tabs-1">도로명주소</a></li>
			<li><a href="#tabs-2">지번주소</a></li>
		</ul>
		<div id="tabs-1">
			<b>주소검색 &nbsp;&nbsp;&nbsp;시/도:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b> <span id="listContainerSido"></span>&nbsp;&nbsp;&nbsp;
			<b>시/군/구: </b> <span id="listContainerSigungu"></span> <br> <b>도로명을
				입력하세요. </b>&nbsp;&nbsp; <input type="text" id="roadname"
				style="ime-mode: active" onKeyDown="keyDownHandler(event);" />&nbsp;
			<input type="button" id="searchRoad" value="검색"><br>
			<div id="listContainerRoad"></div>
			<br>
			<div id="addrCompleteRoad"></div>
		</div>
		<div id="tabs-2">
			<center>
				<b>주소검색 : </b> <b>읍/면/동을 입력하세요.</b>&nbsp;&nbsp; <input type="text"
					id="dong" style="ime-mode: active">&nbsp; <input
					type="button" id="searchJibun" value="검색"> <br>
				<div id="listContainerJibun"></div>
				<br>
				<div id="addrComplete"></div>
			</center>
		</div>
	</div>
</body>
</html>