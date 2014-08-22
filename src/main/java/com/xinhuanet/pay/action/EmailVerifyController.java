package com.xinhuanet.pay.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.xinhuanet.pay.cache.logic.AuthCodeCache;
import com.xinhuanet.pay.common.BaseController;
import com.xinhuanet.pay.exception.LoginUserNotFoundException;
import com.xinhuanet.pay.log.OperatorLog;
import com.xinhuanet.pay.po.Account;
import com.xinhuanet.pay.service.AccountService;
import com.xinhuanet.pay.service.MailService;
import com.xinhuanet.pay.util.Function;

@Controller
public class EmailVerifyController extends BaseController {

	private @Autowired OperatorLog operatorLog;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private AccountService accountService;
	
	private @Autowired AuthCodeCache authCodeCache;
	
	/**
	 * 进入邮箱验证页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "email/emailverify.do")
	public String Email(HttpServletRequest request, HttpServletResponse response) {
		operatorLog.requestOperLog(request, "XXXXXXXXXXXXXXEmail");
		return "secure/email/emailindex";
	}
	
	/**
	 * 接收邮箱账号并发送验证码
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="email/submitemail.do")
	@ResponseBody
	public String EmailVerifyOne(HttpServletRequest request, HttpServletResponse response){
		String userName = "";
		String emailID = request.getParameter("emailID");
		String subject = "新华网支付邮箱认证验证";
		
		int checkRt = accountService.queryAccountByEmail(emailID);
		JSONObject json = new JSONObject();
		if(checkRt>0){
			json.put("code", 1);
			json.put("message", "该邮箱账号已经认证！");
			return json.toJSONString();
		}
		try {
			userName = getLoginName(request, response);
		} catch (LoginUserNotFoundException e) {
			e.printStackTrace();
		}
		String captcha = Function.getFixLenthString(6);
		try {
			//将验证码放入缓存中
			authCodeCache.setPayEmailCode(userName, captcha);
		} catch (Exception e1) {
			logger.info("写入缓存错误！");
			json.put("code", 1);
			json.put("message", "系统错误，请您稍后重试！");
			return json.toJSONString();
		}
		/**发送邮件*/
		try {
			mailService.sendMail(emailID, null, subject, getVerifyContent(userName, captcha), null, null);
			logger.info("已发送邮件，邮箱号为："+emailID+"  验证码："+captcha);
		} catch (Exception e) {
			json.put("code", 1);
			json.put("message", "邮件发送失败，请您稍后重试！");
			return json.toJSONString();
		}
		json.put("code", 0);
		json.put("message", "邮件发送成功！");
		return json.toJSONString();
	}
	
	/**
	 * 校验验证码并返回结果
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="email/submitcaptcha.do")
	@ResponseBody
	public  String EmailVerifyTwo(HttpServletRequest request, HttpServletResponse response){
		//验证码效验
		String captcha = StringUtils.trim(request.getParameter("captcha"));//获取输入的验证码
		String userName = "";
		String userId = "";
		JSONObject json = new JSONObject();
		try {
			userName = getLoginName(request, response);
			userId = getUserId(request, response);
		} catch (LoginUserNotFoundException e) {
			logger.info("获取用户信息失败！");
			json.put("code", 1);
			json.put("message", "获取用户信息失败，请重新登录！");
			return json.toJSONString();
		}
		
		String emailID = request.getParameter("emailID");
		String sercaptcha = "";
		try {
			sercaptcha = authCodeCache.getPayEmailCode(userName);
		} catch (Exception e) {
			logger.info("读取缓存错误！");
			json.put("code", 1);
			json.put("message", "系统错误，请您稍后重试！");
			return json.toJSONString();
		}//取出服务器发送的验证码
		logger.info("接收验证码："+captcha+"   服务器验证码： "+sercaptcha);
		if((captcha!=null&&sercaptcha!=null)&&captcha.equals(sercaptcha)){//如果两个验证码一致——操作数据库
			boolean b = accountService.updAccountEmail(userId, userName, emailID);
//			if(b){
////				request.setAttribute("emailID", emailID);
////				request.setAttribute("message", "恭喜，邮箱已验证成功!");
////				return "emailverify/emailresult";
//				logger.info("邮箱已认证成功！");
//				json.put("code", 0);
//				json.put("message", "邮箱认证成功！");
//				
//				return json.toJSONString();
//			}else{
////				System.out.println("邮箱认证失败！");
////				request.setAttribute("message", "邮箱认证失败！");
////				return "emailverify/emailresult";
//				logger.info("邮箱认证失败！");
//				json.put("code", 1);
//				json.put("message", "邮箱认证失败！");
//				
//				return json.toJSONString();
//			}
			logger.info("邮箱已认证完成！");
			json.put("code", 0);
			json.put("message", "邮箱认证完成！");
			json.put("url", request.getContextPath() + "/email/res?emailID="+emailID);
			return json.toJSONString();
		}else{//提示错误，重新发送邮件或者更改邮箱号再次发送
			logger.info("验证码错误！");
			json.put("code", 1);
			json.put("message", "验证码错误！");
			return json.toJSONString();
		}
	}
	
	@RequestMapping(value="/email/res")
	public ModelAndView returnRes(HttpServletRequest request, HttpServletResponse response){
		//获取账户信息
		Account account = this.getAccount(request,response);
		String emailID = request.getParameter("emailID");
		ModelAndView mav = new ModelAndView();
		if(account.getEmailStatus() == 1){	//邮箱认证成功
			mav.addObject("message", "邮箱认证成功");
			mav.addObject("code", 0);
		} else{ //邮箱认证失败
			mav.addObject("message", "邮箱认证失败");
			mav.addObject("code", 1);
		}
		mav.addObject("emailID", emailID);
		mav.setViewName("secure/email/emailresult");
		return mav;
	}
	/**
	 * @param userName
	 * @return
	 */
	public static String getVerifyContent(String userName,String captcha){
		StringBuffer msgContent = new StringBuffer("");//设置消息内容
		msgContent.append("尊敬的"+userName+"<br></br>");
		msgContent.append("<p>您好!你正在使用新华支付系统进行邮箱认证，本次认证验证码为："+captcha+"<p><br></br>");
		msgContent.append("<p>该验证码15分钟有效、请尽快进行验证！<p><br></br>");
		msgContent.append("<p>如非本人操作请勿理会！<p><br></br>");
		msgContent.append("<p>新华网管理中心！<p><br></br>");
		msgContent.append("<p>"+Function.getDateTimeString()+"<p><br></br>");
		return msgContent.toString();
	}
	
}
