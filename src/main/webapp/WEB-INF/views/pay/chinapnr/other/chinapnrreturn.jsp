<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" errorPage=""%>
<%@ page import="java.io.*,java.lang.*,java.sql.*,java.util.*,chinapnr.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新华网支付平台</title>
<link href="<%=path %>/css/classic/payment.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=path %>/scripts/menu.js"></script>
</head>
<body>
	<div id="all">
		<div class="head"></div>
		
		<%--topMenu start--%>
		<%@include file="/WEB-INF/views/include/menu.jsp"%>
		<%--topMenu end--%>
		
		<div class="contain_l">
			<div class="message"><span>${account.userName } 您好</span>，欢迎使用新华网充值中心。</div>
			<div class="content">
				<div class="content_title"><span>为账户充值</span></div>
				<div class="content_num">
					<ul>
						<li>第一步&nbsp;充值交易</li>
						<li>第二步&nbsp;确认订单</li>
						<li class="on">第三步&nbsp;充值完成</li>
					</ul>
				</div>
				<div class="content_m">
					<div class="cornertb">
						<div class="cornertop width_list"><b class="l"></b> <b class="r"></b>
							<div class="c"></div>
						</div>
					</div>
					<table border="0" cellpadding="0" cellspacing="0" class="corner"
						align="center">
						<tr>
							<td class="l list" valign="top"></td>
							<td valign="top">
								<div class="result">
									${message }
									<span>
									   <c:if test="${bStatus == true}">
											您的订单号为${chinapnr.ordId }，充值金额为 ${chinapnr.ordAmt }元，请您稍候查看账户余额，谢谢您的使用！
									   </c:if>
									   <c:if test="${bStatus == false}">
									   		您的订单${chinapnr.ordId }在交易过程中发生错误，请您稍后重试。
									   	如果您确认银行已经支付完成，请您30分钟后查看账户余额，如仍有问题，请联系客服人员， 谢谢您的使用！
									   </c:if> 
									</span>
								</div>

								<br />
							</td>
							<td class="r">&nbsp;</td>
						</tr>
					</table>
					<div class="cornertb">
						<div class="cornerbtm width_list"><b class="l"></b> <b class="r"></b>
							<div class="c"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	
		<!--页面右侧 start-->
<%-- 		<%@include file="/WEB-INF/views/include/contain_r.jsp"%> --%>
		<!--页面右侧 end-->
	</div>

<!--页面尾部 start-->
<%@include file="/WEB-INF/views/include/footer.jsp"%>
<!--页面尾部 end-->
<script type="text/javascript">
	setActiveMenu("menu0");
</script>
</body>
</html>