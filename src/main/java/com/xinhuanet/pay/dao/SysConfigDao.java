package com.xinhuanet.pay.dao;

import com.xinhuanet.pay.po.SysConfig;

/**
 * Created with IntelliJ IDEA.
 * User: Ronny
 * Date: 13-11-20
 * Time: 上午9:36
 * 支付平台系统配置表
 */
public interface SysConfigDao {
    /**
     * 获取单个配置项
     * @param key
     * @return
     */
    public SysConfig getSysConfig(String key);
}
