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
<link href="<%=path %>/css/classic/payment.css" rel="stylesheet"
	type="text/css" />
<script type="text/javascript"
	src="<%=path %>/scripts/jquery/jquery.min.1.5.2.js"></script>
<script type="text/javascript" src="<%=path %>/scripts/menu.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$("input[name=bank]").click(function(){
		var cls= $(this).attr("mark");
		$("#banklogo").removeClass();
		$("#banklogo").addClass("bank_bg");
		$("#banklogo").addClass(cls);
	});

	$("#bankform").submit(function(){
		var radio = $("input[name=bank]:checked").val();
		if(radio==undefined){
			alert("请您选择所要支付的银行");
			return false;
		}
		var moneyObj = $("#money");
		var money = moneyObj.val();
		var result = /^[1-9][0-9]*$|^[1-9][0-9]*\.[0-9]{1,2}$/.test(money);
		
		if(!result){
			alert("您的输入有误，请您输入正确的金额");
			moneyObj.focus();
			return false;
		} else {
			if(!(Number(money)>=5)){
				alert("充值金额不能低于5元");
				moneyObj.focus();
				return false;
			} 
		}
	});
});

</script>
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
		<td valign="top">
		<form id="bankform" name="bankform" action="<%=path%>/order/chinapnr/bank.do" method="post">
		<div class="bank">
		<div class="bank_icon"><span>01</span>&nbsp;&nbsp;请选择银行：</div>
		</div>
		<div class="bank_con"><!--工行、农行、建行-->
		<div class="bank_logo"><label> <b class="radio"><input
			name="bank" type="radio" value="icbc" mark="bank_icbc" /></b> <b
			class="bank_bg bank_icbc"></b> </label></div>
		<div class="bank_logo"><label> <b class="radio"><input
			name="bank" type="radio" value="abchina" mark="bank_abchina" /></b> <b
			class="bank_bg bank_abchina"></b> </label></div>
		<div class="bank_logo"><label> <b class="radio"><input
			name="bank" type="radio" value="ccb" mark="bank_ccb" /></b> <b
			class="bank_bg bank_ccb"></b> </label></div>


		<!--中行、交行、浦发-->
		<div class="bank_logo"><label> <b class="radio"><input
			name="bank" type="radio" value="" boc mark="bank_boc" /></b> <b
			class="bank_bg bank_boc"></b> </label></div>
		<div class="bank_logo"><label> <b class="radio"><input
			name="bank" type="radio" value="bankcomm" mark="bank_bankcomm" /></b> <b
			class="bank_bg bank_bankcomm"></b> </label></div>
		<div class="bank_logo"><label> <b class="radio"><input
			name="bank" type="radio" value="spdb" mark="bank_spdb" /></b> <b
			class="bank_bg bank_spdb"></b> </label></div>


		<!--招行、中信、民生-->
		<div class="bank_logo"><label> <b class="radio"><input
			name="bank" type="radio" value="cmbchina" mark="bank_cmbchina" /></b> <b
			class="bank_bg bank_cmbchina"></b> </label></div>
		<div class="bank_logo"><label> <b class="radio"><input
			name="bank" type="radio" value="ecitic" mark="bank_ecitic" /></b> <b
			class="bank_bg bank_ecitic"></b> </label></div>
		<div class="bank_logo"><label> <b class="radio"><input
			name="bank" type="radio" value="cmbc" mark="bank_cmbc" /></b> <b
			class="bank_bg bank_cmbc"></b> </label></div>


		<!--兴业、深发、华夏-->
		<div class="bank_logo"><label> <b class="radio"><input
			name="bank" type="radio" value="cib" mark="bank_cib" /></b> <b
			class="bank_bg bank_cib"></b> </label></div>
		<div class="bank_logo"><label> <b class="radio"><input
			name="bank" type="radio" value="sdb" mark="bank_sdb" /></b> <b
			class="bank_bg bank_sdb"></b> </label></div>
		<div class="bank_logo"><label> <b class="radio"><input
			name="bank" type="radio" value="hxb" mark="bank_hxb" /></b> <b
			class="bank_bg bank_hxb"></b> </label></div>


		<!--北京、光大、农村-->
		<div class="bank_logo"><label> <b class="radio"><input
			name="bank" type="radio" value="bankofbeijing"
			mark="bank_bankofbeijing" /></b> <b class="bank_bg bank_bankofbeijing"></b>
		</label></div>
		<div class="bank_logo"><label> <b class="radio"><input
			name="bank" type="radio" value="cebbank" mark="bank_cebbank" /></b> <b
			class="bank_bg bank_cebbank"></b> </label></div>
		<div class="bank_logo"><label> <b class="radio"><input
			name="bank" type="radio" value="bjrcb" mark="bank_bjrcb" /></b> <b
			class="bank_bg bank_bjrcb"></b> </label></div>



		<!--南京、东亚、平安-->
		<div class="bank_logo"><label> <b class="radio"><input
			name="bank" type="radio" value="njcb" mark="bank_njcb" /></b> <b
			class="bank_bg bank_njcb"></b> </label></div>
		<div class="bank_logo"><label> <b class="radio"><input
			name="bank" type="radio" value="hkbea" mark="bank_hkbea" /></b> <b
			class="bank_bg bank_hkbea"></b> </label></div>
		<div class="bank_logo"><label> <b class="radio"><input
			name="bank" type="radio" value="pingan" mark="bank_pingan" /></b> <b
			class="bank_bg bank_pingan"></b> </label></div>


		<!--杭州、宁波、浙商-->
		<div class="bank_logo"><label> <b class="radio"><input
			name="bank" type="radio" value="hccb" mark="bank_hccb" /></b> <b
			class="bank_bg bank_hccb"></b> </label></div>
		<div class="bank_logo"><label> <b class="radio"><input
			name="bank" type="radio" value="nbcb" mark="bank_nbcb" /></b> <b
			class="bank_bg bank_nbcb"></b> </label></div>
		<div class="bank_logo"><label> <b class="radio"><input
			name="bank" type="radio" value="czbank" mark="bank_czbank" /></b> <b
			class="bank_bg bank_czbank"></b> </label></div>


		<!--上海、渤海-->
		<div class="bank_logo"><label> <b class="radio"><input
			name="bank" type="radio" value="bankofshanghai"
			mark="bank_bankofshanghai" /></b> <b class="bank_bg bank_bankofshanghai"></b>
		</label></div>
		<div class="bank_logo"><label> <b class="radio"><input
			name="bank" type="radio" value="cbhb" mark="bank_cbhb" /></b> <b
			class="bank_bg bank_cbhb"></b> </label></div>



		</div>
		<div class="space"></div>

		<div class="pay_icon"><span>02</span>&nbsp;&nbsp;请输入充值金额：</div>

		<div class="pay">
			<div class="bank_bg bank_default" id="banklogo">&nbsp;</div>
			<div class="l_input mar">
				<input id="money" name="money" type="text" size="14" maxlength="8"/>元<br />
				<span>
				      提示：充值金额不小于 5 元
				</span>
			</div>
		</div>
     <div class="pbutton">
		<input class="button" type="submit" value="提交" /> 
		<input type="text" style="display: none;" />
	 </div>		
		</form>

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
<%-- <%@include file="/WEB-INF/views/include/contain_r.jsp"%> --%>
<!--页面右侧 end-->

</div>

<!--页面尾部 start-->
<%@include file="/WEB-INF/views/include/footer.jsp"%>
<!--页面尾部 end-->
<script type="text/javascript">
	setActiveMenu("menu0");
	setActiveLeftMenu("leftMenu0");
</script>
</body>
</html>
