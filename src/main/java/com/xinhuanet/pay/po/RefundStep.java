package com.xinhuanet.pay.po;

import java.io.Serializable;
import java.util.Date;

/**
 * @author duanwc
 * @date 2014-3-2
 *
 */
public class RefundStep implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 自增主键
	 */
	private Integer id;
	/**
	 * 当前退款的原订单ID 主键，不支持分单退款，即订单金额全部退款
	 */
	private String orderId;
	/**
	 * 原因/理由
	 */
	private String reason;
	/**
	 * 步骤,0-用户,1-应用管理员,2-财务
	 */
	private int step;
	/**
	 * 流程状态，1-退款申请,2-拒绝退款,3-同意退款,4-处理完成
	 */
	private int status;
	/**
	 * 处理时间
	 */
	private Date handleTime;
	/**
	 * 用户申请IP地址
	 */
	private String ipAddress;
	/**
	 * 管理员
	 */
	private String admin;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
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
	public Date getHandleTime() {
		return handleTime;
	}
	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	@Override
	public String toString() {
		return "RefundStep [id=" + id + ", orderId=" + orderId + ", reason="
				+ reason + ", step=" + step + ", status=" + status
				+ ", handleTime=" + handleTime + ", ipAddress=" + ipAddress
				+ ", admin=" + admin + "]";
	}
	
}
