<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/page-deal.tld" prefix="page" %>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>账户管理</title>
<link href="<c:url value="/css/base.css" />" type="text/css" rel="stylesheet" />
<link href="<c:url value="/css/pay.css" />" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<c:url value="/js/build/kissy-min.js" />"></script>

</head>

<body>
    
    <!-- 首部  start -->
	<%@ include file="/WEB-INF/views/include/header.jsp" %>
	<!-- 首部 end -->
    
    <div class="paylogo">
        <img src="<c:url value="/images/payLogo.png" />" />
    </div>
    
    
	<!-- 首部导航  start -->
	<%@ include file="/WEB-INF/views/include/contain_header.jsp" %>
	<!-- 首部导航 end -->
    
    
    <div class="paycontent">
        <div class="account_balance_box">
            
            <span class="cur_account_balance">账户余额</span>
            
        </div>
        <div class="balance_box">
            <div class="cash_balance bor" id="cash_balance">
                <img src="<c:url value="/images/pay_img.jpg" />" class="pay_img"/>
                <div class="fl">
                    <p><span>欢迎您！</span>账户名：${account.loginName }</p>
                    <p>现金余额:<span class="ft24"><fmt:formatNumber value="${account.money }" pattern="#,##0.00"/></span>元</p>
                    
                </div>
                <button type="button" class="recharge_btn" id="recharge_btn" onclick="window.location.href='<c:url value="/pay/charge.do" />';">充值</button>
            </div>
			<c:if test="${depositStatus == 0 }">
				<div class="cash_balance" id="balance">
	                <div class="fl">
	                    <p class="no_bond">保证金金额：<fmt:formatNumber value="${sysDeposit }" pattern="#,##0.00"/> 元</p>
	                    <p><span>已缴纳：<fmt:formatNumber value="${account.deposit }" pattern="#,##0.00"/>元 </span></p>
	                    
	                </div>
	                <button type="button" class="recharge_btn ex_bg">缴纳</button>
	            </div>
	        </c:if>
            <c:if test="${depositStatus == 1 }">
				<div class="cash_balance" id="balance">
	                <div class="fl">
