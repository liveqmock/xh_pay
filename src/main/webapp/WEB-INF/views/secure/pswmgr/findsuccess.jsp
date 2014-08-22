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
        <div class="account_balance_box dingwei">            
            <span class="cur_account_balance">找回支付密码</span> 
            <div class="paypsd-step3"> </div>          
        </div>
          <div class="safe-con">
			<div class="space-ctr">
			</div>
			<div class="mail-con ">
				
				
				<c:if test="${code==0 }">
					<div class="find-consuc clearfix">
						<span class="suc-img"> </span>
						<div class="suc-tipscon">
							<p class="fonctr-1">新密码设置成功</p>
							<p><span class="fonctr-2">请牢记新密码。 </span><a class="back-step" href="<c:url value="/"/>">返回首页</a></p>
						</div>
					</div>
				</c:if>
				<c:if test="${code==1 }">
					<div class="mail-cen clearfix">
						<span class="fail-img"> </span><span class="fonctr-5">找回支付密码失败！</span><a class="back-step" href="<c:url value="/pswmgr/findpsw"/>" >重新找回</a>
					</div>
				</c:if>
				
			</div>
		</div>
</div>  
	<!-- 底部 start -->
	<%@ include file="/WEB-INF/views/include/footer.jsp" %>
	<!-- 底部 end -->
</body>
</html>
