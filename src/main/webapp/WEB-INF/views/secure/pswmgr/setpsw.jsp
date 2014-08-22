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
<title>账户管理</title>
<link href="<c:url value="/css/base.css" />" type="text/css" rel="stylesheet" />
<link href="<c:url value="/css/pay.css" />" type="text/css" rel="stylesheet" />
<link href="<c:url value="/css/verification.css" />" type="text/css" rel="stylesheet" />
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
            <span class="cur_account_balance">启用支付密码</span>           
        </div>
      <div class="safe-con">
			<div class="safe-mail">
				<div class="pay-psd2"> </div>
			</div>
			<div class="mail-con">
				<form action="<c:url value="/pswmgr/savepsw.do" />" id="space-form" method="post">
					<div class="reset-con">
						<p><span class="reset-tilte">电子邮件：</span><span class="reset-account">${account.email }</span></p>
						<p><span class="reset-tilte reset-psdcon">支付密码：</span><input id="new-psd" type="password" name="paypsw"/><span class="psd-tips">(请输入8位或以上字符)</span></p>
						<p><span class="reset-tilte reset-psdcon">确认支付密码：</span><input id="new-psdconf" type="password" name="repaypsw" /><span class="psd-tips">(请输入8位或以上字符)</span></p>
<!-- 						<p><span class="reset-tilte reset-psdcon">验证码：</span><input class="yanzhengma" id="yanzhengma" type="text"/><span class="yanzhengma-tips">(请从邮箱中查看)<a id="mail-name" href="#">去邮箱</a></span></p> -->
						<a class="confirm-btn">确定</a>
					</div>
				</form>
			</div>
			<div class="safe-tips">
				<span class="tip-title">为什么要进行身份验证？</span>
				<p>
					1.为保障您的账户信息安全，在变更账户中的重要信息时需要进行身份验证，感谢您的理解与支持。
				</p>
				<p>
					2.验证身份遇到问题？请发送用户名、手机号、历史订单发票至shensu@jd.com，客服将尽快联系您。
				</p>
			</div>
		</div>
    </div>
       <script type="text/javascript">
	       KISSY.config({
				debug : true,
				packages :[{
					name : 'xh',
					path : '<c:url value="/" />js'
				}]
			});
		</script>
		<script type="text/javascript">
			KISSY.use('node,xh/validator',function(S,Node,Validator){
				var flag=S.all('.reset-account').text();
				var mailArr=flag.split('@');
				S.all('#mail-name').attr('href','https://mail.'+mailArr[1]);
				//选择tab
				var allLi=S.all('li','.paynav');
				S.all(allLi[2]).addClass('currentli');
				//表单验证
				var validator = new Validator('#space-form', {
		            warn: 'XhWarn',  //指定使用消息提示类
		            event: 'blur' //事件
				});
				
				validator.add('#new-psdconf',{
					required: true,
					minLength : [8,'密码不能少于8位'],
					equalTo: ['#new-psd','两次输入不一致。'] 
				});
				validator.add('#new-psd',{
					required: true,
					minLength : [8,'密码不能少于8位']
				});
				//表单提交
				S.all('.confirm-btn').on('click',function(){
				    var result = validator.isValid();
				    if(result){
				  		S.DOM.get('#space-form').submit();
				    }
				});
			});
		</script>
</body>
</html>
