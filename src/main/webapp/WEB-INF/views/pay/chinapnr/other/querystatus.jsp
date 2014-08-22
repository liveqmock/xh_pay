<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>查询订单状态</title>
<style type="text/Css">
<!--
table {
	border-collapse: collapse;
}

td {
	border-left: 0;
	border-top: 2;
	font-size: 14px;
	height: 30px;
	padding: 0 12px 0 12px;
	border-collapse: collapse;
}
-->
</style>

</head>
<body>

<table height="50" border= "0"> 
	<tr>
		<td width="90">
			&nbsp;
		</td>
	</tr>
</table>
	<div style="padding-left:280px;font-size:26px;"> 
		<p>
				您的订单状态：
		</p>
	</div>
<table align="center" width="396" border= "1" cellspacing="0" cellpadding="0"> 
	<tr>
		<td width="130">
			CmdId：
		</td>
		<td>
			${chinapnr.cmdId }
		</td>
	</tr>
	<tr>
		<td width="130">
			RespCode：
		</td>
		<td>
			${chinapnr.respCode }
		</td>
	</tr>
	<tr>
		<td width="130">
			ProcStat  ：
		</td>
		<td>
			${pnr.procStatName }
		</td>
	</tr>
	<tr>
		<td width="130">
			ErrMsg  ：
		</td>
		<td>
			${chinapnr.errMsg }
		</td>
	</tr>
	<tr>
		<td colspan="2" align="center">
			<input type="button" value=" 关 闭 " size="16" onclick="window.close();">
		</td>
	</tr>
</table>
</body>
</html>