package com.xinhuanet.pay.service.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xinhuanet.pay.common.Base;
import com.xinhuanet.pay.service.HttpClient4Service;

@Service
public class HttpClient4ServiceImpl extends Base implements HttpClient4Service {

    protected final Logger logger = Logger.getLogger(getClass());
    private final static String ENCODE = "UTF-8";
    private final static int TIMEOUT = 3000;
    /**
     * scheme key
     */
    private final static String  scheme= "scheme";
    /**
     * host key
     */
    private final static String  host= "host";
    /**
     * path key
     */
    private final static String  path= "path";

    @Override
    public HttpGet getHttpGet(String url, List<NameValuePair> params) {
        URI uri = null;
        try {
        	Map<String,String> map = analysisUrl(url);
            uri = URIUtils.createURI(map.get(scheme), map.get(host), -1, map.get(path), URLEncodedUtils.format(params, ENCODE), null);
            logger.info("GET:"+uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return new HttpGet(uri);
    }

    @Override
    public HttpPost getHttpPost(String url, List<NameValuePair> params) {
        URI uri = null;
        try {
        	Map<String,String> map = analysisUrl(url);
            uri = URIUtils.createURI(map.get(scheme), map.get(host), -1, map.get(path), URLEncodedUtils.format(params, ENCODE), null);
            logger.info("POST:"+uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return new HttpPost(uri);
    }
    /**
     * 分析url，解析成schem、host、path
     * @param url
     * @return HashMap<String,String>，如果存在则k为：schem|host|path，v为：对应的值；如果url解析失败，则返回一个空的HashMap对象，不是null
     */
    private Map<String,String> analysisUrl(String url){
    	Map<String,String> map = new HashMap<String, String>();
    	if(StringUtils.isBlank(url)){
    		return map;
    	}
    	String scheme = null;
    	String host = null;
    	String path = null;
    	if(url.indexOf("http:") < 0 && url.indexOf("https:") < 0){
    		scheme = "http";
    	} else if(url.indexOf("http:") >= 0 || url.indexOf("https:") >= 0){
    		String[] tmps = url.split("//",2);
    		url = tmps[1];
    		scheme = tmps[0].substring(0,tmps[0].length()-1);
    	}
		map.put("scheme", scheme);
		host = url.substring(0,url.indexOf("/"));
		map.put("host", host);
		path = url.substring(url.indexOf("/")+1);
		map.put("path", path);
    	return map;
    }

    @Override
    public String getResponeResult(String path, List<NameValuePair> params, String method) {
        HttpClient httpClient = new DefaultHttpClient();

        // 代理的设置
//        HttpHost proxy = new HttpHost("202.84.17.41", 8080);
//        httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
//        httpClient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");
        
		// 代理的设置
		Boolean using = props.getBoolean("http.proxy.using", false);
		if(using){
			String address = props.getString("http.proxy.address");
			Integer port = props.getInt("http.proxy.port");
		    HttpHost proxy = new HttpHost(address, port);
		    httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}
        
        //设置超时时间
        httpClient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT);

        StringBuffer result = new StringBuffer();
        HttpRequestBase request = null;
        try {
            if (method.toUpperCase().equals("POST")) {
                request = this.getHttpPost(path, params);
            } else {
                request = this.getHttpGet(path, params);
            }
            HttpResponse res = httpClient.execute(request);
            HttpEntity entity = res.getEntity();
            if (entity != null) {
                try {
                    result.append(EntityUtils.toString(entity, ENCODE));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (request != null && !request.isAborted()) {
                request.abort();
            }
            httpClient.getConnectionManager().shutdown();
        }
        return result.toString();
    }

    @Override
    public String getResponeResult(String path, List<NameValuePair> params) {
        return this.getResponeResult(path, params, "GET");
    }
    
    @Override
    public String getResponeResult(String path) {
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	return this.getResponeResult(path, params, "GET");
    }

//    public static void main(String[] args) {
//        HttpClient4ServiceImpl service = new HttpClient4ServiceImpl();
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("wd", "nba"));
//        params.add(new BasicNameValuePair("rsv_spt", "1"));
//        params.add(new BasicNameValuePair("issp", "1"));
//        params.add(new BasicNameValuePair("rsv_bp", "0"));
//        params.add(new BasicNameValuePair("ie", "utf-8"));
//        params.add(new BasicNameValuePair("tn", "baiduhome_pg"));
//        params.add(new BasicNameValuePair("rsv_sug3", "1"));
//        params.add(new BasicNameValuePair("rsv_sug", "0"));
//        params.add(new BasicNameValuePair("rsv_sug1", "1"));
//        params.add(new BasicNameValuePair("rsv_sug4", "21"));
//        String result =  service.getResponeResult("http://www.baidu.com", params);


//        String ss = "http://888.qq.com/info/award/index.php?mod=kuaicai&loty_name=gdx&qihao=20130531";
//
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("mod", "kuaicai"));
//        params.add(new BasicNameValuePair("loty_name", "gdx"));
//        params.add(new BasicNameValuePair("qihao", "20130531"));
//        String result =  service.getResponeResult("http://888.qq.com/info/award/index.php", params);
//
//        System.out.println(result);
//    }

}
