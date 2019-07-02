package com.elex.common.net.service.httpclient;

import java.util.Map;

import com.elex.common.net.INetClient;

/**
 * 网络httpclient
 * 
 * @author mausmars
 *
 */
public interface INetHttpClient extends INetClient {
	/**
	 * 发送Get请求
	 * 
	 * @param url
	 * @param params
	 * @param cb
	 */
	void doGet(String url, Map<String, Object> params, ARequestCallback cb);

	/**
	 * 发送post请求
	 * 
	 * @param url
	 * @param params
	 * @param cb
	 */
	void doPost(String url, Map<String, Object> params, ARequestCallback cb);

	void doPost(String url, String content, ARequestCallback cb);
}
