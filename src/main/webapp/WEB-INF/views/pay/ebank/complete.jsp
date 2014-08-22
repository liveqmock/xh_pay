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
<link href="<c:url value="/css/xhpay.css" />" type="text/css" rel="stylesheet" />
<link href="<c:url value="/css/verification.css" />" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<c:url value="/js/build/kissy-min.js" />"></script>
</head>

<body>
<!-- 顶部 start -->
<%@ include file="/WEB-INF/views/include/header.jsp" %>
<!-- 顶部 end -->
<div class="contentBox">
	<div class="nav-info">
    	<span class="navinfoimg payresult"></span>
        <img src="<c:url value="/images/xhpayicon.png" />">
    </div>
    <div class="payreslut">
    	<div class="pay-detail">
    	
    	<c:if test="${empty appOrder.retUrl}">
        	<h2>页面将在3秒钟后自动跳转至首页，如果没有跳转请点击“<a href="<c:url value="/" />">返回首页</a>”</h2>
        </c:if>
        <c:if test="${not empty appOrder.retUrl}">
        	<h2>页面将在3秒钟后自动跳转，如果没有跳转请点击“<a href="javascript:void(0);" onclick="document.forms['forward'].submit();return false;">回到商户</a>”</h2>
        </c:if>
    	
        	
            <div class="pay-data">
            	<p>订单号：<span class="order-num">${appOrder.orderId }</span></p>
                <p class="font_c">应付金额：<span class="pay-num-color pay-num"><fmt:formatNumber value="${appOrder.money }" pattern="#,##0.00"/></span>元&nbsp;&nbsp;实付金额:<span class="pay-num-color pay-num"><fmt:formatNumber value="${appOrder.money }" pattern="#,##0.00"/></span>元</p>
            </div>
            <p class="otherlink">交易完成，你可以&nbsp;<a href="#" target="_blank">查看交易详情</a>&nbsp;&nbsp;<a href="#" target="_blank">查看问题</a></p>
        </div>
    </div>
</div>
<!-- 底部 start -->
<%@ include file="/WEB-INF/views/include/footer.jsp" %>
<!-- 底部 end -->

<script type="text/javascript">     
	function countDown(secs) {
		if (--secs > 0) {
			setTimeout("countDown(" + secs + ")", 1000);
		} else {
			document.forms['forward'].submit();
		}
	}
</script> 
<c:if test="${not empty appOrder.retUrl}">
<script type="text/javascript">countDown(3);</script>
<form name="forward" method="get" action="${appOrder.retUrl }">
	<input type=hidden name="cmdId" value="${cmdId}"  />
	<input type=hidden name="orderId" value="${appOrder.orderId}"  />
	<input type=hidden name="trxId" value="${appOrder.trxId}"  />
	<input type=hidden name="appId" value="${appOrder.appId}"  />
	<input type=hidden name="pid" value="${appOrder.pid}"  />
	<input type=hidden name="pname" value="${appOrder.pname}"  />
	<input type=hidden name="merPriv" value="${appOrder.merPriv}"  />
	<input type=hidden name="money" value="${appOrder.money}"  />
	<input type=hidden name="status" value="${appOrder.status}"  />
	<input type=hidden name="retType" value="${retType}"  />
	<input type=hidden name="chkValue" value="${chkValue}"  />
</form>
</c:if>
<c:if test="${empty appOrder.retUrl}">
<script type="text/javascript">countDown(3);</script>
<form name="forward" method="get" action="<c:url value="/" />">
</form>
</c:if>
</body>
</html>
