<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	String path = request.getContextPath();
%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>新华支付</title>
<link href="<c:url value="/css/base.css" />" type="text/css" rel="stylesheet" />
<link href="<c:url value="/css/pay.css" />" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<c:url value="/js/build/kissy-min.js" />"></script>
</head>

<body>
<!-- 顶部 start -->
<%@ include file="/WEB-INF/views/include/header.jsp" %>
<!-- 顶部 end -->
<div class="paycontent">
	<div class="nav-info">
        <img src="<c:url value="/images/xhpayicon.png" />">
    </div>
    <div class="messagebox">
		${msg }
    </div>
    <div class="tips">
    	<h3>温馨提示</h3>
        <ul>
        	<li>1. 请确认是否符合申请条件。</li>
        	<li>2. 如果想要重新申请，请您查看原始订单，重新选择"退款申请"。</li>
        </ul>
    </div>
</div>
<!-- 底部 start -->
<%@ include file="/WEB-INF/views/include/footer.jsp" %>
<!-- 底部 end -->
</body>
</html>
