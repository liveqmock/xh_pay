<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/page-deal.tld" prefix="page" %>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>交易记录</title>
<link href="<c:url value="/css/base.css" />" type="text/css" rel="stylesheet" />
<link href="<c:url value="/css/pay.css" />" type="text/css" rel="stylesheet" />
<link href="<c:url value="/js/build/menu/assets/c2c-ui.css" />" rel="stylesheet" />
<script type="text/javascript" src="<c:url value="/js/build/kissy.js" />"></script>

<script>
	KISSY.use('../xh/xhpay', function(S, Xhpay) {
		var xhpay = new Xhpay();
		xhpay.init({url:"<c:url value="/consume/gadgetlist.do"/>"});
	});
	
</script>
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
        <div class="account_balance_box" id="recordbox1">
            <ul>
                <li class="cur_account_balance">收支明细</li>
                <li><a href="<c:url value="/order/orderlist.do" />">充值记录</a></li>
            </ul>
        </div>

        <div class="srh_date">
            <div class="start_end_srh">
                <label for="s_date">起止日期:</label>
                <input type="text" class="startdate" value="<c:out value="${param.startTime }" />" readonly="readonly"  id="s_date"/>
                <label class="label_to" for="e_date">至</label>
                <input type="text" class="enddate" value="<c:out value="${param.endTime }" />"  id="e_date"  readonly="readonly"/>
                <button type="button" class="srh_btn fr" id="internal_srh_btn"></button>
            </div>
            <div class="fast_srh" >
                <label>查询时间:</label>
                <ul class="funds_flow" id="funds_date_ul">
                	<li <c:if test="${param.dateTag == 0}">class="funds_currentli"</c:if> datetag="0"><a><span>今天</span></a></li>
                    <li <c:if test="${param.dateTag == 1 || empty param.dateTag}">class="funds_currentli"</c:if> datetag="1"><a><span>一周</span></a></li>
                    <li <c:if test="${param.dateTag == 2}">class="funds_currentli"</c:if> datetag="2"><a><span>一月</span></a></li>
                    <li <c:if test="${param.dateTag == 3}">class="funds_currentli"</c:if> datetag="3"><a><span>三月</span></a></li>
                </ul>
            </div>
        </div>      
		<div class="srh_condition">
            <div class="funds_flow_box">
                <label>资金流向：</label>
                <ul class="funds_flow" id="funds_flow_ul">
                    <li <c:if test="${empty param.fundsFlow}">class="funds_currentli"</c:if> flowtag=""><a><span>全部</span></a></li>
                    <li <c:if test="${param.fundsFlow == 0}">class="funds_currentli"</c:if> flowtag="0"><a><span>收入</span></a></li>
                    <li <c:if test="${param.fundsFlow == 1}">class="funds_currentli"</c:if> flowtag="1"><a><span>支出</span></a></li>
                </ul>
            </div>
            <div class="funds_range_box">
                <label>金额范围：</label>
                <ul class="funds_flow" id="funds_range_ul">
                    <li <c:if test="${param.fundsRange == 0 || empty param.fundsRange}">class="funds_currentli"</c:if> rangetag="0" mintag="0" maxtag="100"><a><span>0-100</span></a></li>
                    <li <c:if test="${param.fundsRange == 1}">class="funds_currentli"</c:if> rangetag="1" mintag="101" maxtag="500"><a><span>101-500</span></a></li>
                    <li <c:if test="${param.fundsRange == 2}">class="funds_currentli"</c:if> rangetag="2" mintag="501" maxtag="1000"><a><span>501-1000</span></a></li>
                    <li <c:if test="${param.fundsRange == 3}">class="funds_currentli"</c:if> rangetag="3" mintag="1001" maxtag=""><a><span>1001及以上</span></a></li>
                </ul>
            </div>
    
		</div>

        <table class="record_tb">
            <tr>
                <th width="8%">创建时间</th>
                <th width="20%" class="tl">名称|交易号</th>
                <th width="20%">对方</th>
                <th width="15%">用途</th>
                <th width="20%">金额|明细</th>
                <th>操作</th>
                <th>退款申请</th>
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
                       <c:choose>
							<c:when test="${gadgets.orderType == 1 }">
								<td title="已申请">退款申请</td>
							</c:when>
							<c:otherwise>
								<td><a class="refund" id="${gadgets.id }" href="<c:url value="/user/refund/apply.do?orderId=${gadgets.orderId }&refundMoney=${gadgets.money }" />">退款申请</a></td>
							</c:otherwise>
						</c:choose>
				</tr>
			</c:forEach>
            <tr>
                <td colspan="4" style=" text-align:left;">已完成支出9笔共<span class="out_count"> 10546.01</span> 元，进行中0笔共<span class="out_count">  0.00 </span>元 | 已完成收入0笔共<span class="in_count"> 0.00 </span>元，进行中0笔共<span class="in_count"> 0.00 </span>元</td>
                <td colspan="3" class="totalcount">
                
					<div class="numpage">
				        <c:url var="url" value="/consume/gadgetlist.do?dateTag=${param.dateTag }&startTime=${param.startTime }&endTime=${param.endTime }&fundsFlow=${param.fundsFlow }&fundsRange=${param.fundsRange }&min=${param.min }&max=${param.max }"></c:url>
						<page:pageDeal maxRowCountAN="maxRowCountAN" theme="page" url="${url }"
					             currentPageAN="currentPageAN" maxPageCountAN="maxPageCountAN"/>
                    </div>
            	</td>
            </tr>
                    
		</table>

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
	
	
	<script type="text/javascript">
    KISSY.use('',function(S){			
        //选择tab
        var allLi=S.all('li','.paynav');
        S.all(allLi[1]).addClass('currentli');         
    });
	</script>
 <script type="text/javascript">
    KISSY.use('', function(S) {
		var $ = KISSY.all;
		$('.close-de','.fail').on("click",function(){
			$(".fail").hide();
		});
		$('.close-de','.suc').on("click",function(){
			$(".suc").hide();
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
	});
</script>  
</body>
</html>