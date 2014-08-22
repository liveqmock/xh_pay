package com.xinhuanet.pay.dao;

public interface PayPasswordDao {
	/**
	 * 根据用户名设置支付密码
	 * @param loginName
	 * @param pwd
	 * @return
	 */
	public int updatePayPsw(String loginName, String pwd);

	/**
	 * 根据用户ID设置支付密码
	 * @param userId
	 * @param pwd
	 * @return
	 */
	public int updatePayPsw(Long userId, String pwd);
}
