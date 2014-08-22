package com.xinhuanet.pay.service.impl;

import com.xinhuanet.pay.po.AppOrder;
import com.xinhuanet.pay.service.PayAppOrderService;
import com.xinhuanet.pay.service.QuartzService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 李野
 * Date: 13-5-28
 * Time: 上午11:20
 */
@Service("quartzServiceImpl")
public class QuartzServiceImpl implements QuartzService{

    private static final Logger logger = LoggerFactory.getLogger(QuartzServiceImpl.class);

    private @Autowired
    PayAppOrderService payAppOrderService;

    public void doIt() {
//        logger.info("开始执行后台轮询通知");
        List<AppOrder> list = payAppOrderService.getQuartzList();
//        System.out.println("list:"+list);
        if (list == null || list.size() == 0){
            return;
        }

        for (AppOrder payAppOrder : list){
            payAppOrderService.backgroundNotice(payAppOrder);
        }
        logger.info("后台轮询通知结束");

    }
}