<!-- 	                    <p class="no_bond">您还没有缴纳足额的保证金!</p> -->
<%-- 	                    <p>缴纳的保证金金额：<span class="ft24"><fmt:formatNumber value="${account.deposit }" pattern="#,##0.00"/></span>元</p> --%>
<%-- 	                    <p>还需缴纳：<span class="ft24"><fmt:formatNumber value="${shortfall }" pattern="#,##0.00"/></span>元</p> --%>
	                    <p class="no_bond">保证金金额：<fmt:formatNumber value="${sysDeposit }" pattern="#,##0.00"/> 元</p>
	                    <p><span>已缴纳：<fmt:formatNumber value="${account.deposit }" pattern="#,##0.00"/>元</span> <span class="spacectr">还需缴纳：</span><span class="ft24"><fmt:formatNumber value="${shortfall }" pattern="#,##0.00"/></span>元</p>
	                </div>
	                <button type="button" class="recharge_btn" onclick="window.location.href='<c:url value="/pay/deposit.do" />';">缴纳</button>
	            </div>
			</c:if>
        </div>
        <div class="account_balance_box" id="recordbox1">
            <ul>
                <li class="cur_account_balance">最近交易明细</li>
                <li><a href="<c:url value="/consume/gadgetlist.do" />">交易明细</a></li>
                <li><a href="<c:url value="/order/orderlist.do" />">充值记录</a></li>
            </ul>
        </div>

            <div class="account_tabbox1" id="">
                <table class="record_tb">
                    <tr>
                        <th width="8%">创建时间</th>
                        <th width="20%" class="tl">名称|交易号</th>
                        <th width="20%">对方</th>
                        <th width="20%">用途</th>
                        <th width="15%">金额</th>
                        <th>操作</th>
                        <th>退款信息</th>
                    </tr>

					<c:forEach var="gadgets" items="${gadgetlist}" varStatus="i">
						<tr>
							<td class="td_time"><fmt:formatDate value="${gadgets.addTime }" type="both" pattern="yyyy-MM-dd HH:mm:ss" /></td>
	                        <td  class="tl">${gadgets.orderType }|${gadgets.orderId }</td>
	                        <td>${gadgets.appName }</td>
	                        <td>${gadgets.pname }</td>
	                        <c:if test="${gadgets.type == 0 }">
	                        	<td class="add_m">+<fmt:formatNumber value="${gadgets.money }" pattern="#,##0.00"/></td>
	                        </c:if>
	                        <c:if test="${gadgets.type == 1 }">
	                        	<td class="reduce_m">-<fmt:formatNumber value="${gadgets.money }" pattern="#,##0.00"/></td>
	                        </c:if>
	                        <%-- 操作失败 <td class="wait_pay">+50</td> --%>
	                        <td><a class="xiangqing" id="${gadgets.id }">详情</a></td>
	                        <!-- 当gadgets.canRefund == 1在退款时间范围内，否则不在退款时间范围内-->
							<c:choose>
								<c:when test="${gadgets.canRefund == 1 }">
									<c:choose>
										<c:when test="${gadgets.refundStatus == 0 or gadgets.refundStatus == 2 or gadgets.refundStatus == 3}">
											<td><a class="refund" id="${gadgets.id }" href="<c:url value="/user/refund/apply.do?orderId=${gadgets.orderId }&refundMoney=${gadgets.money }" />">退款申请</a></td>
										</c:when>
										<c:otherwise>
											<td><a class="refunddetail" id="${gadgets.orderId }" >退款详情</a></td>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${gadgets.refundStatus == 0}">
											<td title="不符合条件,已超过退款期限">退款申请</td>
										</c:when>
										<c:otherwise>
											<td><a class="refunddetail" id="${gadgets.orderId }" >退款详情</a></td>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
						</tr>
					</c:forEach>
					
                    <tr>
                        <td colspan="7" class="totalcount">
                        	<div class="numpage">
						        <c:url var="url" value="/account/index.do"></c:url>
								<page:pageDeal maxRowCountAN="maxRowCountAN" theme="page" url="${url }"
							             currentPageAN="currentPageAN" maxPageCountAN="maxPageCountAN"/>   
                            </div>
                        </td>
                    </tr>
                    
                </table>
            </div>
            
	</div>

	<!-- 常见问题 start -->
	<%@ include file="/WEB-INF/views/include/faq.jsp" %>
	<!-- 常见问题 end -->
     
	<!-- 底部 start -->
	<%@ include file="/WEB-INF/views/include/footer.jsp" %>
	<!-- 底部 end -->
	<div class="checkbg-de fail" >
		<div class="checkbox-de">
			<div class="checkmessage">
				<a class="close-de"> </a>
				<div class="de-error">
					<span class="de-img"></span>
					<span class="de-miaoshu">网络连接异常，请稍后再试</span>
				</div>
	        </div>    
	    </div>
	</div>
	<div class="checkbg-de suc">
		<div class="checkbox-de">
			<div class="checkmessage">
				<a class="close-de"> </a>
				 <table>
                    <tr><td width="20%" class="pdr20 ">应用名称：</td><td style="color:#006fa7" width="30%" class="tdbg appName">网上银行</td><td width="20%" class="pdr20">付款后余额：</td><td style="color:#f00;" class="tdbg afterMoney">300</td></tr>
                    <tr><td  class="pdr20">商品名称：</td><td class="tdbg pName" >竞拍保证金</td><td  class="pdr20">类型：</td><td style="color:#006fa7" class="tdbg orderType">保证金</td></tr>
                    <tr><td class="pdr20" >交易时间：</td><td class="tdbg addtime" >2013-01-11 12:45</td><td  class="pdr20">状态：</td><td class="tdbg statusName" >支出</td></tr>
                   <!--   <tr><td  class="pdr20">交易号：</td><td class="tdbg trxId"  >E20130111245</td><td rowspan="3"></td><td rowspan="3" class="tdbg"></td></tr>-->
                    <tr><td  class="pdr20">订单号：</td><td class="tdbg orderId" >D130110130111245</td></tr>
                    <tr><td  class="pdr20">金额：</td><td class="tdbg money"  >5000.00</td></tr>
                </table>
	        </div>    
	    </div>
	</div>
	<div class="checkbg-de refundsuc">
		<div class="checkbox-de">
			<div class="checkmessage">
				<a class="close-de"> </a>
	 			<table class="refundtable">
         
     			</table>
     		</div>    
	    </div>
	</div>
	
 <script type="text/javascript">
    KISSY.use('', function(S) {
		var $ = KISSY.all;
		$('.close-de','.fail').on("click",function(){
			$(".fail").hide();
		});
		$('.close-de','.suc').on("click",function(){
			$(".suc").hide();
		});
		$('.close-de','.refundsuc').on("click",function(){
			$(".refundsuc").hide();
		});
		$(".xiangqing",'.record_tb').on("click", function() {
			var docHeight=S.DOM.docHeight ();
			var docWidth = S.DOM.docWidth();
			var data={};
			data.id=$(this).attr('id');
			S.IO({
				url:'<c:url value="/consume/detail" />',
			    type:"post",
			    processData:true,
			    data:data,
			    dataType:'json',			
			    success:function (d) {
			        if(d.code===1){
			        	$(".fail").width(docWidth);
						$(".fail").height(docHeight);
						console.log( $(".fail") );
						$(".fail").show();
						return false;
			        }else if(d.code===0){
			        	$('.appName').text(d.appName);
						$('.afterMoney').text(d.afterMoney);
						S.log(d);
						$('.pName').text(d.pname);
						$('.orderType').text(d.orderTypeName);
						$('.addtime').text(d.addTime);
						$('.statusName').text(d.typeName);
						//$('.trxId').text();
						$('.orderId').text(d.orderId);
						$('.money').text(d.money);
						$(".suc").width(docWidth);
						$(".suc").height(docHeight);
						console.log( $(".fail") );
						$(".suc").show();
						return false;
			        }
			    }
			});
		});
		
		
		$(".refunddetail",'.record_tb').on("click", function() {
			var docHeight=S.DOM.docHeight ();
			var docWidth = S.DOM.docWidth();
			var data={};
			data.id=$(this).attr('id');
			S.IO({
				url:'<c:url value="/user/refund/step.do" />',
			    type:"post",
			    processData:true,
			    data:data,
			    dataType:'json',			
			    success:function (d) {
			        if(d.code===1){
			        	$(".fail").width(docWidth);
						$(".fail").height(docHeight);
						console.log( $(".fail") );
						$(".fail").show();
						return false;
			        }else if(d.code===0){
			        	$(".refundtable").html("");	        	
			        	S.each(d.refundList, function(item) {
			        	    $(".refundtable").append("<tr><td  class='pdr20'>订单号：</td><td class='tdbg orderId' >" + item.orderId + "</td><td  class='pdr20'>操作时间：</td><td class='tdbg formateDate' > " + item.formateDate + "</td></tr>");
			        	    $(".refundtable").append("<tr><td  class='pdr20'>状态：</td><td class='tdbg statusName' >" + item.statusName + "</td><td  class='pdr20'>步骤：</td><td class='tdbg stepName' > " + item.stepName + "</td></tr>");
			        	    $(".refundtable").append("<tr><td  class='pdr20'>原因：</td><td class='tdbg reason' colspan='3'>" + item.reason + "</td></tr>");
			        	});
			        	S.log(d);
			        	$(".refundsuc").width(docWidth);
						$(".refundsuc").height(docHeight);
						console.log( $(".fail") );
						$(".refundsuc").show();
						return false;
			        }
			    }
			});
		});		
		
	});
	</script>
	<script type="text/javascript">
	    KISSY.use('',function(S){			
	        //选择tab
	        var allLi=S.all('li','.paynav');
	        S.all(allLi[0]).addClass('currentli');         
	    });
	</script>
</body>
</html>