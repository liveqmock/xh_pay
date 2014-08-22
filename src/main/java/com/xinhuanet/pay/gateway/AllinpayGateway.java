package com.xinhuanet.pay.gateway;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component("allinpayGateway")
public class AllinpayGateway extends BaseGateway<AllinpayGateway> {
	/**
	 * 中国工商银行-icbc
	 */
	public static final String ICBC_GATEWAY = "icbc";
	/**
	 * 招商银行-cmb
	 */
	public static final String CMB_GATEWAY = "cmb";
	/**
	 * 兴业银行-cib
	 */
	public static final String CIB_GATEWAY = "cib";
	/**
	 * 中国建设银行-ccb
	 */
	public static final String CCB_GATEWAY = "ccb";
	/**
	 * 中国民生银行-cmbc
	 */
	public static final String CMBC_GATEWAY = "cmbc";
	/**
	 * 华夏银行-hxb
	 */
	public static final String HXB_GATEWAY = "hxb";
	/**
	 * 广发银行-cgb
	 */
	public static final String GDB_GATEWAY = "cgb";
	/**
	 * 浦东发展银行-spdb
	 */
	public static final String SPDB_GATEWAY = "spdb";
	/**
	 * 交通银行-comm
	 */
	public static final String BCM_GATEWAY = "comm";
	/**
	 * 中国农业银行-abc
	 */
	public static final String ABC_GATEWAY = "abc";
	/**
	 * 中信银行-citic
	 */
	public static final String CITIC_GATEWAY = "citic";
	/**
	 * 中国光大银行-ceb
	 */
	public static final String CEB_GATEWAY = "ceb";
	/**
	 * 中国银行-boc
	 */
	public static final String BOC_GATEWAY = "boc";
	/**
	 * 邮政储蓄-psbc
	 */
	public static final String PSBC_GATEWAY = "psbc";
	/**
	 * 平安银行-pingan
	 */
	public static final String PINGAN_GATEWAY = "pingan";
	/**
	 * 上海银行-bos
	 */
	public static final String BANKOFSHANGHAI_GATEWAY = "bos";
	/**
	 * 虚拟银行-vbank
	 */
	public static final String VBANK_GATEWAY = "vbank";
	
	/**
	 * 存储第三方网关支持的银行信息
	 */
	private static Map<String,Object> map;
	
	public Map<String,Object> getConfigMap(){
		if(map != null){
			return map;
		}
		map = new LinkedHashMap<String,Object>();
		List<String> bankGateways = props.getList("Bank.Gateway");
		for(String bank : bankGateways){
			try {
				bank = new String(bank.getBytes("ISO-8859-1"),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			String[] gateway = bank.split(":");
			map.put(gateway[0].trim(), getBankValue(gateway[0],gateway[1]));
		}
		/**
		map.put(BankCard.ICBC, getBankValue(ICBC_GATEWAY,BankCard.ICBC_NAME));//中国工商银行
		map.put(BankCard.CMB, getBankValue(CMB_GATEWAY,BankCard.CMB_NAME));//招商银行
		map.put(BankCard.CIB, getBankValue(CIB_GATEWAY,BankCard.CIB_NAME));//兴业银行
		map.put(BankCard.CCB, getBankValue(CCB_GATEWAY,BankCard.CCB_NAME));//中国建设银行
		map.put(BankCard.CMBC, getBankValue(CMBC_GATEWAY,BankCard.CMBC_NAME));//中国民生银行
		map.put(BankCard.HXB, getBankValue(HXB_GATEWAY,BankCard.HXB_NAME));//华夏银行
		map.put(BankCard.GDB, getBankValue(GDB_GATEWAY,BankCard.GDB_NAME));//广发银行
		map.put(BankCard.SPDB, getBankValue(SPDB_GATEWAY,BankCard.SPDB_NAME));//浦东发展银行
		map.put(BankCard.BCM, getBankValue(BCM_GATEWAY,BankCard.BCM_NAME));//交通银行
		map.put(BankCard.ABC, getBankValue(ABC_GATEWAY,BankCard.ABC_NAME));//中国农业银行
		map.put(BankCard.CITIC, getBankValue(CITIC_GATEWAY,BankCard.CITIC_NAME));//中信银行
		map.put(BankCard.CEB, getBankValue(CEB_GATEWAY,BankCard.CEB_NAME));//中国光大银行
		map.put(BankCard.BOC, getBankValue(BOC_GATEWAY,BankCard.BOC_NAME));//中国银行
		map.put(BankCard.PSBC, getBankValue(PSBC_GATEWAY,BankCard.PSBC_NAME));//邮政储蓄
		map.put(BankCard.PINGAN, getBankValue(PINGAN_GATEWAY,BankCard.PINGAN_NAME));//平安银行
		map.put(BankCard.BANKOFSHANGHAI, getBankValue(BANKOFSHANGHAI_GATEWAY,BankCard.BANKOFSHANGHAI_NAME));//上海银行
		map.put(BankCard.VBANK, getBankValue(VBANK_GATEWAY,BankCard.VBANK_NAME));//虚拟银行
		**/
		return Collections.unmodifiableMap(map);
	}
	
	private AllinpayGateway(){}
	
	private static AllinpayGateway gateway = null;
	
	protected @Autowired @Resource(name="allinpayGateway") void init(final AllinpayGateway gateway){
		AllinpayGateway.gateway = gateway;
		/**
		 * 注册网关对象
		 */
		this.attach(ThirdPartyGateway.allinpay, AllinpayGateway.gateway);
	}
	/**
	 * 获取Spring注册的对象
	 * @return AllinpayGateway
	 */
	public static AllinpayGateway getInstance(){
		return gateway;
	}
}
