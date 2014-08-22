/**
 * 
 */
package com.xinhuanet.pay.gateway;

/**
 * @author duanwc 定义银行卡标识
 */
public class BankCard {
	/**
	 * 中国工商银行
	 */
	public static final String ICBC = "icbc";
	public static final String ICBC_NAME = "中国工商银行";
	/**
	 * 招商银行
	 */
	public static final String CMB = "cmb";
	public static final String CMB_NAME = "招商银行";
	/**
	 * 兴业银行
	 */
	public static final String CIB = "cib";
	public static final String CIB_NAME = "兴业银行";
	/**
	 * 中国建设银行
	 */
	public static final String CCB = "ccb";
	public static final String CCB_NAME = "中国建设银行";
	/**
	 * 中国民生银行
	 */
	public static final String CMBC = "cmbc";
	public static final String CMBC_NAME = "中国民生银行";
	/**
	 * 华夏银行
	 */
	public static final String HXB = "hxb";
	public static final String HXB_NAME = "华夏银行";
	/**
	 * 广发银行
	 */
	public static final String GDB = "gdb";
	public static final String GDB_NAME = "广发银行";

	/**
	 * 北京银行
	 */
	public static final String BOB = "bob";
	public static final String BOB_NAME = "北京银行";
	/**
	 * 浦东发展银行
	 */
	public static final String SPDB = "spdb";
	public static final String SPDB_NAME = "浦东发展银行";
	/**
	 * 交通银行
	 */
	public static final String BCM = "bcm";
	public static final String BCM_NAME = "交通银行";
	/**
	 * 中国农业银行
	 */
	public static final String ABC = "abc";
	public static final String ABC_NAME = "中国农业银行";
	/**
	 * 中信银行
	 */
	public static final String CITIC = "citic";
	public static final String CITIC_NAME = "中信银行";
	/**
	 * 中国光大银行
	 */
	public static final String CEB = "ceb";
	public static final String CEB_NAME = "中国光大银行";
	/**
	 * 北京农村商业银行
	 */
	public static final String BJRCB = "bjrcb";
	public static final String BJRCB_NAME = "北京农村商业银行";
	/**
	 * 中国银行
	 */
	public static final String BOC = "boc";
	public static final String BOC_NAME = "中国银行";
	/**
	 * 邮政储蓄
	 */
	public static final String PSBC = "psbc";
	public static final String PSBC_NAME = "邮政储蓄";
	/**
	 * 南京银行
	 */
	public static final String NJCB = "njcb";
	public static final String NJCB_NAME = "南京银行";
	/**
	 * 平安银行
	 */
	public static final String PINGAN = "pingan";
	public static final String PINGAN_NAME = "平安银行";
	/**
	 * 杭州银行
	 */
	public static final String HZB = "hzb";
	public static final String HZB_NAME = "杭州银行";
	/**
	 * 浙商银行
	 */
	public static final String CZB = "czb";
	public static final String CZB_NAME = "浙商银行";
	/**
	 * 上海银行
	 */
	public static final String BANKOFSHANGHAI = "bankofshanghai";
	public static final String BANKOFSHANGHAI_NAME = "上海银行";
	/**
	 * 渤海银行
	 */
	public static final String CBHB = "cbhb";
	public static final String CBHB_NAME = "渤海银行";
	/**
	 * 虚拟银行
	 */
	public static final String VBANK = "vbank";
	public static final String VBANK_NAME = "虚拟银行";

	/**
	 * 根据银行银行网关，获取银行名称
	 * 
	 * @param gateway
	 *            新华支付平台定义的网关号
	 * @return 如果匹配，返回对应的银行名称，否则，返回null
	 */
	public String getCardName(String gateway) {
		if (ICBC.equals(gateway)) {
			return ICBC_NAME;// 工商银行
		} else if (CMB.equals(gateway)) {
			return CMB_NAME;// 招商银行
		} else if (CIB.equals(gateway)) {
			return CIB_NAME;// 兴业银行
		} else if (CCB.equals(gateway)) {
			return CCB_NAME;// 建设银行
		} else if (CMBC.equals(gateway)) {
			return CMBC_NAME;// 中国民生银行
		} else if (HXB.equals(gateway)) {
			return HXB_NAME;// 华夏银行
		} else if (GDB.equals(gateway)) {
			return GDB_NAME;// 深圳发展银行
		} else if (BOB.equals(gateway)) {
			return BOB_NAME;// 北京银行
		} else if (SPDB.equals(gateway)) {
			return SPDB_NAME;// 浦东发展银行
		} else if (BCM.equals(gateway)) {
			return BCM_NAME;// 交通银行
		} else if (ABC.equals(gateway)) {
			return ABC_NAME;// 农业银行
		} else if (CITIC.equals(gateway)) {
			return CITIC_NAME;// 中信银行
		} else if (CEB.equals(gateway)) {
			return CEB_NAME;// 中国光大银行
		} else if (BJRCB.equals(gateway)) {
			return BJRCB_NAME;// 北京农村商业银行
		} else if (BOC.equals(gateway)) {
			return BOC_NAME;// 中国银行
		} else if (PSBC.equals(gateway)) {
			return PSBC_NAME;// 东亚银行
		} else if (NJCB.equals(gateway)) {
			return NJCB_NAME;// 南京银行
		} else if (PINGAN.equals(gateway)) {
			return PINGAN_NAME;// 平安银行
		} else if (HZB.equals(gateway)) {
			return HZB_NAME;// 杭州银行
		} else if (CZB.equals(gateway)) {
			return CZB_NAME;// 浙商银行
		} else if (BANKOFSHANGHAI.equals(gateway)) {
			return BANKOFSHANGHAI_NAME;// 上海银行
		} else if (CBHB.equals(gateway)) {
			return CBHB_NAME;// 渤海银行
		} else if (VBANK.equals(gateway)) {
			return VBANK_NAME;// 虚拟银行
		} else {
			return null;
		}
	}
}
