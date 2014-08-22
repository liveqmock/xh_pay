<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新华网支付平台</title>
<link href="<%=path%>/css/classic/payment.css" rel="stylesheet" type="text/css" />
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
	<li class="on">第一步&nbsp;充值交易</li>
	<li>第二步&nbsp;确认订单</li>
	<li>第三步&nbsp;充值完成</li>
</ul>
</div>
<div class="content_m">




<div class="cornertb">
<div class="cornertop"><b class="l"></b> <b class="r"></b>
<div class="c"></div>
</div>
</div>

<table border="0" cellpadding="0" cellspacing="0" class="corner"
	align="center">
	<tr>
		<td class="l" valign="top">

		<%--leftMenu start--%>
		<%@include file="/WEB-INF/views/include/leftMenu.jsp"%>
		<%--leftMenu end--%>


		</td>
		<td valign="top" class="yjhk">

		<div class="pay_icon"><span>01</span>&nbsp;&nbsp;银行电汇：
          	<div class="bank_unionpay r_logo"></div>
        </div>

		
		<h3>银行电汇：</h3>
          <ul>
          	<li>收款方：新华网络有限公司 </li>
            <li>开户行：新华网银行</li>
            <li>账  号：10000000000000000000</li>
            <li>传  真：010-00000000</li>
            <li>咨询电话：010-11111111</li>
          </ul>  
          
          <h3>汇款流程：</h3>
          <ul class="dash">
          	<li>第一步：到银行柜台按示例填好汇款单，办理汇款、购卡事宜；</li>
            <li>第二步：在银行返给的汇款回单上写明您的电话、发票内容、邮寄地址；</li>
            <li>第三步：如果您想快速获得服务，请将银行回单传真至010-00000000；</li>
            <li>第四步：我们收到后将立即与您联系办理账号事宜（节假日顺延）。</li>
          </ul>
		<div class="space"></div>
		
		<div class="pay_icon"><span>02</span>&nbsp;&nbsp;邮局汇款：
          	<div class="bank_yzcx r_logo"></div>
        </div>

		
		<h3>汇款地址：</h3>
          <ul>
          	<li>地  址：北京市大兴区</li>
            <li>收款人：新华网</li>
            <li>邮  编：100000</li>
            <li>传  真：010-00000000</li>
            <li>咨询电话：010-11111111</li>
          </ul>  
          
          <h3>汇款流程：</h3>
          <ul class="dash">
          	<li>第一步：到邮局按示例填好汇款单，办理汇款、购卡事宜；</li>
            <li>第二步：在邮局返给的汇款回单上写明您的电话和发票内容、邮寄地址；</li>
            <li>第三步：如果您想快速获得服务，请将邮局回单传真至010-00000000；</li>
            <li>第四步：我们收到后将立即与您联系办理账号事宜（节假日顺延）。</li>
          </ul>
          <div class="space"></div>

		</td>
		<td class="r">&nbsp;</td>
	</tr>
</table>

	<div class="cornertb">
		<div class="cornerbtm"><b class="l"></b> <b class="r"></b>
		<div class="c"></div>
		</div>
	</div>

</div>

</div>

</div>

<!--页面右侧 start-->
<%-- TODO --%>
<!--页面右侧 end-->

</div>

<!--页面尾部 start-->
<%@include file="/WEB-INF/views/include/footer.jsp"%>
<!--页面尾部 end-->
<script type="text/javascript">
	setActiveMenu("menu0");
	setActiveLeftMenu("leftMenu4");
</script>
</body>
</html>