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
            <span class="cur_account_balance">邮箱验证</span>           
        </div>
       <div class="safe-con">
			<div class="safe-mail">
				<div class="mail-step1"></div>
			</div>
			<div class="mail-con">
					<div class="receive-mail">
						<p class="receive-ctr">
							<span class="mail-img"> </span><span class="mail-name">邮件已发送到您的邮箱 <a id="mail-name"> </a>。请按邮箱中提示操作，完成身份验证。</span>
						</p>
					</div>
					<div class="reset-con">
					     <form id="email-form" name="email-form"  method="post">
							<p id="input-mail"><span class="reset-tilte reset-psdcon">您的邮箱：</span><input id="user-mail" name="emailID" type="text"/><a id="send-mail" class="yanzheng">发送</a></p>
							<p id="show-mail"><span class="reset-tilte">您的邮箱：</span><span class="reset-account"> </span><a id="change-mail" class="xiugai-mail">修改</a></p>
							<p class="hide-ctr"><span class="reset-tilte reset-psdcon">验证码：</span><input class="yanzhengma" id="yanzhengma" name="captcha" type="text"/><span class="yanzhengma-tips">(请<a id="go-mail" href="#">登录邮箱</a>查看您的验证码，填写验证码)</span></p>
							<p class="hide-ctr"><a class="confirm-btn ">下一步</a></p>
						</form>
					</div>
				<div class="receive-mail">
					<div class="question-report">
						<ul class="receive-ctr">
							<li><span>如果长时间没有收到邮件请检查是否在垃圾邮件中。如果还没收到请重新发送</span></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="safe-tips">
				<span class="tip-title">为什么要进行身份验证？</span>
				<p>
					1.为保障您的账户信息安全，在变更账户中的重要信息时需要进行身份验证，感谢您的理解与支持。
				</p>
				<p>
					2.验证身份遇到问题？请发送用户名、手机号、历史订单发票至shensu@xinhua.cn，客服将尽快联系您。
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
			KISSY.use('xh/validator,xh/dialog',function(S,Validator,Dialog){
				//倒计时器
				function delayURL() {
					var delay = KISSY.all('#send-time').html();
					if(delay > 0) {
						delay--;
						S.all('#send-time'). html(delay);
					} else {
						S.all('.repeat-send').hide();
						S.all('.can-send').css('display','inline-block');
						S.all('#send-mail').show();
					}
					setTimeout(delayURL, 1000);
				}
				var dTips=new Dialog();
				//发送邮箱验证
				var flag=true;
				S.all('#send-mail').on('click',function(){
					var email=S.all('#user-mail').val();
					
					if(email!=''){
						S.IO({
							url:'<c:url value="/email/submitemail.do" />',
						    type:"post",
						    processData:true,
						    data:{
						    	"emailID":email
						    },
						    dataType:'json',
						
						    success:function (d) {
						        if(d.code===0){
						        	S.all('#input-mail').hide();
						        	var showMail=S.all('#show-mail').show();
						        	S.all('.reset-account',showMail).text(email);
									S.all('.hide-ctr').fadeIn(0.8); 
									S.all('.receive-mail').fadeIn(0.8);
									S.all('#mail-name').text(S.all('#user-mail').val());
									S.all('#send-time').text(60);
									S.all('.repeat-send').show();
									S.all('.can-send').css('display','none');
// 									if(flag){
// 										delayURL();	
// 									}
// 									flag=false;
									dTips.successTip({msg: d.message, fun:function(){}});
						        }else if(d.code===1){
						        	dTips.failureTip({msg:d.message,fun:function(){}});
						        }
						    }
						});
						
					}											
				});	
				//修改邮箱操作
				S.all('.xiugai-mail').on('click',function(){
					S.all('#input-mail').show();
					S.all('#show-mail').hide();
					S.all('.hide-ctr').hide();
					S.all('.receive-mail').hide();
					S.all('#yanzhengma').val('');
				});
				//验证码填写完毕，执行“下一步”
				S.all('.confirm-btn').on('click',function(){
					var captcha=S.all('#yanzhengma').val();
					var email=S.all('#user-mail').val();
					var formData={};
					formData.captcha=captcha;
					formData.emailID=email;
					if(captcha!=''){
						S.IO({
							url:'<c:url value="/email/submitcaptcha.do" />',
						    type:"post",
						    data:formData,
						    dataType:'json',
						    success:function (d) {
						        if(d.code===0){
						        	window.top.location.href=d.url;
						        }else if(d.code===1){
						        	dTips.failureTip({msg:d.message,fun:function(){}});
						        }
						    }
						});
					}
				});
				S.all('#mail-name').on('click',function(){
					var tempmail=S.all('.reset-account').text();
					var arrmail=tempmail.split('@');
					window.open('http://mail.'+arrmail[1]);
				});
				S.all('#go-mail').on('click',function(){
					var flag=S.all('#user-mail').val();
					var mailArr=flag.split('@');
					window.open('http://mail.'+mailArr[1]);
				});
				//选择tab
				var allLi=S.all('li','.paynav');
				S.all(allLi[2]).addClass('currentli'); 
				//表单验证
				var validator = new Validator('#email-form', {
		            warn: 'XhWarn',  //指定使用消息提示类
		            event: 'blur' //事件
				});
				
				validator.add('#user-mail', {
					required: true, //必须
					email: ['请填写正确的电子邮件地址'] //电子邮件
				});
			});
		</script>
</body>
</html>
