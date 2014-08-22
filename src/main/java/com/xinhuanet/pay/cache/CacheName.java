package com.xinhuanet.pay.cache;

public class CacheName {
	/**
	 * 配置缓存列表的最大条数
	 */
	public static final int CACHE_TOTALCOUNT=600;
	
	/**
	 * 为了防止缓存污染，针对每个不同的应用，重新定义这里的缓存名称前缀的值
	 */
	protected static final String CACHEPREFIX="xhpay_";
	
	//****为了防止缓存污染，针对每个不同的应用，重新定义这里的缓存名称前缀的值****//
	/**
	 * 开通支付密码时用的验证码
	 */
	public final static String PAY_PSW_CODE = CACHEPREFIX + "psw_code_"; 

	/**
	 * 开通邮箱时用的验证码
	 */
    public final static String PAY_EMAIL_CODE = CACHEPREFIX + "email_code_";
    
	/**
	 * 应用订单AppOrder对象,key值为 当前前缀 + uuid
	 */
    public final static String APPORDER = CACHEPREFIX + "appOrder_";
	/**
	 * 网银、第三方支付订单Order对象,key值为 当前前缀 + 订单号
	 */
    public final static String ORDER = CACHEPREFIX + "order_";
    /**
     * 系统设置的属性,key值为 当前前缀 + 属性key
     */
    public final static String SYSCONFIG = CACHEPREFIX + "sys_";

}
