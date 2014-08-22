<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
 <div class="paytopbox">
     <div class="paytop">
     	<a class="quit" href="<c:url value="/logout" />">[退出]</a>
         <a href="pay_transaction_records.html" target="_blank">帮助</a>
         <a href="http://my.xuan.news.cn/" target="_blank">我的空间</a>
         <a href="<c:url value="/" />">${account.loginName }</a>
     </div>
 </div>