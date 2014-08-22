package com.xinhuanet.pay.po;

import java.io.Serializable;

/**
 * 用于保存SSO登录的用户信息
 * @author duanwc
 *
 */
public class UserInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String userId;
	private String loginName;				//登录名称
	private String nickName;				//用户昵称
	private String email;					//电子邮件
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return "UserInfo [email=" + email + ", loginName=" + loginName
				+ ", nickName=" + nickName + ", userId=" + userId + "]";
	}
}
