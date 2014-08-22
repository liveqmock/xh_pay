package com.xinhuanet.pay.service;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.xinhuanet.pay.common.RefundChangeState;
import com.xinhuanet.pay.po.RefundApply;
import com.xinhuanet.pay.po.RefundStep;


/**
 * Created with IntelliJ IDEA.
 * Date: 13-5-17
 * Time: 上午11:12
 */
public class RefundStepTest {

//    @Test
    public void addTest() throws Exception {
        String[] args = new String[]{"src/main/webapp/WEB-INF/config/app-config.xml", "src/main/webapp/WEB-INF/config/mvc-config.xml","src/main/webapp/WEB-INF/config/redis.xml"};
        ApplicationContext context = new FileSystemXmlApplicationContext(args);
        RefundStepService service = context.getBean(RefundStepService.class);
        RefundStep ref =new RefundStep();
       
        ref.setOrderId("20140311052232957319");//当前退款的原订单ID
        //1.
//        ref.setReason("购买导致误操作");//原因
//        ref.setStatus(RefundChangeState.REFUND_APPLY);//处理状态,1-退款申请,2-撤销申请,3-拒绝退款,4-同意退款,5-完成
//        ref.setStep(RefundChangeState.ROLE_USER);//角色步骤,0-用户,1-应用管理员,2-财务
//        ref.setHandleTime(new Date());//操作时间
//        ref.setIpAddress("172.18.11.184"); //IP地址
//        ref.setAdmin("duanwc");//操作人
        //2.
        ref.setReason("同意");//原因
        ref.setStatus(RefundChangeState.REFUND_AGREE);//处理状态,1-退款申请,2-撤销申请,3-拒绝退款,4-同意退款,5-完成
        ref.setStep(RefundChangeState.ROLE_APP_MANAGER);//角色步骤,0-用户,1-应用管理员,2-财务
        ref.setHandleTime(new Date());//操作时间
        ref.setIpAddress("172.18.0.1"); //IP地址
        ref.setAdmin("superman");//操作人
		
        
        int i = service.add(ref);
        assertEquals(1,i);
        //System.out.println(service);
    }
    
//    @Test
    public void getTest() throws Exception {
        String[] args = new String[]{"src/main/webapp/WEB-INF/config/app-config.xml", "src/main/webapp/WEB-INF/config/mvc-config.xml","src/main/webapp/WEB-INF/config/redis.xml"};
        ApplicationContext context = new FileSystemXmlApplicationContext(args);
        RefundStepService service = context.getBean(RefundStepService.class);

        RefundStep ref = service.get(1);
        assertEquals("20140311052232957319",ref.getOrderId());
    }
    
    @Test
    public void getListTest() throws Exception {
        String[] args = new String[]{"src/main/webapp/WEB-INF/config/app-config.xml", "src/main/webapp/WEB-INF/config/mvc-config.xml","src/main/webapp/WEB-INF/config/redis.xml"};
        ApplicationContext context = new FileSystemXmlApplicationContext(args);
        RefundStepService service = context.getBean(RefundStepService.class);

        List<RefundStep> list = service.getList("20140311052232957319");
        assertEquals(2,list.size());
    }
}
