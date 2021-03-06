package com.xinhuanet.pay.service;

import com.xinhuanet.pay.exception.AccountErrorException;
import com.xinhuanet.pay.po.Account;

public interface AccountService {
	

	/**
	 * 查询当前用户帐户信息
	 * @param uid
	 * @return 帐户实例
	 */
	public Account getAccount(String uid);
	
	/**
	 * 更新用户保证金
	 * @param uid 用户ID
	 * @param money 充值金额
	 * @param lastOrderId 最后一次充值订单号
	 * @return
	 */
	public int addAccountDeposit(String uid,double money,String lastOrderId);
	/**
	 * 更新用户现金余额
	 * @param uid 用户ID
	 * @param money 充值金额
	 * @param lastOrderId 最后一次充值订单号
	 * @return
	 */
	public int addAccountCash(String uid,double money,String lastOrderId);
	/**
	 * 扣除账户金额
	 * @param uid 用户ID
	 * @param money 支付金额
	 * @return
	 */
	public int subAccountAmt(String uid,double money);
	/**
	 * 验证用户是否存在
	 * @param uid 用户ID
	 * @param loginName 用户登录名称
	 * @return 用户存在返回true；不存在返回false
	 */
	public boolean checkAccountExists(String uid,String loginName);
	/**
	 * 初始化一个用户
	 * @param uid 用户ID
	 * @param loginName 用户登录名称
	 * @return 初始化成功返回true，否则返回false
	 */
	public boolean initializeAccount(String uid,String loginName);
	
	/**
	 * 更新一个用户的邮箱认证和邮箱认证状态
	 * @param uid
	 * @param loginName
	 * @param emailID 用户邮箱号
	 * @return 更新记录条数，1表示成功，其它则表示失败
	 */
	public boolean updAccountEmail(String uid,String loginName,String emailID);
	
	/**
	 * 根据邮箱账号查看是否已被使用  0表示未使用  其他数字表示已已经被使用
	 * @param emailID
	 * @return
	 */
	public int queryAccountByEmail(String emailID);
	
	/**
	 * 对用户进行过滤，初始化等工作
	 * @param uid 用户ID
	 * @param loginName 用户登录名称
	 * @throws UserErrorException
	 */
	public void filterAccount(String uid,String loginName) throws AccountErrorException;
}
