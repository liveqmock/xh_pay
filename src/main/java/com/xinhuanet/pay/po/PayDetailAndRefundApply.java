package com.xinhuanet.pay.po;

import java.io.Serializable;
import java.util.Date;

public class PayDetailAndRefundApply implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 系统ID
	 */
	private String id;
	/**
	 * 用户ID
	 */
	private String uid;
	/**
	 * 用户登录名称
	 */
	private String loginName;
	/**
	 * 商品ID
	 */
	private String pid;
	/**
	 * 商品名称
	 */
	private String pname;
	/**
	 * 类型，0-收入，1-支出
	 */
	private int type;
	/**
	 * 订单ID
	 */
	private String orderId;
	/**
	 * 金额
	 */
	private double money;
	/**
	 * 操作前金额
	 */
	private double beforeMoney;
	/**
	 * 操作后金额
	 */
	private double afterMoney;
	/**
	 * 交易类型(0-充值、1-即时交易、2-保证金)
	 */
	private int orderType;
	/**
	 * 状态（预留）
	 */
	private int status;
	/**
	 * 来自哪个应用
	 */
	private int appId;
	/**
	 * 来自哪个应用中文名称
	 */
	private String AppName;
	/**
	 * 定制时间
	 */
	private Date addTime;
	/**
	 * IP地址
	 */
	private String ipAddress;
	/**
	 * 备注信息
	 */
	private String ext;
	/**
	 * 币种
	 */
	private String curType;
	/**
	 * 币种名称
	 */
	private String curName;
	
	/**
	 * 交易号，来源于第三方交易平台
	 */
	private String trxId;
	/**
	 * 退款单完成后回来更新的订单ID
	 */
	private String refOrdId;
	/**
	 * 退款金额
	 */
	private double refundMoney;
	/**
	 * 第三方网关（支付宝-alipay、通联支付-allinpay、汇付天下-chinapnr）
	 */
	private String payType;
	/**
	 * 申请退款原因
	 */
	private String reason;
	/**
	 * 处理完成状态,0-用户处理完成，1-应用管理员处理完成，2-财务处理完成
	 */
	private int step;
	/**
	 * 流程状态，1-退款申请,2-拒绝退款,3-同意退款,4-处理完成
	 */
	private int refundStatus;
	/**
	 * 主动申请或被动申请(主动-0，被动-1)
	 */
	private Integer apply;
	/**
	 * 申请时间
	 */
	private Date handleTime;
	/**
	 * 备注
	 */
	private String comment;
	/**
	 * 是否可以退款 0：不可以，1：可以
	 */
	private String canRefund;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public double getBeforeMoney() {
		return beforeMoney;
	}
	public void setBeforeMoney(double beforeMoney) {
		this.beforeMoney = beforeMoney;
	}
	public double getAfterMoney() {
		return afterMoney;
	}
	public void setAfterMoney(double afterMoney) {
		this.afterMoney = afterMoney;
	}
	public int getOrderType() {
		return orderType;
	}
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public String getAppName() {
		return AppName;
	}
	public void setAppName(String appName) {
		AppName = appName;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public String getCurType() {
		return curType;
	}
	public void setCurType(String curType) {
		this.curType = curType;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	public String getTrxId() {
		return trxId;
	}
	public void setTrxId(String trxId) {
		this.trxId = trxId;
	}
	public String getRefOrdId() {
		return refOrdId;
	}
	public void setRefOrdId(String refOrdId) {
		this.refOrdId = refOrdId;
	}
	public double getRefundMoney() {
		return refundMoney;
	}
	public void setRefundMoney(double refundMoney) {
		this.refundMoney = refundMoney;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
	public int getRefundStatus() {
		return refundStatus;
	}
	public void setRefundStatus(int refundStatus) {
		this.refundStatus = refundStatus;
	}
	public Integer getApply() {
		return apply;
	}
	public void setApply(Integer apply) {
		this.apply = apply;
	}
	public Date getHandleTime() {
		return handleTime;
	}
	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCanRefund() {
		return canRefund;
	}
	public void setCanRefund(String canRefund) {
		this.canRefund = canRefund;
	}
	@Override
	public String toString() {
		return "PayDetailAndRefundApply [id=" + id + ", uid=" + uid
				+ ", loginName=" + loginName + ", pid=" + pid + ", pname="
				+ pname + ", type=" + type + ", orderId=" + orderId
				+ ", money=" + money + ", beforeMoney=" + beforeMoney
				+ ", afterMoney=" + afterMoney + ", orderType=" + orderType
				+ ", status=" + status + ", appId=" + appId + ", AppName="
				+ AppName + ", addTime=" + addTime + ", ipAddress=" + ipAddress
				+ ", ext=" + ext + ", curType=" + curType + ", curName="
				+ curName + ", trxId=" + trxId + ", refOrdId=" + refOrdId
				+ ", refundMoney=" + refundMoney + ", payType=" + payType
				+ ", reason=" + reason + ", step=" + step + ", refundStatus="
				+ refundStatus + ", apply=" + apply + ", handleTime="
				+ handleTime + ", comment=" + comment + ", canRefund="
				+ canRefund + "]";
	}
}
