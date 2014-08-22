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
<script type="text/javascript" src="<c:url value="/js/xh/pay2.js" />"></script>
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
            	<li class="bl nor"><a href="<c:url value="/balance/pay?id=${param.id}" />">账户支付</a></li>
            </c:if>
            <li class="bl nor"><a href="<c:url value="/ebank/choose?id=${param.id}" />">网银支付</a></li>
            <li class="pay_account_currentli">支付平台</li>
        </ul>
    </div>
    <!-- <form class="ebank-form" name="ebank-form" action="<c:url value="/third/order/pay.do"></c:url>" method="post" target="_blank"> -->
    <form class="ebank-form" name="ebank-form" action="<c:url value="/pay/chooseplatform.do"></c:url>" method="post" target="_blank">
    	<input type="hidden" name="id" value="${order.id }">
    	 <p class="pay_b">支付账号：${account.loginName }&nbsp;&nbsp;&nbsp;&nbsp;可用余额：<span><fmt:formatNumber value="${account.money }" pattern="#,##0.00"/></span> 元</p>
        <div class="bank-title">网上银行</div>
        
        <ul class="pay_platform">
        	<li><input type="radio" name="platform_name" class="selectbankradio" value="alipay"><a id="alipay"></a></li><%-- 
        	<li><input type="radio" name="platform_name" class="selectbankradio" value="tenpay" checked="checked"><a id="tenpay"></a></li> --%>
        </ul>
        <div class="platform_info">
            <div class="alipay_info">
            	<h3>支付宝：</h3>
        		<p>支付宝（alipay）最初作为淘宝网公司为了解决网络交易安全所设的一个功能，该功能为首先使用的"第三方担保交易模式"，由买家将货款打到支付宝账户，由支付宝向卖家通知发货，买家收到商品确认后指令支付宝将货款放于卖家，至此完成一笔网络交易。支付宝于2004年12月独立为浙江支付宝网络技术有限公司，是阿里巴巴集团的关联公司。</p>
            </div>
        </div>
<!--         <p class="pay_btn"><button type="button" class="pay_account_btn"></button></p> -->
        <div class="paybtnbox">
        	<a href="javascript:;" > 
        		<img id="paynow_btn" onclick="document.forms['ebank-form'].submit();" src="<c:url value="/images/paybtn.png" />" >
        	</a>
        </div>
    </form>
   
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
