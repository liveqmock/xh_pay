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
</head>
<body>
<div class="contentBox">
    <div class="payreslut">
    	<div class="pay-detail">
            	<p>已向邮箱账号：${emailID }发送验证码请注意查收</p>
            	<form id="email-form" name="email-form" action="<%=path%>/email/submitcaptcha.do" method="post">
						<input type="hidden" name="emailID" value="${emailID}">
						请输入验证码：<input id="captcha" name="captcha" type="text"  class="payinput" size="10"  maxlength="6"/> 
						<input type="submit" value="提交">
				</form>
            </div>
		</div>
    </div>
</body>
</html>