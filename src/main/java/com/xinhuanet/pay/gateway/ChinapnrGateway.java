package com.xinhuanet.pay.gateway;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("chinapnrGateway")
public class ChinapnrGateway extends BaseGateway<ChinapnrGateway> {
	@Autowired
	protected PropertiesConfiguration props;
	
	/**
	 * 中国工商银行-25
	 */
	public static final String ICBC_GATEWAY = "25";
	/**
	 * 招商银行-28
	 */
	public static final String CMB_GATEWAY = "28";
	/**
	 * 兴业银行-09
	 */
	public static final String CIB_GATEWAY = "09";
	/**
	 * 中国建设银行-27
	 */
	public static final String CCB_GATEWAY = "27";
	/**
	 * 中国民生银行-12
	 */
	public static final String CMBC_GATEWAY = "12";
	/**
	 * 华夏银行-13
	 */
	public static final String HXB_GATEWAY = "13";
	/**
	 * 广发银行-19
	 */
	public static final String GDB_GATEWAY = "19";
	/**
	 * 北京银行-15
	 */
	public static final String BOB_GATEWAY = "15";
	/**
	 * 浦东发展银行-16
	 */
	public static final String SPDB_GATEWAY = "16";
	/**
	 * 交通银行-21
	 */
	public static final String BCM_GATEWAY = "21";
	/**
	 * 中国农业银行-29
	 */
	public static final String ABC_GATEWAY = "29";
	/**
	 * 中信银行-33
	 */
	public static final String CITIC_GATEWAY = "33";
	/**
	 * 中国光大银行-36
	 */
	public static final String CEB_GATEWAY = "36";
	/**
	 * 北京农村商业银行-40
	 */
	public static final String BJRCB_GATEWAY = "40";
	/**
	 * 中国银行-45
	 */
	public static final String BOC_GATEWAY = "45";
	/**
	 * 邮政储蓄-46
	 */
	public static final String PSBC_GATEWAY = "46";
	/**
	 * 南京银行-49
	 */
	public static final String NJCB_GATEWAY = "49";
	/**
	 * 平安银行-50
	 */
	public static final String PINGAN_GATEWAY = "50";
	/**
	 * 杭州银行-51
	 */
	public static final String HZB_GATEWAY = "51";
	/**
	 * 浙商银行-53
	 */
	public static final String CZB_GATEWAY = "53";
	/**
	 * 上海银行-54
	 */
	public static final String BANKOFSHANGHAI_GATEWAY = "54";
	/**
	 * 渤海银行-55
	 */
	public static final String CBHB_GATEWAY = "55";
	
	/**
	 * 存储第三方网关支持的银行信息
	 */
	private static Map<String,Object> map;
	
	public Map<String,Object> getConfigMap(){
		if(map != null){
			return map;
		}
		
		map = new HashMap<String,Object>();
		map.put(BankCard.ICBC, getBankValue(ICBC_GATEWAY,BankCard.ICBC_NAME));//中国工商银行
		map.put(BankCard.CMB, getBankValue(CMB_GATEWAY,BankCard.CMB_NAME));//招商银行
		map.put(BankCard.CIB, getBankValue(CIB_GATEWAY,BankCard.CIB_NAME));//兴业银行
		map.put(BankCard.CCB, getBankValue(CCB_GATEWAY,BankCard.CCB_NAME));//中国建设银行
		map.put(BankCard.CMBC, getBankValue(CMBC_GATEWAY,BankCard.CMBC_NAME));//中国民生银行
		map.put(BankCard.HXB, getBankValue(HXB_GATEWAY,BankCard.HXB_NAME));//华夏银行
		map.put(BankCard.GDB, getBankValue(GDB_GATEWAY,BankCard.GDB_NAME));//广发银行
		map.put(BankCard.BOB, getBankValue(BOB_GATEWAY,BankCard.BOB_NAME));//北京银行
		map.put(BankCard.SPDB, getBankValue(SPDB_GATEWAY,BankCard.SPDB_NAME));//浦东发展银行
		map.put(BankCard.BCM, getBankValue(BCM_GATEWAY,BankCard.BCM_NAME));//交通银行
		map.put(BankCard.ABC, getBankValue(ABC_GATEWAY,BankCard.ABC_NAME));//中国农业银行
		map.put(BankCard.CITIC, getBankValue(CITIC_GATEWAY,BankCard.CITIC_NAME));//中信银行
		map.put(BankCard.CEB, getBankValue(CEB_GATEWAY,BankCard.CEB_NAME));//中国光大银行
		map.put(BankCard.BJRCB, getBankValue(BJRCB_GATEWAY,BankCard.BJRCB_NAME));//北京农村商业银行
		map.put(BankCard.BOC, getBankValue(BOC_GATEWAY,BankCard.BOC_NAME));//中国银行
		map.put(BankCard.PSBC, getBankValue(PSBC_GATEWAY,BankCard.PSBC_NAME));//邮政储蓄
		map.put(BankCard.NJCB, getBankValue(NJCB_GATEWAY,BankCard.NJCB_NAME));//南京银行
		map.put(BankCard.PINGAN, getBankValue(PINGAN_GATEWAY,BankCard.PINGAN_NAME));//平安银行
		map.put(BankCard.HZB, getBankValue(HZB_GATEWAY,BankCard.HZB_NAME));//杭州银行
		map.put(BankCard.CZB, getBankValue(CZB_GATEWAY,BankCard.CZB_NAME));//浙商银行
		map.put(BankCard.BANKOFSHANGHAI, getBankValue(BANKOFSHANGHAI_GATEWAY,BankCard.BANKOFSHANGHAI_NAME));//上海银行
		map.put(BankCard.CBHB, getBankValue(CBHB_GATEWAY,BankCard.CBHB_NAME));//渤海银行
		return Collections.unmodifiableMap(map);
	}
	
	private ChinapnrGateway(){}
	
	private static ChinapnrGateway gateway = null;
	
	protected @Autowired @Resource(name="chinapnrGateway") void init(final ChinapnrGateway gateway){
		ChinapnrGateway.gateway = gateway;
		/**
		 * 注册网关对象
		 */
		this.attach(ThirdPartyGateway.chinapnr, ChinapnrGateway.gateway);
	}
	/**
	 * 获取Spring注册的对象
	 * @return ChinapnrGateway
	 */
	public static ChinapnrGateway getInstance(){
		return gateway;
	}
}
