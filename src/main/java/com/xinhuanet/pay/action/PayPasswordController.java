package com.xinhuanet.pay.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.news.xhsso.util.HttpUtil;

import com.alibaba.fastjson.JSONObject;
import com.xinhuanet.pay.cache.logic.AuthCodeCache;
import com.xinhuanet.pay.common.BaseController;
import com.xinhuanet.pay.po.Account;
import com.xinhuanet.pay.service.MailService;
import com.xinhuanet.pay.service.PayPasswordService;
import com.xinhuanet.pay.util.Function;
import com.xinhuanet.pay.util.Md5Util;

/**
 * 支付密码管理
 * @author Ronny
 *
 */
@Controller
public class PayPasswordController extends BaseController{
	
	private @Autowired PayPasswordService payPswService;
	private @Autowired AuthCodeCache authCodeCache;
	
	private @Autowired MailService mailService;
	

	private String getLogMsg(HttpServletRequest request){
		String ip = HttpUtil.getIpAddr(request);
		return "ip为"+ip+" ";
	}
	
	/**
	 * 发送验证码到邮件
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/pswmgr/sendcode")
	@ResponseBody
	public String sendValidatekCode(HttpServletRequest request, HttpServletResponse response){
		Account account = super.getAccount(request, response);
		String email = account.getEmail();
		String code = Function.getCode(6);
		JSONObject json = new JSONObject();
		logger.info("用户["+account.getLoginName()+"],userId:["+account.getUid()+"]申请开通支付密码,向["+email+"]发送验证码,"+getLogMsg(request)+":code:"+code);
		if(account.getEmailStatus()!=1){
			json.put("code", 1);
			json.put("message", "您的邮箱尚未认证，请认证后再启用支付密码");
			return json.toJSONString();
		}
		try {
			authCodeCache.setPayPswCode(account.getLoginName(),code);
			mailService.sendMail(email, null, "新华支付--开启支付密码", getVerifyContent(account.getLoginName(),code), null, null);
		} catch (Exception e) {
			e.printStackTrace();
			json.put("code", 1);
			json.put("message", "系统错误，请您稍后重试！");
			return json.toJSONString();
		}
		json.put("code", 0);
		json.put("message", "邮件发送成功");
		return json.toJSONString();
	}
	
	/**
	 * 设置支付密码
	 * @return
	 */
	@RequestMapping(value="/pswmgr/setpsw", method= RequestMethod.GET)
	public String setPayPsw(HttpServletRequest request, HttpServletResponse response){
		Account account = super.getAccount(request, response);
		logger.info("用户["+account.getLoginName()+"],userId:["+account.getUid()+"]申请开通支付密码,验证用户身份,用户输入验证码，"+getLogMsg(request));
		if(account.getEmailStatus()!=1){
			request.setAttribute("message","用户未通过邮箱认证，不能设置支付密码！");
			return "/pswmgr/error";
		}
		
		request.setAttribute("email", account.getEmail());
		
		String viewName = (String)request.getAttribute("findpsw");
		if(!StringUtils.isEmpty(viewName)){
			return viewName;
		}
		return "secure/pswmgr/checkcode";
	}
	
