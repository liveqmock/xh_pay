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

<link href="<c:url value="/css/xhpay.css" />" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<c:url value="/js/build/kissy-min.js" />"></script>

<style type="text/css">
.payinput {
	display: inline-block;
	height: 20px;
	padding: 4px 6px;
	margin-bottom: 10px;
	font-size: 14px;
	line-height: 20px;
	color: #555;
	vertical-align: middle;
	-webkit-border-radius: 4px;
	-moz-border-radius: 4px;
	border-radius: 4px;
}

.contentBox .payreslut {
	background: #fafffa;
	width: 992px;
	border: solid 4px #ccdee5;
	margin: 20px 0 500px;
	background: url(<c:url value="/images/tmp_payresultbg.png" />) 55px 0px no-repeat;
}
</style>

</head>

<body>
<!-- 顶部 start -->
<%@ include file="/WEB-INF/views/include/header.jsp" %>
<!-- 顶部 end -->
<div class="contentBox">
	<div class="nav-info">
		<img src="<c:url value="/images/xhpayicon.png" />">
		<a href="<c:url value="/ebank/choose?id=${param.id}" />">网银支付</a>
    </div>
    <div>
    <ul>
    <li>订单号：<span class="order-num">${order.orderId }</span>&nbsp;&nbsp;实付金额:<span class="pay-num-color pay-num"><fmt:formatNumber value="${order.money }" pattern="#,##0.00"/></span>元</li>
            <li>立即支付<span class="pay-num-color"><fmt:formatNumber value="${order.money }" pattern="#,##0.00"/></span>元，即可完成订单。请你在<span class="pay-num-color">24</span>小时完成付款，否则订单会被自动取消。</li>
    </ul>
    </div>
    <p>&nbsp;</p>
    <p>&nbsp;</p>
    <p>&nbsp;</p>
    <div >
    	<br>支付帐号：${account.loginName }
    	<br>可用余额：<fmt:formatNumber value="${account.money }" pattern="#,##0.00"/>元
    	<br>余额充足：${ lessThan}
    	
    	<form action="<c:url value="/balance/paying" />" method="post">
    	<input type="hidden" name="id" value="${param.id }">
    	<br>支付密码：<input type="text" name="payPassword" id="payPassword">
    	<br><input type="submit" id="send-mail" value="提交">
    	</form>
    </div>
</div>

<script type="text/javascript">
<c:if test="${code == 1}">
alert('${message}');
</c:if>
// KISSY.use('',function(S){
// 	S.all('#send-mail').on('click',function(){
// 		var payPassword=S.all('#payPassword').val();
// 		S.IO({
// 			url:'<c:url value="/secure/verify" />',
// 		    type:"post",
// 		    processData:true,
// 		    data:{
// 		    	"payPassword":payPassword
// 		    },
// 		    dataType:'json',
		
// 		    success:function (d) {
// 		        if(d.code===0){
// 		        	window.location.href="";
// 		        }else if(d.code===1){
// 		        	alert("失败");
// 		        }
// 		    }
// 		});
// 	});	
// });
</script>

<!-- 底部 start -->
<%@ include file="/WEB-INF/views/include/footer.jsp" %>
<!-- 底部 end -->
</body>
</html>


