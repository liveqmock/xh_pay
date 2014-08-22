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
<title>新华支付平台</title>
<link href="<c:url value="/css/base.css" />" type="text/css" rel="stylesheet" />
<link href="<c:url value="/css/xhpay.css" />" type="text/css" rel="stylesheet" />
<link href="<c:url value="/css/verification.css" />" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<c:url value="/js/build/kissy-min.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/xh/pay.js" />"></script>
</head>

<body>
    <!-- 首部  start -->
	<%@ include file="/WEB-INF/views/include/header.jsp" %>
	<!-- 首部 end -->
	
<div class="contentBox">
	<div class="nav-info">
    	<span class="navinfoimg"></span>
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
    <div class="pay_account_tab">
        <ul>
        	<c:if test="${order.orderType != 0 }">
            	<li class="bl nor"><a href="<c:url value="/balance/pay?id=${order.id }" />">账户支付</a></li>
            </c:if>
            <li class="pay_account_currentli">网银支付</li>
            <li><a href="<c:url value="/third/choose?id=${order.id }" />">支付平台</a></li>
        </ul>
    </div>
    <form class="ebank-form" name="ebank-form" action="<c:url value="/ebank/order/confirm.do"></c:url>" method="post">
    	<input type="hidden" name="id" value="${order.id }">
    	 <p class="pay_b">支付账号：${account.loginName }&nbsp;&nbsp;&nbsp;&nbsp;可用余额：<span><fmt:formatNumber value="${account.money }" pattern="#,##0.00"/></span> 元</p>
        <div class="quick-payment">
        	<label>已选银行</label>
<!--             <input type="radio" checked name="paybank" class="selectbankradio"> -->
            <a class="curbank bank" id="icbc"></a>
        </div>
        <div class="bank-title">网上银行</div>
        <ul class="online-banks">
        <c:forEach items="${bankGatewayMap}" var="gateway">
        	<li>
        		<input type="radio" name="bank" class="selectbankradio" value="${gateway.key}" <c:if test="${gateway.key=='vbank'}">checked="checked"</c:if> />
        		<a id="${gateway.key}" <c:if test="${gateway.key=='vbank'}">class="curbank"</c:if>>
        			<c:if test="${gateway.key=='vbank'}">${gateway.value.value}</c:if>
        			<c:if test="${gateway.key=='xhb'}">${gateway.value.value}</c:if>
        		</a>
        	</li>
        </c:forEach>
        <!-- 
            <li><input type="radio" name="bank" class="selectbankradio" value="icbc" checked="checked"><a id="icbc" class="curbank"></a></li>
        	<li><input type="radio" name="bank" class="selectbankradio" value="psbc"><a id="psbc"></a></li>
            <li><input type="radio" name="bank" class="selectbankradio" value="boc"><a id="boc"></a></li>
            <li><input type="radio" name="bank" class="selectbankradio" value="abc"><a id="abc"></a></li>
            <li><input type="radio" name="bank" class="selectbankradio" value="ccb"><a id="ccb"></a></li>
            <li><input type="radio" name="bank" class="selectbankradio" value="cmb"><a id="cmb"></a></li>
            <li><input type="radio" name="bank" class="selectbankradio" value="bcm"><a id="bcm"></a></li>
            <li><input type="radio" name="bank" class="selectbankradio" value="spdb"><a id="spdb"></a></li>
            <li><input type="radio" name="bank" class="selectbankradio" value="ceb"><a id="ceb"></a></li>
            <li><input type="radio" name="bank" class="selectbankradio" value="citic"><a id="citic"></a></li>
            <li><input type="radio" name="bank" class="selectbankradio" value="pingan"><a id="pingan"></a></li>
            <li><input type="radio" name="bank" class="selectbankradio" value="cmbc"><a id="cmbc"></a></li>
            <li><input type="radio" name="bank" class="selectbankradio" value="cbhb"><a id="cbhb"></a></li>
            <li><input type="radio" name="bank" class="selectbankradio" value="hxb"><a id="hxb"></a></li>
            <li><input type="radio" name="bank" class="selectbankradio" value="gdb"><a id="gdb"></a></li>
            <li><input type="radio" name="bank" class="selectbankradio" value="bankofshanghai"><a id="bankofshanghai"></a></li>
            <li><input type="radio" name="bank" class="selectbankradio" value="bob"><a id="bob"></a></li>
            <li><input type="radio" name="bank" class="selectbankradio" value="cib"><a id="cib"></a></li>
            <li><input type="radio" name="bank" class="selectbankradio" value="bjrcb"><a id="bjrcb"></a></li>
            <li><input type="radio" name="bank" class="selectbankradio" value="njcb"><a id="njcb"></a></li>
            <li><input type="radio" name="bank" class="selectbankradio" value="hzb"><a id="hzb"></a></li>
            <li><input type="radio" name="bank" class="selectbankradio" value="czb"><a id="czb"></a></li>
            <li><input type="radio" name="bank" class="selectbankradio" value="vbank"><a id="vbank">虚拟银行</a></li>
            <li><input type="radio" name="bank" class="selectbankradio" value="xhb"><a id="xhb">不存在银行</a></li>
             -->
        </ul>
<!--         <p class="pay_btn"><button type="button" class="pay_account_btn"></button></p> -->
        <div class="paybtnbox">
        	<a href="javascript:;"> 
        		<img src="<c:url value="/images/paybtn.png" />" onclick="document.forms['ebank-form'].submit();">
        	</a>
        </div>
    </form>
   
</div>
	<!-- 底部 start -->
	<%@ include file="/WEB-INF/views/include/footer.jsp" %>
	<!-- 底部 end -->
</body>
</html>
