package com.xinhuanet.pay.exception;
/**
 * 必选属性值未设置异常
 * @author duanwc
 *
 */
public class AttributeUnsetException extends Exception {

	private static final long serialVersionUID = 8037596917465814042L;

	public AttributeUnsetException(String msg) {
		super(msg);
	}
	
	public AttributeUnsetException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
