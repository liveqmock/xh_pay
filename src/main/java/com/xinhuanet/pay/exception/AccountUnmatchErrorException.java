package com.xinhuanet.pay.exception;

/**
 * 用户不匹配异常
 * @author duanwc
 *
 */
public class AccountUnmatchErrorException extends AccountErrorException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccountUnmatchErrorException(String msg) {
		super(msg);
	}
	
	public AccountUnmatchErrorException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
