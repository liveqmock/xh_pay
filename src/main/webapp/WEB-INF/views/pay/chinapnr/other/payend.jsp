<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>
<%-- <form method="post" action="${apporder.returl }"> --%>
<form method="post" action="http://www.baidu.com/">
  <input type=hidden name="Version" value="${chinapnr.version }">
  <input type=hidden name="CmdId" value="${chinapnr.cmdId }">
  <input type=hidden name="MerId" value="${chinapnr.merId }">
  <input type=hidden name="OrdId" value="${chinapnr.ordId }">
  <input type=hidden name="OrdAmt" value="${chinapnr.ordAmt }">
  <input type=hidden name="CurCode" value="${chinapnr.curCode }">
  <input type=hidden name="RetUrl" value="${chinapnr.retUrl }">
  <input type=hidden name="BgRetUrl" value="${chinapnr.bgRetUrl }">
  <input type=hidden name="ChkValue" value="${chinapnr.chkValue }">
</form>
<script type="text/javascript">
	document.forms[0].submit();
</script>
</body>
</html>