<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>新华支付-快捷支付</title>
</head>
<body>
	<form action="<%=path %>/order/chinapnr/appordersend.do?id=${id}" method="post" target="_blank">
		应用名称：${appid }<br>
		商品名称：${pname }<br>
		支付金额：${money }<br>
		订单流水号：${id }
		选择银行：兴业银行<input type="radio" name="bank" value="cib"><br></br>
				广发银行<input type="radio" name="bank" value="07"><br></br>
		<input type="submit" value="网银支付">
	</form>
</body>
</html>