package com.xinhuanet.pay.po;

import java.util.Date;

import com.xinhuanet.pay.common.AccountStatus;

public class Account {
	/**
	 * 用户id
	 */
	private String uid;
	/**
	 * 用户登录名称
	 */
	private String loginName;
	/**
	 * 用户昵称
	 */
	private String userName;
	/**
	 * 目前金额
	 */
	private double money;
	/**
	 * 保证金
	 */
	private double deposit;
	/**
	 * 电子邮件
	 */
	private String email;
	/**
	 * 电子邮件认证状态，未认证-0，已认证-1
	 */
	private int emailStatus;
	/**
	 * 手机
	 */
	private String mobile;
	/**
	 * 手机认证状态，未认证-0，已认证-1
	 */
	private int mobileStatus;
	/**
	 * 支付密码，密文
	 */
	private String payCode;
	/**
	 * 支付密码状态，未启用-0，已启用-1
	 */
	private int payCodeStatus;
	/**
	 * 用户等级
	 */
	private int level;
	/**
	 * 用户等级名称
	 */
	private String levelName;
	/**
	 * 状态(限制-0，特权-1)
	 */
	private int status;
	/**
	 * 账户类型(普通客户-0，大客户-1)
	 */
	private int type;
	/**
	 * 账户类型名称
	 */
	private String typeName;
	/**
	 * 创建时间
	 */
	private Date createtime;
	/**
	 * 最后更新时间
	 */
	private Date updatetime;
	/**
	 * 备注
	 */
	private String comment;
	/**
	 * 最后一次订单号
	 */
	private String lastOrderId;
	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}
	public int getLevel() {
		return level;
	}

	public String getLastOrderId() {
		return lastOrderId;
	}

	public void setLastOrderId(String lastOrderId) {
		this.lastOrderId = lastOrderId;
	}

	public void setLevel(int level) {
		AccountStatus status = new AccountStatus();
		status.setLevel(level);
		this.setLevelName(status.getLevelName());
		this.level = level;
	}

	public int getStatus() {
		return status;
	}
	
	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		AccountStatus status = new AccountStatus();
		status.setType(type);
		this.setTypeName(status.getTypeName());
		this.type = type;
	}
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public double getDeposit() {
		return deposit;
	}

	public void setDeposit(double deposit) {
		this.deposit = deposit;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(int emailStatus) {
		this.emailStatus = emailStatus;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public int getMobileStatus() {
		return mobileStatus;
	}

	public void setMobileStatus(int mobileStatus) {
		this.mobileStatus = mobileStatus;
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public int getPayCodeStatus() {
		return payCodeStatus;
	}

	public void setPayCodeStatus(int payCodeStatus) {
		this.payCodeStatus = payCodeStatus;
	}

	@Override
	public String toString() {
		return "Account [uid=" + uid + ", loginName=" + loginName
				+ ", userName=" + userName + ", money=" + money + ", deposit="
				+ deposit + ", email=" + email + ", emailStatus=" + emailStatus
				+ ", mobile=" + mobile + ", mobileStatus=" + mobileStatus
				+ ", payCode=" + payCode + ", payCodeStatus=" + payCodeStatus
				+ ", level=" + level + ", levelName=" + levelName + ", status="
				+ status + ", type=" + type + ", typeName=" + typeName
				+ ", createtime=" + createtime + ", updatetime=" + updatetime
				+ ", comment=" + comment + ", lastOrderId=" + lastOrderId + "]";
	}

}
