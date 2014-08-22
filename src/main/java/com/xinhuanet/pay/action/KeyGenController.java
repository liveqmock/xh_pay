package com.xinhuanet.pay.action;

import com.xinhuanet.pay.common.BaseController;
import com.xinhuanet.pay.po.KeyGen;
import com.xinhuanet.pay.service.KeyGenService;
import com.xinhuanet.pay.util.Function;
import com.xinhuanet.pay.util.KenGen;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: 李野
 * Date: 13-5-15
 * Time: 上午10:26
 */
@Controller
public class KeyGenController extends BaseController {

    private
    @Autowired
    KeyGenService keyGenService;

    /**
     * 生成应用对应密钥
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/kenGen.do")
    public String kenGen(HttpServletRequest request, HttpServletResponse response) {
    	String json = "";
        String merId = request.getParameter("merId");
        if (StringUtils.isEmpty(merId) || !Function.isNumber(merId)) {
        	json = "{\"code\":1,\"message\":\"失败\"}";
            return json;
        }
        String publicKey;
        String privateKey;
        try {
            Map<String, Object> keyMap = KenGen.genKeyPair();
            publicKey = KenGen.getPublicKey(keyMap);
            privateKey = KenGen.getPrivateKey(keyMap);
            KeyGen keyGen = new KeyGen(merId, publicKey, privateKey);
            boolean flag = keyGenService.addKey(keyGen);
            if(flag){
            	json = "{\"code\":0,\"message\":\"成功\"}";
            } else{
            	json = "{\"code\":1,\"message\":\"失败\"}";
            }
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            json = "{\"code\":1,\"message\":\"失败\"}";
            return json;
        }
    }

    @RequestMapping(value = "/getKey.do")
    @ResponseBody
    public String getKey(HttpServletRequest request, HttpServletResponse response) {

        String merId = request.getParameter("merId");
        String flag = request.getParameter("flag");

        if (StringUtils.isEmpty(merId)) {
            return "{\"code\":1,\"msg\":\"应用ID为空值\"}";
        }
        KeyGen keyGen = keyGenService.getKey(merId);

        if (keyGen == null) {
        	return "{\"code\":1,\"msg\":\"应用ID对应的密钥不存在，请重新生成\"}";
        }

        try {
            // 清空response
            response.reset();
            response.setContentType("application/octet-stream");

            OutputStream toClient = null;
            toClient = new BufferedOutputStream(response.getOutputStream());
            BufferedWriter localBufferedWriter = new BufferedWriter(new OutputStreamWriter(toClient));
            if ("public".equals(flag)) {
                localBufferedWriter.write("[NetPayClient]");
                localBufferedWriter.newLine();
                localBufferedWriter.write("PGID=888888");
                localBufferedWriter.newLine();
                localBufferedWriter.write("pubkey=" + keyGen.getPublicKey());
                response.addHeader("Content-Disposition", "attachment;filename=PgPubk" + merId + ".key");
            } else {
                localBufferedWriter.write("[NetPayClient]");
                localBufferedWriter.newLine();
                localBufferedWriter.write("MERID=" + merId);
                localBufferedWriter.newLine();
                localBufferedWriter.write("prikey=" + keyGen.getPrivateKey());
                response.addHeader("Content-Disposition", "attachment;filename=MerPrk" + merId + ".key");
            }
            localBufferedWriter.flush();
            localBufferedWriter.close();
            toClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
