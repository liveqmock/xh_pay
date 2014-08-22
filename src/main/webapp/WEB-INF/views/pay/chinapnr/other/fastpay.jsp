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
<h1>手机快捷支付</h1>
	<form action="<%=path %>/order/chinapnr/selectbank.do" method="post" target="_blank">
		金额：<input type="text" name="money" value="52.60">
		应用id：<input type="text" name="appId" value="100"><br>
		前台返回地址：<input type="text" name="appReturl" value="https://maps.google.com/maps?hl=zh-CN&tab=ll"><br>
		后台返回地址：<input type="text" name="appBgReturl" value="http://data.auto.qq.com/car_public/hq/areanewslist_beijing.shtml"><br>
		应用订单号：<input type="text" name="appOrderid" value="201306301111231111">
		商品号：<input type="text" name="pid" value="110003123"><br>
		商品名称：<input type="text" name="pname" value="文库——关于国家水利建设新项目审核规则.doc">
		应用私有域：<input type="text" name="merpriv" value="ABCDEFGJHIJKRSTKMNOPQUVWXYZ">
		备注：<input type="text" name="ext" value="这是支付的第一笔订单"><br>
		<input type="submit" value="手机快捷支付">
	</form>
	<br><br><br><br>
	<h1>PC快捷支付</h1>
	<form action="<%=path %>/order/chinapnr/pcselectbank.do" method="post" target="_blank">
		金额：<input type="text" name="money" value="52.60">
		应用id：<input type="text" name="appId" value="100"><br>
		前台返回地址：<input type="text" name="appReturl" value="https://maps.google.com/maps?hl=zh-CN&tab=ll"><br>
		后台返回地址：<input type="text" name="appBgReturl" value="http://data.auto.qq.com/car_public/hq/areanewslist_beijing.shtml"><br>
		应用订单号：<input type="text" name="appOrderid" value="201306302222232222">
		商品号：<input type="text" name="pid" value="110003123"><br>
		商品名称：<input type="text" name="pname" value="文库——关于国家水利建设新项目审核规则.doc">
		应用私有域：<input type="text" name="merpriv" value="ABCDEFGJHIJKRSTKMNOPQUVWXYZ">
		备注：<input type="text" name="ext" value="这是支付的第一笔订单"><br>
		<input type="submit" value="PC快捷支付">
	</form>
	<br><br><br><br><br>
	<h1>普通网银支付</h1>
	<form action="<%=path %>/order/chinapnr/chinapnrpayorder.do" method="post" target="_blank">
		金额：<input type="text" name="money" value="52.60">
		应用id：<input type="text" name="appId" value="100"><br>
		前台返回地址：<input type="text" name="appReturl" value="https://maps.google.com/maps?hl=zh-CN&tab=ll"><br>
		后台返回地址：<input type="text" name="appBgReturl" value="http://data.auto.qq.com/car_public/hq/areanewslist_beijing.shtml"><br>
		应用订单号：<input type="text" name="appOrderid" value="201306303333233333">
		商品号：<input type="text" name="pid" value="110003123"><br>
		商品名称：<input type="text" name="pname" value="文库——关于国家水利建设新项目审核规则.doc">
		应用私有域：<input type="text" name="merpriv" value="ABCDEFGJHIJKRSTKMNOPQUVWXYZ">
		备注：<input type="text" name="ext" value="这是支付的第一笔订单"><br>
		<input type="submit" value="网银支付">
	</form>
</body>
</html>