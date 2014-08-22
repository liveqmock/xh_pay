<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
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
			<div class="message"><span>${account.userName } 您好</span>，欢迎使用新华网充值中心，您目前的账户余额是<span><fmt:formatNumber
				type="number" value="${account.money }" /></span> 元。</div>
			<div class="content">
				<div class="content_title"><span>为账户充值</span></div>
				<div class="content_num">
					<ul>
						<li>第一步&nbsp;充值交易</li>
						<li class="on">第二步&nbsp;确认订单</li>
						<li>第三步&nbsp;充值完成</li>
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
				                <div class="bank" id="width">
				                    <div class="bank_icon"><span id="letter">*</span>&nbsp;&nbsp;交易错误：</div>
				                </div>
				                <div class="spacer"></div>
								<table class="bor">
									<tbody>
										<tr>
											<td style="height: 50px; font-size:14px;">网络异常，无法获取您提交的订单数据，请您稍后<a href="javascript:;" onclick="location.reload();">重试</a>，给您带来的不便，请您谅解！</td>
										</tr>
									</tbody>
								</table>
								<br />
								<div class="pbutton">
									<input type="button" class="button" value=" 返 回 " onclick="cancel();" />
								</div>
					
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
	function cancel(){
		var url = "<%=path %>/pay/chinapnr.do";
		window.location.href=url;
	}
	setActiveMenu("menu0");
</script>
</body>
</html>