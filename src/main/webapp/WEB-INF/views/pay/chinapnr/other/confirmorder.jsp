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
<script type="text/javascript" src="<%=path %>/scripts/jquery/jquery.min.1.5.2.js"></script>
<script type="text/javascript" src="<%=path %>/scripts/jquery/jquery.blockUI.js"></script>
<script type="text/javascript" src="<%=path %>/scripts/menu.js"></script>
<script type="text/javascript">
$(document).ready(function() { 
    $('#confirmOrder').click(function() {
        $.blockUI({
            message: $('#maskLayer'),
            css: { 
                width: '350px',
                border: '6px solid #AAAAAA',
                cursor:'default'
            },
	     	// styles for the overlay 
	        overlayCSS:  { 
	            backgroundColor: '#000', 
	            opacity:         0.5 
	        }
        }); 
        $('#completePay').click(function (){
            window.location='<c:url value="/order/orderlist.do"></c:url>';
        	//$.unblockUI();
        });
        $('#problemPay').click(function (){
        	//window.close();
        	$.unblockUI();
        });
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
							<form method="post" action="<%=path %>/order/chinapnr/send.do" target="_blank">
								<input type="hidden" name="orderId" value="${order.orderId }" />
								<input type="hidden" name="money" value="${order.money }" />
								<input type="hidden" name="gateId" value="${order.gateId }" />
				                <div class="bank" id="width">
				                    <div class="bank_icon"><span id="letter">*</span>&nbsp;&nbsp;确认订单：</div>
				                    <div class="bank_r bank_${bank}"></div>
				                </div>
				                <div class="spacer"></div>
								<table class="bor">
									<tbody>
										<tr>
											<th>订单号</th>
											<td>${order.orderId }</td>
										</tr>
										<tr>
											<th>充值金额</th>
											<td>${order.money }&nbsp;&nbsp;元</td>
										</tr>
									</tbody>
								</table>
								<br />
								<div class="pbutton">
									<input id="confirmOrder" type="submit" class="button" value="确认订单" />
									<input type="button" class="button" value=" 取 消 " onclick="cancel();" />
								</div>
							</form>
					
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
		var url = "<%=path %>/pay/chinapnr.do?bank=${bank}";
		window.location.href=url;
	}
	setActiveMenu("menu0");
</script>


<div id="maskLayer" class="mask" style="display:none;">
	<div class="title"><span>信息提示</span></div>
	<div class="con">
		<div class="ph"></div>
		<h4>请在新打开的支付窗口上完成充值！</h4>
		<h5>充值完成前请不要关闭此窗口</h5>
		<h5>完成充值后请根据您的情况点击下面的按钮：</h5>
		
		<div class="pbutton">
			<input id="completePay" type="button" class="allbtn" value="已完成充值"/>
			<input id="problemPay" type="button" class="allbtn" value="充值遇到问题"/>
		</div>
		<br/>
	</div>
</div>

</body>
</html>
