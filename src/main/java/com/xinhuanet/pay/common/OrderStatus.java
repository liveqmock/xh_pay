package com.xinhuanet.pay.common;

public class OrderStatus {
	//---------------订单状态 start------------------
	/**
	 * 订单状态，未充值
	 */
	public static final int ORDER_STATUS_INIT = 0;
	/**
	 * 订单状态，成功
	 */
	public static final int ORDER_STATUS_SUCCEES = 1;
	/**
	 * 订单状态，失败
	 */
	public static final int ORDER_STATUS_FAILED = 2;
	/**
	 * 订单状态，过期
	 */
	public static final int ORDER_STATUS_INVALID = 3;
	/**
	 * 订单状态汉字名称，未充值
	 */
	public static final String ORDER_STATUS_INIT_NAME = "未充值";
	/**
	 * 订单状态汉字名称，成功
	 */
	public static final String ORDER_STATUS_SUCCEES_NAME = "成功";
	/**
	 * 订单状态汉字名称，失败
	 */
	public static final String ORDER_STATUS_FAILED_NAME = "失败";
	/**
	 * 订单状态汉字名称，过期
	 */
	public static final String ORDER_STATUS_INVALID_NAME = "过期";
	//------------------end--------------------------

	
	/**
	 * 订单状态
	 */
	private int status;
	/**
	 * 订单状态名称
	 */
	private String statusName;
	/**
	 * 充值类型
	 */
	private String type;
	/**
	 * 返回的信息
	 */
	private String message;
	
	/**
	 * 订单是否正常
	 */
	private boolean normal = true;
	/**
	 * 订单是否正常汉字名称
	 */
	private String normalName;
	
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isNormal() {
		return normal;
	}

	public void setNormal(boolean normal) {
		this.normal = normal;
	}
	public String getNormalName() {
		if (normal) {
			normalName = "成功";
		} else {
			normalName = "失败";
		}
		return normalName;
	}
	
	public String getStatusName() {
		switch (status) {
		case ORDER_STATUS_INIT:
			statusName = ORDER_STATUS_INIT_NAME;
			break;
		case ORDER_STATUS_SUCCEES:
			statusName = ORDER_STATUS_SUCCEES_NAME;
			break;
		case ORDER_STATUS_FAILED:
			statusName = ORDER_STATUS_FAILED_NAME;
			break;
		case ORDER_STATUS_INVALID:
			statusName = ORDER_STATUS_INVALID_NAME;
			break;
		}
		return statusName;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
}
