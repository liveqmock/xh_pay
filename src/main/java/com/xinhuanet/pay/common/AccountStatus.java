package com.xinhuanet.pay.common;

public class AccountStatus {
	//---------------客户类型 start------------------
	/**
	 * 客户类型-普通客户
	 */
	public static final int ACCOUNT_TYPE_CUSTOMERS = 0;
	/**
	 * 客户类型-大客户
	 */
	public static final int ACCOUNT_TYPE_BIGCUSTOMERS = 1;
	
	/**
	 * 客户类型汉字名称-普通客户
	 */
	public static final String ACCOUNT_TYPE_CUSTOMERS_NAME = "普通客户";
	/**
	 * 客户类型汉字名称-大客户
	 */
	public static final String ACCOUNT_TYPE_BIGCUSTOMERS_NAME = "大客户";
	//---------------------end-------------------------------------
	
	
	//---------------客户状态 start-----------------
	/**
	 * 客户状态-受限制
	 */
	public static final int ACCOUNT_STATUS_RESTRICT = 0;
	/**
	 * 客户状态-不受限制
	 */
	public static final int ACCOUNT_STATUS_UNRESTRAINED = 1;
	/**
	 * 客户状态汉字名称-受限制
	 */
	public static final String ACCOUNT_STATUS_RESTRICT_NAME = "受限制用户";
	/**
	 * 客户状态汉字名称-不受限制
	 */
	public static final String ACCOUNT_STATUS_UNRESTRAINED_NAME = "特权用户";
	//---------------------end--------------------------------------
	
	//---------------客户等级 start-------------------
	/**
	 * 客户等级-一级
	 */
	public static final int ACCOUNT_LEVEL_ONE = 1;
	/**
	 * 客户等级-二级
	 */
	public static final int ACCOUNT_LEVEL_TWO = 2;
	/**
	 * 客户等级汉字名称-一级
	 */
	public static final String ACCOUNT_LEVEL_ONE_NAME = "一级";
	/**
	 * 客户等级汉字名称-二级
	 */
	public static final String ACCOUNT_LEVEL_TWO_NAME = "二级";
	//---------------end--------------------------------
	
	/**
	 * 客户等级
	 */
	private int level;
	/**
	 * 客户等级汉字名称
	 */
	private String levelName;
	/**
	 * 客户类型
	 */
	private int type;
	/**
	 * 客户类型汉字名称
	 */
	private String typeName;
	/**
	 * 客户状态
	 */
	private int status;
	/**
	 * 客户状态名称
	 */
	private String statusName;
	/**
	 * 返回的信息
	 */
	private String message;


	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getStatusName() {
		switch (status) {
		case ACCOUNT_STATUS_RESTRICT:
			statusName = ACCOUNT_STATUS_RESTRICT_NAME;
			break;
		case ACCOUNT_STATUS_UNRESTRAINED:
			statusName = ACCOUNT_STATUS_UNRESTRAINED_NAME;
			break;
		}
		return statusName;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}

	public String getLevelName() {
		switch (level) {
		case ACCOUNT_LEVEL_ONE:
			levelName = ACCOUNT_LEVEL_ONE_NAME;
			break;
		case ACCOUNT_LEVEL_TWO:
			levelName = ACCOUNT_LEVEL_TWO_NAME;
			break;
		}
		return levelName;
	}

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTypeName() {
		switch (type) {
		case ACCOUNT_TYPE_CUSTOMERS:
			typeName = ACCOUNT_TYPE_CUSTOMERS_NAME;
			break;
		case ACCOUNT_TYPE_BIGCUSTOMERS:
			typeName = ACCOUNT_TYPE_BIGCUSTOMERS_NAME;
			break;
		}
		return typeName;
	}
	
}
