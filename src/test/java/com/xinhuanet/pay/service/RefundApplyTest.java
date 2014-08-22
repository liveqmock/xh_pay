package com.xinhuanet.pay.service;

import java.util.Date;

import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.xinhuanet.pay.po.RefundApply;


/**
 * Created with IntelliJ IDEA.
 * Date: 13-5-17
 * Time: 上午11:12
 */
public class RefundApplyTest {

    @Test
    public void addTest() throws Exception {
        String[] args = new String[]{"src/main/webapp/WEB-INF/config/app-config.xml", "src/main/webapp/WEB-INF/config/mvc-config.xml","src/main/webapp/WEB-INF/config/redis.xml"};
        ApplicationContext context = new FileSystemXmlApplicationContext(args);
        RefundApplyService service = context.getBean(RefundApplyService.class);
        RefundApply refApply =new RefundApply();
       
        refApply.setUid("29023531");//用户ID
		refApply.setLoginName("duanwc");//用户登录名称
		refApply.setOrderId("20140311052232957319");//当前退款的原订单ID
		refApply.setTrxId("20140311052232957319");//交易号，来源于第三方交易平台
		refApply.setRefOrdId("");//退款单完成后回来更新的订单ID
		 refApply.setAppId(140); //应用ID
		refApply.setMoney(300);//充值金额
		refApply.setPayType("allinpay");//第三方网关（支付宝-alipay、通联支付-allinpay、汇付天下-chinapnr）
		
		
		refApply.setReason("购买导致误操作");//申请退款原因
		refApply.setStatus(1);//申请状态(未处理-0，处理中-1，等待退款-2，处理完成-10)
		refApply.setStep(0);//申请步骤
		refApply.setApply(0);//主动申请或被动申请(主动-0，被动-1)
		refApply.setHandleTime(new Date());//申请时间
		refApply.setComment(null);//备注
		
        
        int i = service.add(refApply);
        assertEquals(1,i);
        //System.out.println(service);
    }
}
