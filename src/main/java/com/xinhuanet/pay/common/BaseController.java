package com.xinhuanet.pay.common;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

import cn.news.xhsso.sessionutils.XSession;
import cn.news.xhsso.sessionutils.XSessionUtils;

import com.xinhuanet.pay.exception.LoginUserNotFoundException;
import com.xinhuanet.pay.po.Account;
import com.xinhuanet.pay.po.UserInfo;
import com.xinhuanet.pay.service.AccountService;
import com.xinhuanet.pay.util.LoginConf;

@Controller
public class BaseController extends Base{

	/**
	 * 获取userId
	 * @param request request请求
	 * @return 用户ID
	 * @throws LoginUserNotFoundException
	 */
	public String getUserId(HttpServletRequest request,HttpServletResponse response) throws LoginUserNotFoundException{
		UserInfo userinfo = this.getUserInfo(request, response);
		Object uid =  userinfo.getUserId();
		if(uid == null){
			throw new LoginUserNotFoundException("Login user not found.");
		} else{
			return (String) uid;
		}
	}
	/**
	 * 获取loginName
	 * @param request request请求
	 * @return 用户登录名称
	 * @throws LoginUserNotFoundException
	 */
	public String getLoginName(HttpServletRequest request,HttpServletResponse response) throws LoginUserNotFoundException{
		UserInfo userinfo = this.getUserInfo(request, response);
		Object loginName = userinfo.getLoginName();
		if(loginName == null){
			throw new LoginUserNotFoundException("Login user not found.");
		} else{
			return (String) loginName;
		}
	}
	/**
	 * 获取userName
	 * @param request request请求
	 * @return 用户昵称
	 * @throws LoginUserNotFoundException
	 */
	public String getUserName(HttpServletRequest request,HttpServletResponse response) throws LoginUserNotFoundException{
		UserInfo userinfo = this.getUserInfo(request, response);
		Object uname = userinfo.getNickName();
		if(uname == null){
			throw new LoginUserNotFoundException("Login user not found.");
		} else{
			return (String) uname;
		}
	}

	/**
	 * 出现IO异常返回503状态码
	 * @param request
	 * @param response
	 */
	@ExceptionHandler(IOException.class)
	public void handleIOExceptions(HttpServletRequest request,
			HttpServletResponse response) {
		logger.error("HTTP 503 SERVICE UNAVAILABLE :" + request.getRequestURL());

		response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
	}
	
	@ExceptionHandler(LoginUserNotFoundException.class)
	public void handelNoUserLoginException(HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		response.sendRedirect(getProperty("xh.payment.ids.loginurl", "http://login.home.news.cn/"));
	}
	
	/**
	 * 账户服务
	 */
	private @Autowired AccountService accountService;
	/**
	 * 封装账户所有属性
	 * @param request
	 */
	public Account getAccount(HttpServletRequest request,HttpServletResponse response){
		return this.getAccount(request,response,true);
	}
	/**
	 * 封装账户基本属性，不动态获取用户数据
	 * @param request 
	 * @param b_database 是否查询数据库，默认为true
	 */
	public Account getAccount(HttpServletRequest request,HttpServletResponse response,boolean b_database){
		Account account = new Account();
		//获取账户属性
		try{
			//如果为true，则动态获取用户属性
			if(b_database){
				account = accountService.getAccount(this.getUserId(request,response));
			}
			account.setUserName(this.getUserName(request,response));
			account.setUid(this.getUserId(request,response));
			account.setLoginName(this.getLoginName(request,response));
			request.setAttribute("account", account);
		} catch(Exception e){
			e.printStackTrace();
		}
		return account;
	}
	
	private UserInfo getUserInfo(HttpServletRequest request,HttpServletResponse response){
		XSession session=XSessionUtils.getSession(request, response);
		UserInfo userinfo = ((UserInfo)session.getAttribute(LoginConf.SESSIONKEY));
		
		if(userinfo==null){
			logger.info("用户未登录，无法获取用户信息，请登录系统");
			try {
				response.sendRedirect(request.getContextPath()+"/index.html");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return userinfo;
	}
	
}