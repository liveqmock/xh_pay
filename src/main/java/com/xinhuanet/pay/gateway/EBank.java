package com.xinhuanet.pay.gateway;

public class EBank {
	/**
	 * 根据用户当前订单，是否需要操作本地数据库。如果交易成功，
	 * 本地数据库订单状态失败和初始的时候，可以进行操作；如果
	 * 交易失败，数据库状态为初始的时候进行操作;
	 * @param orderStatus 订单状态
	 * @param bStatus 是否成功
	 * @return 如果需要操作返回true，否则返回false
	 */
	public static boolean isOperate(int orderStatus,boolean bStatus){
		boolean operate = false;
		if (bStatus) {// 如果交易成功
			if (orderStatus == 0 || orderStatus == 2) {// 如果本地数据库状态为初始状态,如果本地数据库状态为失败
				operate = true;
			}
		} else {// 如果交易失败
			if (orderStatus == 0) {
				operate = true;
			}
		}
		return operate;
	}
}
