<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>首页</title>
<style type="text/css">
	.table{
		border: 0px solid red;
		font-size:26px;
	}
	.table td{
		line-height: 250%;
	}
	.tableinner{
		border: 0px solid red;
		font-size:16px;
		padding-left:39px;
	}
</style>
</head>
<body>
<h1>系统功能演示</h1>
<table class="table">
	<tr>
		<td>
			<a style="color:blue;">汇付天下</a>
			<table class="tableinner">
				<tr>
					<td><a style="color:graytext;" href="<%=path%>/order/chinapnr/queryStatus.do?orderId=20140102110802222234"
						target="_blank">查询订单状态</a></td>
				</tr>
				<tr>
					<td><a style="color:graytext;" href="<%=path%>/order/chinapnr/queryOrder.do?orderId=20140102110802222234"
						target="_blank">查询订单明细</a></td>
				</tr>
				<tr>
					<td><a style="color:graytext;" href="<%=path%>/order/chinapnr/paymentConfirm.do?orderId=20140102110802222234"
						target="_blank">订单结算</a></td>
				</tr>
				<tr>
					<td><a style="color:graytext;" href="<%=path%>/order/chinapnr/refundOrder.do?oldOrdId=20140102110802222231"
						target="_blank">退款</a></td>
				</tr>
				<tr>
					<td><a style="color:graytext;" href="<%=path%>/order/chinapnr/queryRefundStatus.do?orderId=20140102110802222231"
						target="_blank">退款状态查询</a></td>
				</tr>
				
				<tr>
					<td><a style="color:graytext;" href="<%=path%>/order/allinpay/queryOrder.do?orderId=20140304040346854266"
						target="_blank">通联支付查询订单明细</a></td>
				</tr>
				<tr>
					<td><a style="color:graytext;" href="<%=path%>/order/allinpay/refundOrder.do?oldOrdId=20140304050808003865"
						target="_blank">通联支付退款</a></td>
				</tr>
				<tr>
					<td><a style="color:graytext;" href="<%=path%>/order/alipay/refundOrder.do?oldOrdId=20140311085652777477"
						target="_blank">支付宝退款</a></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</body>
</html>