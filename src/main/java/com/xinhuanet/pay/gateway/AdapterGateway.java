package com.xinhuanet.pay.gateway;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.xinhuanet.pay.common.Base;

/**
 * 获取适配的网关类型
 * @author duanwc
 *
 */
@Component
public class AdapterGateway extends Base{
	/**
	 * 私有构造方法
	 */
	private AdapterGateway() {}
	
	/**
	 * 获取支付网关属性
	 * @param gateway 第三方支付网关
	 * @param bank 银行标识
	 * @param subGateway 第三方网关对象
	 * @return 适配的网关类型枚举属性
	 */
	private Gateway getGateway(String gateway, String bank, BaseGateway<?> subGateway){
		Gateway.adapterGateeay.setGatewayConfigMap(subGateway.getConfigMap());
		if(Gateway.adapterGateeay.getGatewayConfigMap().get(bank)!=null){
			Gateway.adapterGateeay.setThirdGateway(gateway);
			Gateway.adapterGateeay.setGateway(((BankGatewayEntry)Gateway.adapterGateeay.getGatewayConfigMap().get(bank)).getKey());
			Gateway.adapterGateeay.setBankCardName(((BankGatewayEntry)Gateway.adapterGateeay.getGatewayConfigMap().get(bank)).getValue());
			return Gateway.adapterGateeay;
		}
		return null;
	}
	
	/**
	 * 获取支付网关属性
	 * @param bank 银行标识
	 * @return 适配的网关类型枚举属性
	 */
	public Gateway getGateway(String bank){
		/**
		 * 设置默认的优先级
		 */
		List<String> priorityList = new ArrayList<String>();
		priorityList.add("chinapnr");
		priorityList.add("allinpay");
		/**
		 * 获取配置文件内，支付网关的优先级
		 */
		List<?> list = props.getList("priority.gateway", priorityList);
		/**
		 * 是否随机选择第三方支付平台，默认为false
		 */
		if(props.getBoolean("priority.random", false)){
			Collections.shuffle(list);
		}
		/**
		 * 匹配网关
		 */
		Gateway gate = null;
		for(Object gateway : list){
			BaseGateway<?> bgw = BaseGateway.baseGatewayMap.get(gateway);
			if(bgw != null){
				gate = this.getGateway((String)gateway, bank, bgw);
				if(gate != null){
					logger.info("系统所选择的第三方网关:"+gateway + ",网关号:"+gate.getGateway() +",银行:"+gate.getBankCardName());
					break;
				}
			}
		}
		return gate;
	}
}
