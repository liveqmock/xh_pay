package com.xinhuanet.pay.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.news.xhsso.bean.SSOUser;
import cn.news.xhsso.client.appactor.ActorException;
import cn.news.xhsso.sessionutils.XSession;
import cn.news.xhsso.sessionutils.XSessionUtils;

import com.xinhuanet.pay.exception.AccountErrorException;
import com.xinhuanet.pay.po.UserInfo;
import com.xinhuanet.pay.service.AccountService;

public class SSOLoginActor implements cn.news.xhsso.client.appactor.ICoAppActor {
	protected final Logger logger = Logger.getLogger(getClass());
	/**
	 * session保存用户信息的唯一标识
	 */
	private static final String SESSIONKEY = LoginConf.SESSIONKEY;
	/**
	* 应用中Session的登录标记.
	*/
	private static final String LOGIN_FLAG = LoginConf.LOGIN_FLAG;
	/**
	 * 账户控制service
	 */
	private AccountService accountService;

	@Override
	public boolean checkLocalLogin(HttpServletRequest request, HttpServletResponse response) {
		// 需验证用户是否登录
		try {
			XSession session=XSessionUtils.getSession(request, response);
			return session.getAttribute(LOGIN_FLAG, false)!=null;
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return false;
		}
//		return true;
	}

	@Override
	public void loadLoginUser(HttpServletRequest request, HttpServletResponse response, SSOUser user) {
		XSession session=XSessionUtils.getSession(request, response);
		UserInfo userinfo = new UserInfo();
		userinfo.setUserId(user.getUserid());
		userinfo.setLoginName(user.getUsername());
		userinfo.setNickName(user.getNickname());
		userinfo.setEmail(user.getEmail());
		session.setAttribute(LOGIN_FLAG, user.getUsername());
		session.setAttribute(SESSIONKEY, userinfo);
		//获取spring管理的对象
		WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(request.getSession().getServletContext());
		accountService = (AccountService)wac.getBean("accountService");
		//对用户进行过滤，如不存在用户，则新增用户
		try {
			accountService.filterAccount(user.getUserid(), user.getUsername());
			logger.info("用户:"+user.getUsername()+",ID:"+user.getUserid()+" 登录新华支付平台");
		} catch (AccountErrorException e) {
			logger.debug("用户初始化异常",e);
			e.printStackTrace();
		}
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response) throws ActorException {
		try {
			XSession session=XSessionUtils.getSession(request, response);
			session.removeAttribute(LOGIN_FLAG);
			session.removeAttribute(SESSIONKEY);
		} catch (IllegalStateException e) {
			// this can be ignored
		}
	}



	@Override
	public boolean addUser(HttpServletRequest arg0, HttpServletResponse arg1,
			SSOUser arg2) throws cn.news.xhsso.client.appactor.ActorException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean disableUser(HttpServletRequest arg0,
			HttpServletResponse arg1, SSOUser arg2)
			throws cn.news.xhsso.client.appactor.ActorException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean enableUser(HttpServletRequest arg0,
			HttpServletResponse arg1, SSOUser arg2)
			throws cn.news.xhsso.client.appactor.ActorException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void executeAfterLogin(HttpServletRequest arg0,
			HttpServletResponse arg1, StringBuffer arg2)
			throws cn.news.xhsso.client.appactor.ActorException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean executeBeforeLogin(HttpServletRequest arg0,
			HttpServletResponse arg1, StringBuffer arg2)
			throws cn.news.xhsso.client.appactor.ActorException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String extractUserName(HttpServletRequest request,
			HttpServletResponse arg1)
			throws cn.news.xhsso.client.appactor.ActorException {
		String result = request.getParameter("username");
//		System.out.println("username:"+result);
		return (result == null) ? "" : result;
	}

	@Override
	public String extractUserPwd(HttpServletRequest request,
			HttpServletResponse arg1)
			throws cn.news.xhsso.client.appactor.ActorException {
		String result = request.getParameter("password");
//		System.out.println("username:"+result);
		return (result == null) ? "" : result;
	}

	@Override
	public String getSessionId(HttpServletRequest request,
			HttpServletResponse response, boolean arg2)
			throws cn.news.xhsso.client.appactor.ActorException {
		// TODO Auto-generated method stub
//		return request.getSession().getId();
		XSession session=XSessionUtils.getSession(request, response);
		return session.getId();
	}

	@Override
	public void loadAnonymous(HttpServletRequest arg0, HttpServletResponse arg1)
			throws cn.news.xhsso.client.appactor.ActorException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean removeUser(HttpServletRequest arg0,
			HttpServletResponse arg1, SSOUser arg2)
			throws cn.news.xhsso.client.appactor.ActorException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean unremoveUser(HttpServletRequest arg0,
			HttpServletResponse arg1, SSOUser arg2)
			throws cn.news.xhsso.client.appactor.ActorException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean updateUser(HttpServletRequest arg0,
			HttpServletResponse arg1, SSOUser arg2)
			throws cn.news.xhsso.client.appactor.ActorException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean useStandardHttpSession() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean userExist(HttpServletRequest arg0, HttpServletResponse arg1,
			SSOUser arg2) throws cn.news.xhsso.client.appactor.ActorException {
		// TODO Auto-generated method stub
		return true;
	}
	
    @Override
    public boolean isUserEnabled(HttpServletRequest request, HttpServletResponse response, SSOUser ssoUser, StringBuffer stringBuffer) throws ActorException {

        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
