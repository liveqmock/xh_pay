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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>退款申请</title>
<link href="<c:url value="/css/base.css" />" type="text/css" rel="stylesheet" />
<link href="<c:url value="/css/pay.css" />" type="text/css" rel="stylesheet" />
<link href="<c:url value="/css/verification.css" />" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<c:url value="/js/build/kissy-min.js" />"></script>
</head>

<body>
    <!-- 首部  start -->
	<%@ include file="/WEB-INF/views/include/header.jsp" %>
	<!-- 首部 end -->
    
    <div class="paylogo">
        <img src="<c:url value="/images/payLogo.png" />" />
    </div>
    
    
	<!-- 首部导航  start -->
	<%@ include file="/WEB-INF/views/include/contain_header.jsp" %>
	<!-- 首部导航 end -->
    
<div class="paycontent">

    <div class="account_balance_box">
    	<span class="cur_account_balance">退款申请</span>
    </div>
    <form id="ebank-form" name="ebank-form" action="<c:url value="/user/refund/apply/confirm.do" />" method="post">
    <div class="recharge_messagebox">
        <p class="pay_b">支付账号：${account.loginName }&nbsp;&nbsp;&nbsp;&nbsp;退款金额：<span><fmt:formatNumber value="${refundMoney }" pattern="#,##0.00"/></span> 元</p>
		<%-- <p><label>充值账户：</label><span>${account.loginName }</span></p>  --%>
        <p><label>退款原因：</label><textarea class="refund_input" id="reason" name="reason" placeholder="请输入您的退款原因!" ></textarea></p>
        <%--<p class="pd80">请注意：支持国内主流银行储蓄卡充值，在线支付成功后，充值金额会在一分钟内到账。</p>
  		<p class="pd80"><input type="image" src="<c:url value="/images/next_step.png" />" /> --%>
 		<p class="pd80"><img id="chargeSub" src="<c:url value="/images/more-banks-btn.png" />" />
 		<input type="hidden" name="orderId" value="${orderId}" />
    </div>
    </form>
    <div class="tips">
    	<h3>温馨提示</h3>
        <ul>
        	<li>1. 充值成功后，余额可能存在延迟现象，一般1到5分钟会内到账，如有问题，请咨询客服；</li>
            <li>2. 充值金额输入值必须是不小于10且不大于50000的正整数；</li>
            <li>3. 您只能用储蓄卡在线支付充值金额的钱，如遇到任何支付问题可以查看在线支付帮助；</li>
            <li>4. 充值完成后，您可以进入账户充值记录页面进行查看余额充值状态。</li>
        </ul>
    </div>


   
</div>
	<!-- 底部 start -->
	<%@ include file="/WEB-INF/views/include/footer.jsp" %>
	<!-- 底部 end -->
		<script type="text/javascript">
			KISSY.use('',function(S){
			
				S.all('#chargeSub').on('click',function(){
// 					var result=validator.isValid();
// 					if(result){
// 						S.DOM.get('#ebank-form').submit();
// 					}
					S.DOM.get('#ebank-form').submit();
				});
			});
		</script>
		
	<script type="text/javascript">
	    KISSY.use('',function(S){			
	        //选择tab
	        var allLi=S.all('li','.paynav');
	        S.all(allLi[0]).addClass('currentli');         
	    });
	</script>
</body>
</html>
