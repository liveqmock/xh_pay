package com.xinhuanet.pay.service;

import com.xinhuanet.pay.po.SysConfig;

/**
 * Created with IntelliJ IDEA.
 * User: Ronny
 * Date: 13-11-20
 * Time: 上午9:36
 * 支付平台系统配置表
 */
public interface SysConfigService {
    /**
     * 获取单个配置项
     * @param key
     * @return
     */
    public SysConfig getSysConfig(String key);
}
