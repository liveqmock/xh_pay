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
<link href="<c:url value="/css/xhpay.css" />" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<c:url value="/js/build/kissy-min.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/xh/pay.js" />"></script>
</head>

<body>
<!-- 顶部 start -->
<%@ include file="/WEB-INF/views/include/header.jsp" %>
<!-- 顶部 end -->

<%-- 	<form action="<%=path %>/order/chinapnr/appordersend.do?id=${id}" method="post" target="_blank"> --%>
<%-- 		应用名称：${appId }<br> --%>
<%-- 		商品名称：${pname }<br> --%>
<%-- 		支付金额：${money }<br> --%>
<%-- 		订单流水号：${id } --%>
<!-- 		选择银行：兴业银行<input type="radio" name="bank" value="09"><br></br> -->
<!-- 				广发银行<input type="radio" name="bank" value="07"><br></br> -->
<!-- 		<input type="submit" value="网银支付"> -->
<!-- 	</form> -->
<div class="contentBox">
	<div class="nav-info">
    	<img class="navinfoimg" src="<c:url value="/images/navinfo1.png" />">
        <img src="<c:url value="/images/xhpayicon.png" />">
    </div>
    <div class="messagebox">
		<ul>
        	<li class="pay-info-title">订单提交成功，请尽快付款 <a href="<c:url value="/balance/pay?id=${order.id }" />">余额支付</a></li>
            <c:if test="${order.orderType == 2 }">
            	<li> <span style="color:green;">缴纳保证金</span></li>
            </c:if>
            <li>订单号：<span class="order-num">${order.orderId }</span>&nbsp;&nbsp;实付金额:<span class="pay-num-color pay-num"><fmt:formatNumber value="${order.money }" pattern="#,#00.00"/></span>元</li>
            <li>立即支付<span class="pay-num-color"><fmt:formatNumber value="${order.money }" pattern="#,#00.00"/></span>元，即可完成订单。请你在<span class="pay-num-color">24</span>小时完成付款，否则订单会被自动取消。</li>
        </ul>    
    </div>
    <form class="ebank-form" name="ebank-form" action="<c:url value="/order/chinapnr/confirm.do"></c:url>">
    <input type="hidden" name="id" value="${order.id }">
<%--     <form class="ebank-form" action="<c:url value="/order/chinapnr/confirm.do"></c:url>"> --%>
    	<div class="ebanktitle">网银支付</div>
<!--         <div class="quick-payment"> -->
<!--             <input type="radio" checked name="paybank" class="selectbankradio"> -->
<!--             <a class="curbank bank" id="icbc"></a> -->
<!--             <a class="other-bank"></a> -->
<!--         </div> -->
<!--         <div class="bank-title">网上银行</div> -->
        <ul class="online-banks">
            
            <li><input type="radio" name="bank" class="selectbankradio" value="icbc"><a id="icbc"></a></li>
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
        </ul>
        <div class="paybtnbox">
        	<a href="javascript:;"> 
        		<img src="<c:url value="/images/paybtn.png" />" onclick="document.forms['ebank-form'].submit();">
        	</a>
        </div>
    </form>
   
</div>

<!-- <div class="more-banks-box"> -->
<!-- 	<div class="more-banks"> -->
<!--     	<div class="more-banks-title">选择银行<a href="javascript:void(0)">点击隐藏</a></div> -->
<!--         <form> -->
<!--         <ul> -->
<!--         	<li><input type="radio" name="quickpayment" class="selectbankradio"><a id="icbc"></a></li> -->
<!--         	<li><input type="radio" name="quickpayment" class="selectbankradio"><a id="psbc"></a></li> -->
<!--             <li><input type="radio" name="quickpayment" class="selectbankradio"><a id="boc"></a></li> -->
<!--             <li><input type="radio" name="quickpayment" class="selectbankradio"><a id="abc"></a></li> -->
<!--             <li><input type="radio" name="quickpayment" class="selectbankradio"><a id="ccb"></a></li> -->
<!--             <li><input type="radio" name="quickpayment" class="selectbankradio"><a id="cmb"></a></li> -->
<!--             <li><input type="radio" name="quickpayment" class="selectbankradio"><a id="bcm"></a></li> -->
<!--             <li><input type="radio" name="quickpayment" class="selectbankradio"><a id="spdb"></a></li> -->
<!--             <li><input type="radio" name="quickpayment" class="selectbankradio"><a id="ceb"></a></li> -->
<!--             <li><input type="radio" name="quickpayment" class="selectbankradio"><a id="citic"></a></li> -->
<!--             <li><input type="radio" name="quickpayment" class="selectbankradio"><a id="pingan"></a></li> -->
<!--             <li><input type="radio" name="quickpayment" class="selectbankradio"><a id="cmbc"></a></li> -->
<!--             <li><input type="radio" name="quickpayment" class="selectbankradio"><a id="sdb"></a></li> -->
<!--             <li><input type="radio" name="quickpayment" class="selectbankradio"><a id="hxb"></a></li> -->
<!--             <li><input type="radio" name="quickpayment" class="selectbankradio"><a id="gdb"></a></li> -->
<!--             <li><input type="radio" name="quickpayment" class="selectbankradio"><a id="bankofshanghai"></a></li> -->
<!--             <li><input type="radio" name="quickpayment" class="selectbankradio"><a id="nbcb"></a></li> -->
<!--         </ul> -->
<!--         </form> -->
<%--         <div class="more-bank-btn"><a href="javascript:void(0)"><img src="<c:url value="/images/more-banks-btn.png" />"></a></div> --%>
<!--     </div> -->
<!-- </div> -->

<!-- 底部 start -->
<%@ include file="/WEB-INF/views/include/footer.jsp" %>
<!-- 底部 end -->
</body>
</html>
