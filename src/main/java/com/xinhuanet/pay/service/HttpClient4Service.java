package com.xinhuanet.pay.service;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import java.util.List;

public interface HttpClient4Service {
	/**
	 * 获取GET请求
	 * @param path 后台应用路径
	 * @param params 客户端请求参数
	 * @return 一个GET请求对象
	 */
	public HttpGet getHttpGet(String path, List<NameValuePair> params);
	/**
	 * 获取POST请求
	 * @param path 后台应用路径
	 * @param params 客户端请求参数
	 * @return 一个POST请求对象
	 */
	public HttpPost getHttpPost(String path, List<NameValuePair> params);
	/**
	 * 获取请求结果，默认为GET方式
	 * @param path 后台应用路径
	 * @param params 客户端请求参数
	 * @return 结果为字符串
	 */
	public String getResponeResult(String path, List<NameValuePair> params);
	/**
	 * 通过指定的request请求方式，获取请求结果。
	 * @param path 后台应用路径
	 * @param params 客户端请求参数
	 * @param method 客户端request请求的方式，目前支持GET和POST，不区分大小写
	 * @return 结果为字符串
	 */
	public String getResponeResult(String path, List<NameValuePair> params, String method);
	
	/**
	 * 获取请求结果，默认为GET方式
	 * @param path 后台应用路径
	 * @param params 客户端请求参数
	 * @return 结果为字符串
	 */
	public String getResponeResult(String path);
}
