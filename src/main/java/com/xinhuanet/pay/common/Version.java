package com.xinhuanet.pay.common;

import java.io.Serializable;

/**
 * 支付系统版本
 * @author duanwc
 *
 */
public abstract class Version implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 当前版本
	 */
	public static final String CURRENT_VERSION = "10";
	
	/**
	 * 历史版本
	 */
	public static final String VERSION_10 = "10";
}
