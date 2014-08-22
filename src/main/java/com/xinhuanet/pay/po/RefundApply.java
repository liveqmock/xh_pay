package com.xinhuanet.pay.po;

import java.io.Serializable;
import java.util.Date;

/**
 * @author duanwc
 * @date 2014-3-2
 *
 */
public class RefundApply implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 当前退款的原订单ID 主键，不支持分单退款，即订单金额全部退款
	 */
	private String orderId;
	/**
	 * 用户ID
	 */
	private String uid;
	/**
	 * 用户登录名称
	 */
	private String loginName;

	/**
	 * 交易号，来源于第三方交易平台
	 */
	private String trxId;
	/**
	 * 退款单完成后回来更新的订单ID
	 */
	private String refOrdId;
	/**
	 * 应用id
	 */
	private int appId;
	/**
	 * 充值金额
	 */
	private double money;
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
	private int status;
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
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
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
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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
	@Override
	public String toString() {
		return "RefundApply [orderId=" + orderId + ", uid=" + uid
				+ ", loginName=" + loginName + ", trxId=" + trxId
				+ ", refOrdId=" + refOrdId + ", appId=" + appId + ", money="
				+ money + ", payType=" + payType + ", reason=" + reason
				+ ", step=" + step + ", status=" + status + ", apply=" + apply
				+ ", handleTime=" + handleTime + ", comment=" + comment + "]";
	}

}