	/**
	 * 设置支付密码
	 * @return
	 */
	@RequestMapping(value="/pswmgr/setpsw", method= RequestMethod.POST)
	public String setPayPswDo(HttpServletRequest request, HttpServletResponse response){
		Account account = super.getAccount(request, response);
		logger.info("用户["+account.getLoginName()+"],userId:["+account.getUid()+"]申请开通支付密码,确认用户身份,检验用户邮箱收到的验证是否正确,"+getLogMsg(request));
//		HttpSession session = request.getSession();
//		String c = (String)session.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		String parm = (String) request.getParameter("authCode");
		String emailCode = "";
		try {
			emailCode = authCodeCache.getPayPswCode(account.getLoginName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(StringUtils.isNotBlank(emailCode) && StringUtils.isNotBlank(parm) && emailCode.equalsIgnoreCase(parm)){
			String viewName = request.getParameter("findpsw");
			if(!StringUtils.isEmpty(viewName)){
				return viewName;
			}
			return "secure/pswmgr/setpsw";
		}
		request.setAttribute("code", 1);
		request.setAttribute("message", "验证码错误");
		return setPayPsw(request, response);
	}
	
	/**
	 * 保存支付密码
	 * @return
	 */
	@RequestMapping(value="/pswmgr/savepsw", method= RequestMethod.POST)
	public String savePayPsw(HttpServletRequest request, HttpServletResponse response){
		
		Account account = super.getAccount(request, response);
		String viewName = request.getParameter("findpsw"); //重置支付密码
		String paypsw = (String) request.getParameter("paypsw");
		
		if(StringUtils.isNotBlank(paypsw)){
			logger.info("用户["+account.getLoginName()+"],userId:["+account.getUid()+"]支付密码开启成功,"+getLogMsg(request));
			payPswService.updatePayPsw(account.getLoginName(), Md5Util.MD5Encode(paypsw));
			request.setAttribute("message", "支付密码开启成功!");
			StringBuffer sb = new StringBuffer("您的支付密码已经修改为：");
			paypsw = paypsw.substring(0,paypsw.length()-3)+"***";
			sb.append(paypsw);
			try {
				mailService.sendMail(account.getEmail(), null, "新华支付--修改支付密码", sb.toString(), null, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			request.setAttribute("code", 0);
			request.setAttribute("message", "启用支付密码成功");
			
			if(!StringUtils.isEmpty(viewName)){
				return viewName;
			}
			
			return "secure/pswmgr/success";
		}
		logger.info("用户["+account.getLoginName()+"],userId:["+account.getUid()+"]支付密码开启失败,"+getLogMsg(request));
		request.setAttribute("code", 1);
		request.setAttribute("message", "启用支付密码失败");
		if(!StringUtils.isEmpty(viewName)){
			request.setAttribute("findpsw", "secure/pswmgr/findpswcheckcode");
		}
		return setPayPsw(request, response);
	}
	
	/**
	 * 找回密码
	 * @return
	 */
	@RequestMapping(value = "/pswmgr/findpsw", method= RequestMethod.GET)
	public String findPayPsw(HttpServletRequest request, HttpServletResponse response){
		Account account = super.getAccount(request, response);
		logger.info("用户["+account.getLoginName()+"],userId:["+account.getUid()+"]用户找回支付密码,"+getLogMsg(request));
		request.setAttribute("findpsw", "secure/pswmgr/findpswcheckcode");
		return setPayPsw(request,response);
	}
	
	/**
	 * 修改密码
	 * @return
	 */
	@RequestMapping(value = "/pswmgr/changepsw", method= RequestMethod.GET)
	public String changePayPsw(HttpServletRequest request, HttpServletResponse response){
		Account account = super.getAccount(request, response);
		logger.info("用户["+account.getLoginName()+"],userId:["+account.getUid()+"],修改支付密码,"+getLogMsg(request));
		return "secure/pswmgr/changepsw";
		
	}
	
	/**
	 * 修改密码
	 * @return
	 */
	@RequestMapping(value = "/pswmgr/changepsw", method= RequestMethod.POST)
	public String changePayPswDo(HttpServletRequest request, HttpServletResponse response){
		Account account = super.getAccount(request, response);
		logger.info("用户["+account.getLoginName()+"],userId:["+account.getUid()+"],修改支付密码,验证旧密码是否一致"+getLogMsg(request));
		String oldpsw = request.getParameter("oldpsw");
		String newpsw = request.getParameter("newpsw");
		if(account.getPayCode().equals(Md5Util.MD5Encode(oldpsw))){
			logger.info("用户["+account.getLoginName()+"],userId:["+account.getUid()+"]支付密码修改完成,"+getLogMsg(request));
			payPswService.updatePayPsw(account.getLoginName(), Md5Util.MD5Encode(newpsw));
			StringBuffer sb = new StringBuffer("您的支付密码已经修改为：");
			newpsw = newpsw.substring(0,newpsw.length()-3)+"***";
			sb.append(newpsw);
			try {
				mailService.sendMail(account.getEmail(), null, "新华支付--修改支付密码", sb.toString(), null, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			request.setAttribute("code", 0);
			request.setAttribute("message", "修改支付密码成功");
			return "secure/pswmgr/changeinfo";
		}
		request.setAttribute("code", 1);
		request.setAttribute("message", "修改支付密码失败");
		return "secure/pswmgr/changeinfo";
		
	}
	
	/**
	 * 
	 * @param userName
	 * @return
	 */
	public static String getVerifyContent(String userName,String captcha){
		StringBuffer msgContent = new StringBuffer("");//设置消息内容
		msgContent.append("尊敬的"+userName+"<br></br>");
		msgContent.append("<p>您好!您正在为新华支付设置支付密码，为了确认是您本人在进行此项操作，请复制此验证码"+captcha+"到网页进行验证，并进入下一步操作！<p><br></br>");
		msgContent.append("<p>该验证码15分钟内有效，过期请得新发送！<p><br></br>");
		msgContent.append("<p>新华网管理中心！<p><br></br>");
		msgContent.append("<p>"+Function.getDateTimeString()+"<p><br></br>");
		return msgContent.toString();
	}
}
