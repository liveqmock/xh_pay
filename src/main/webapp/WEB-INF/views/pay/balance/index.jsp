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
            <li class="pay_account_currentli">账户支付</li>
            <li><a href="<c:url value="/ebank/choose?id=${param.id}" />">网银支付</a></li>
            <li><a href="<c:url value="/third/choose?id=${order.id }" />">支付平台</a></li>
        </ul>
    </div>
    <div class="pay_account_box">
        <p class="pay_b">支付账号：${account.loginName }&nbsp;&nbsp;&nbsp;&nbsp;可用余额：<span><fmt:formatNumber value="${account.money }" pattern="#,##0.00"/></span> 元</p>
        
<form action="<c:url value="/balance/paying" />" method="post" id="space-form">
<input type="hidden" name="id" value="${param.id }">
        <div class="pay_in">
            <p>支付金额：<span class="pay-num-color pay-num"><fmt:formatNumber value="${order.money }" pattern="#,##0.00"/></span>元</p>
            
            
            
		<c:choose>
			<c:when test="${isUsingPwd == true}">
				<p class="pay_w"><label>您尚未开启支付密码! 暂时无法完成支付。请您</label><a href="<c:url value="/secure"/>" class="open_pwd"><img src="<c:url value="/images/open_pwd.png" />"></a></p>
			</c:when>
			<c:when test="${isUsingPwd == false && lessThan == true }">
				<p class="pay_w">您账户<span>余额不足</span>，请您选择<a href="<c:url value="/ebank/choose?id=${param.id}" />">网银支付</a>或<a href="<c:url value="/pay/charge.do" />">充值</a>后付款</p>
			</c:when>
			<c:otherwise>
				<p>支付密码: <input type="password" class="pay_pwd"  name="payPassword" id="payPassword" /><a href="<c:url value="/pswmgr/findpsw"/>">忘记密码？</a></p>
				<p class="pay_btn"><input type="submit" class="pay_account_btn" /></p>
			</c:otherwise>
		</c:choose>
		
        </div>
        
</form>
    </div>
</div>
 		<script type="text/javascript">
	       KISSY.config({
				debug : true,
				packages :[{
					name : 'xh',
					path : '<c:url value="/" />js'
				}]
			});
		</script>
		<script type="text/javascript">
			KISSY.use('xh/validator,xh/dialog',function(S,Validator,Dialog){
				var dTips=new Dialog();
				<c:if test="${code == 1}">
					dTips.failureTip({msg: "${message}", fun:function(){}});
				</c:if>	
				
				var validator = new Validator('#space-form', {
		            warn: 'XhWarn',  //指定使用消息提示类
		            event: 'blur' //事件
				});
				
				validator.add('#payPassword',{
					required: true
				});
			});
		</script>

<!-- 底部 start -->
<%@ include file="/WEB-INF/views/include/footer.jsp" %>
<!-- 底部 end -->

</body>
</html>


