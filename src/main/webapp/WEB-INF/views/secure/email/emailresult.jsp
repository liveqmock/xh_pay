<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>账户管理</title>
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
            <span class="cur_account_balance">邮箱验证</span>           
        </div>
      <div class="safe-con">
			<div class="safe-mail">
				<div class="mail-step2"></div>
			</div>
			<div class="mail-con">
				<c:if test="${account.emailStatus==1 }">
					<div class="suc-tipsmail">
						<p class="fonctr-1">恭喜您，您的邮箱验证为</p>
						<p><span class="fonctr-4">${emailID }</span></p>
						<p><span class="fonctr-2">您可以使用此邮箱进行<a class="safe-center" href="<c:url value="/pswmgr/setpsw"/>">开启支付密码</a>等操作</span></p>
					</div>
				</c:if>
				<c:if test="${account.emailStatus==0 }">
					<div class="mail-cen clearfix">
						<span class="fail-img"> </span><span class="fonctr-5">邮箱验证失败！</span><a class="back-step" href="<c:url value="/email/emailverify.do"/>">重新验证</a>
					</div>
				</c:if>
			</div>
			<div class="safe-tips">
				<span class="tip-title">为什么要进行身份验证？</span>
				<p>
					1.为保障您的账户信息安全，在变更账户中的重要信息时需要进行身份验证，感谢您的理解与支持。
				</p>
				<p>
					2.验证身份遇到问题？请发送用户名、手机号、历史订单发票至shensu@jd.com，客服将尽快联系您。
				</p>
			</div>
		</div>

    </div>
		<script type="text/javascript">
			KISSY.use('',function(S){			
				//选择tab
				var allLi=S.all('li','.paynav');
				S.all(allLi[2]).addClass('currentli');         
			});
		</script>
</body>
</html>