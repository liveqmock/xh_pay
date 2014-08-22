<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html><head><meta http-equiv="Content-Type" content="text/html; charset=utf-8"></head>
<body>
<%= (String)request.getAttribute("describe")%>
<form id="paysubmit" name="paysubmit"  action="<%= (String)request.getAttribute("action") %>" method="post">
<%
Map<String, String> sPara = (HashMap<String, String>)request.getAttribute("sPara");
List<String> keys = new ArrayList<String>(sPara.keySet());
Collections.sort(keys);
for (int i = 0; i < keys.size(); i++) {
    String name = (String) keys.get(i);
    String value = (String) sPara.get(name);
    out.print("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
}
%>
 </form>
<script type="text/javascript">document.forms[0].submit();</script>
</body>
</html>