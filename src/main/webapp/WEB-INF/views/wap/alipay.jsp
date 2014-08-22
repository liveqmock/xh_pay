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
<title>新华手机网页支付</title>
</head>

<body>
    <div>
		新华网 用户余额支付<br>
		手机页面需设计.....
    </div>
    
    <br />
    <input type="button" value="继续使用支付宝PC版支付" onclick="pickUrl();">
</body>
<script type="text/javascript">
	function pickUrl(){
		window.location.href = "/xh_pay/third/order/pay?id=${param.id}";
	}
</script>
</html>
