<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>
<form method="post" action="http://test.chinapnr.com/gar/RecvMerchant.do">
  <input type=hidden name="Version" value="${chinapnr.version }">
  <input type=hidden name="CmdId" value="${chinapnr.cmdId }">
  <input type=hidden name="MerId" value="${chinapnr.merId }">
  <input type=hidden name="OrdId" value="${chinapnr.ordId }">
  <input type=hidden name="OrdAmt" value="${chinapnr.ordAmt }">
  <input type=hidden name="CurCode" value="${chinapnr.curCode }">
  <input type=hidden name="Pid" value="${chinapnr.pid }">
  <input type=hidden name="RetUrl" value="${chinapnr.retUrl }">
  <input type=hidden name="MerPriv" value="${chinapnr.merPriv }">
  <input type=hidden name="GateId" value="${chinapnr.gateId }"> 
  <input type=hidden name="PayUsrId" value="${chinapnr.payUsrId }">
  <input type=hidden name="BgRetUrl" value="${chinapnr.bgRetUrl }">
  <input type=hidden name="ChkValue" value="${chinapnr.chkValue }">
</form>
<script type="text/javascript">
	document.forms[0].submit();
</script>
</body>
</html>