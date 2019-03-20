<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>YUM Account</title>



<link rel="stylesheet" href="${pageContext.request.contextPath}/scripts/jqueryUI/jquery-ui.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/scripts/jqGrid/css/ui.jqgrid.css" />
<script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
<script src="${pageContext.request.contextPath}/scripts/jqueryUI/jquery-ui.js"></script>
<script src="${pageContext.request.contextPath}/scripts/jqGrid/js/jquery.jqGrid.min.js"></script>
<script 	src="${pageContext.request.contextPath}/scripts/jqGrid/js/i18n/grid.locale-kr.js"></script>
<script 	src="${pageContext.request.contextPath}/scripts/jqGrid/js/i18n/grid.locale-en.js"></script>
<script src="http://malsup.github.com/jquery.form.js"></script>





<style type="text/css">

 .ui-datepicker{
   z-index: 9999 !important;
}

.ui-dialog { 
   z-index: 9999 !important ; 
   font-size:12px;
}


	 #topmenu {
 	margin: 5px 5px 5px 5px;
	padding: 5px 5px 5px 5px;
	height: 20%;
	min-width: 95%;
	
 	}
 	
 	 #wrap {
 		height:100%;
 		min-width:100%;
 		background: transparent;
	}

	#bottom {
 		
 		text-align:center;
 		min-width:600px;
	}
	
	body { 
	margin : 0 auto;
	background-image: url('${pageContext.request.contextPath}/resources/image/back.jpg');
	background-repeat: no-repeat;
	background-attachment: fixed;
	background-size: cover;
	background-position: center;
	
}

</style>

<decorator:head />

</head>
<body>
	
	<div id="topmenu" align="center">
		<jsp:include page="top.jsp"/>
	</div>
	<br>
	<br>
	<br>
	<br>
	<br>	
 	 <div id="wrap" align="center">
 	 	 	<div id="contents" align="center">
	 		<div id="section" align="center">
	 			<decorator:body/>
	 		</div>
		 </div>
	 </div>
	 
	 <div align="center" style="clear:both;"></div>
	 
	 <div id="bottom" align="center">	
			<jsp:include page="bottom.jsp" />
			<br>
	</div>
 	
	
</body>
</html>