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
		xhpay.init({url:"<c:url value="/order/orderlist.do"/>"});
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
         <div class="account_balance_box" id="recordbox2">
            <ul>
                <li><a href="<c:url value="/consume/gadgetlist.do" />">收支明细</a></li>
                <li class="cur_account_balance">充值记录</li>
            </ul>
        </div>
        
        <div class="paynav2_content">
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
                <table class="record_tb">
                    <tr>
                        <th width="8%">创建时间</th>
                        <th width="20%" class="tl">订单号</th>
                        <th width="20%" class="tl">交易号</th>
                        <th width="20%">充值类型</th>
                        <th width="8%">金额</th>
                        <th width="15%">状态</th>
                        <th>操作</th>
                    </tr>
                    
            <c:forEach var="orders" items="${orderlist}" varStatus="i">
                    <tr>
                        <td class="td_time"><fmt:formatDate value="${orders.payTime }" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td  class="tl">${orders.appOrderId }</td>
                        <td>${orders.id }</td>
                        <td>${orders.payTypeName }</td>
                        <td class="add_m">+<fmt:formatNumber value="${orders.money }" pattern="#,##0.00"/></td>
                        <td>
							<c:if test="${orders.payStatus == 0}">
					          	<font color="red">${orders.payStatusName }</font>
							</c:if>
							<c:if test="${orders.payStatus == 1}">
					          	<font color="green">${orders.payStatusName }</font>
							</c:if>
							<c:if test="${orders.payStatus != 1 and orders.payStatus != 0}">
					          	${orders.payStatusName }
							</c:if>
                        </td>
                        <td><a href="">详细</a></td>
                    </tr>
            </c:forEach>
                    
            
                    <tr>
                        <td colspan="4" style=" text-align:left;">已完成充值共9笔<span class="out_count"> 10546.01</span> 元，进行中0笔共<span class="out_count">  0.00 </span>元</td>
                        <td colspan="3" class="totalcount">
                            <div class="numpage">
                            
					        <c:url var="url" value="/order/orderlist.do"></c:url>
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
	<script type="text/javascript">
    KISSY.use('',function(S){			
        //选择tab
        var allLi=S.all('li','.paynav');
        S.all(allLi[1]).addClass('currentli');         
    });
	</script>
</body>
</html>