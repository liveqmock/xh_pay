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
<script type="text/javascript" src="<c:url value="/js/xh/pay2.js" />"></script>
</head>

<body>
    <!-- 首部  start -->
	<%@ include file="/WEB-INF/views/include/header.jsp" %>
	<!-- 首部 end -->
<div class="contentBox">
	<div class="nav-info">
    	<span class="navinfoimg paycheck"></span>
        <img src="<c:url value="/images/xhpayicon.png" />">
    </div>
    <div class="messagebox">
		<p class="pay-info-title">订单提交成功，请尽快付款!
	    <c:if test="${order.orderType == 2 }">
           	<span class="font_g">(缴纳保证金)</span>
        </c:if>
		</p>
            <div class="textbg">
                <p>订单号：<span class="order-num">${order.orderId }</span><em style="padding-left:30px;">实付金额:<span class="pay-num-color pay-num" ><fmt:formatNumber value="${order.money }" pattern="#,##0.00"/></span>元</em></p>
                <c:choose>
                	<c:when test="${order.orderType == 2 }">
                		<p class="order-num">您将为您的帐户缴纳<span class="pay-num-color"><fmt:formatNumber value="${order.money }" pattern="#,##0.00"/></span>元保证金</p>
                	</c:when>
                	<c:otherwise>
                		<p class="order-num">立即支付<span class="pay-num-color"><fmt:formatNumber value="${order.money }" pattern="#,##0.00"/></span>元，即可完成订单。请你在<span class="pay-num-color">24小时</span>完成付款，否则订单会被自动取消。</p>
                	</c:otherwise>
                </c:choose>
            </div>
    </div>
     <div class="sure-pay">
    	<h2>您已经选择支付了</h2>
        <p><a id="icbc" class="curbank"></a><a href="<c:url value="/ebank/choose?id=${param.id}" />" target="_self" class="modifybank">修改</a></p>
        <h2>支付说明情况如下</h2>
        <img src="<c:url value="/images/bankdata.png" />">
        <button class="paynow_btn" type="button" id="paynow_btn" onclick="window.open('<c:url value="/ebank/order/pay.do?id=${param.id}&bank=${param.bank }" />')"></button>
    </div>
</div>
<div class="checkbg">
	<div class="checkbox">
		<div class="checkmessage">
        	<h2>返回支付页面</h2>
            <p class="pay-completed">
              <a href="<c:url value="/order/orderlist.do" />"> 支付完成</a>
              <a href="#">支付遇到问题</a>
            </p>
            <a href="<c:url value="/ebank/choose?id=${param.id}" />" target="_self" class="backtopay">返回重新选择银行</a>
        </div>    
    </div>
</div>
	<!-- 底部 start -->
	<%@ include file="/WEB-INF/views/include/footer.jsp" %>
	<!-- 底部 end -->
</body>
</html>
