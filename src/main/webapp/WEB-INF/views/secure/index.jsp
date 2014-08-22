<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/page-deal.tld" prefix="page" %>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>账户管理</title>
<link href="css/base.css" rel="stylesheet" />
<link href="css/pay.css" rel="stylesheet" />
<link rel="stylesheet" href="css/verification.css" />
<script src="js/build/kissy.js"></script>
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
            <span class="cur_account_balance">安全中心</span>           
        </div>
       <div class="safe-con">

		
			<c:choose>
				<c:when test="${account.emailStatus == 0}">
					<div class="safe-level">
						<span class="level-title">安全级别：</span>
						<span class="the-level">初级</span>
						<span class="level-img"><span class="level-img1 level1"> </span> </span>
						<span class="level-suggestion">建议您启动全部安全设置，以保障账户以及资金安全</span>
					</div>
					<div class="safe-pub clearfix unverification">
						<span class="unverification-img"> </span>
						<span class="unverification-word">邮箱验证</span>
						<span class="unverification-detail">验证后，可用于快速找回登录密码，接受账户余额变动提醒。</span>
						<a class="unverification-button" href="<c:url value="/email/emailverify.do"/>">立即验证</a>
					</div>
					<div class="safe-pub clearfix lock">
						<span class="lock-img"> </span>
						<span class="lock-word">支付密码</span>
						<span class="lock-detail">启用支付密码后，在使用账户中余额或优惠券等资产时，需要输入支付密码</span>
						<span class="lock-button">立即启用</span>
					</div>
				</c:when>
				<c:when test="${account.emailStatus == 1 && account.payCodeStatus == 0}">
					<div class="safe-level">
						<span class="level-title">安全级别：</span>
						<span class="the-level">中级</span>
						<span class="level-img"><span class="level-img1 level2"> </span> </span>
						<span class="level-suggestion">建议您启动全部安全设置，以保障账户以及资金安全</span>
					</div>
					<div class="safe-pub clearfix unverification">
						<span class="verificatied-img"> </span>
						<span class="unverification-word">邮箱验证</span>
						<span class="unverification-detail">验证后，可用于快速找回登录密码，接受账户余额变动提醒。</span>
						<a class="verification-button">已验证</a>
					</div>
					<div class="safe-pub clearfix unverification">
						<span class="unverification-img"> </span>
						<span class="unverification-word">支付密码</span>
						<span class="unverification-detail">启用支付密码后，在使用账户中余额或优惠券等资产时，需要输入支付密码</span>
						<a class="unverification-button" href="<c:url value="/pswmgr/setpsw"/>">立即启用</a>
					</div>
				</c:when>
				<c:when test="${account.emailStatus == 1 && account.payCodeStatus == 1}">
					<div class="safe-level">
						<span class="level-title">安全级别：</span>
						<span class="the-level">较高</span>
						<span class="level-img"><span class="level-img1 level3"> </span> </span>
						<span class="level-suggestion">建议您启动全部安全设置，以保障账户以及资金安全</span>
					</div>
					<div class="safe-pub clearfix unverification">
						<span class="verificatied-img"> </span>
						<span class="unverification-word">邮箱验证</span>
						<span class="unverification-detail">验证后，可用于快速找回登录密码，接受账户余额变动提醒。</span>
						<a class="verification-button">已验证</a>
					</div>
					<div class="safe-pub clearfix unverification">
						<span class="unverification-img"> </span>
						<span class="unverification-word">支付密码</span>
						<span class="unverification-detail">启用支付密码后，在使用账户中余额或优惠券等资产时，需要输入支付密码</span>
						<a class="unverification-button" href="<c:url value="/pswmgr/changepsw"/>">修改密码</a>
						<a class="remember-tips" href="<c:url value="/pswmgr/findpsw"/>">忘记密码？</a>
					</div>
				</c:when>
				<c:otherwise>
					<div class="safe-level">
						<span class="level-title">安全级别：</span>
						<span class="the-level">非常高</span>
						<span class="level-img"><span class="level-img1 level3"> </span> </span>
						<span class="level-suggestion">您已启动全部安全设置，您的账户以及资金安全</span>
					</div>
					<div class="safe-pub clearfix unverification">
						<span class="verificatied-img"> </span>
						<span class="unverification-word">邮箱验证</span>
						<span class="unverification-detail">验证后，可用于快速找回登录密码，接受账户余额变动提醒。</span>
						<a class="verification-button">已验证</a>
					</div>
					<div class="safe-pub clearfix unverification">
						<span class="unverification-img"> </span>
						<span class="unverification-word">支付密码</span>
						<span class="unverification-detail">启用支付密码后，在使用账户中余额或优惠券等资产时，需要输入支付密码</span>
						<a class="unverification-button" href="pay-manage-chpsd1.html">修改密码</a>
					</div>
				</c:otherwise>
			</c:choose>
		
		<div class="safe-tips">
			<span class="tip-title">安全服务提示</span>
			<p>1.确认您登录的是<span id="login-name"> </span>,注意防范进入钓鱼网站，不要轻信各种即时通讯工具发送的商品或支付链接，谨防网购诈骗。</p>
			<p>2.建议您安装杀毒软件，并定期更新操作系统等软件补丁，确保账户及交易安全。</p>
		</div>
	</div>

</div>
	<!-- 底部 start -->
	<%@ include file="/WEB-INF/views/include/footer.jsp" %>
	<!-- 底部 end -->
		<script type="text/javascript">
			KISSY.use('',function(S){			
				//选择tab
				var allLi=S.all('li','.paynav');
				S.all(allLi[2]).addClass('currentli');         
			});
		</script>
</body>
</html>