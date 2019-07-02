package com.elex.im.test.net.httpclient.test;

import java.util.HashMap;
import java.util.Map;

import com.elex.common.net.service.httpclient.ARequestCallback;
import com.elex.common.net.service.httpclient.HttpAsyncClient;
import com.elex.common.net.service.httpclient.HttpClientNetConfig;
import com.elex.common.net.type.NetProtocolType;

public class HttpAsyncClientStarter {
	public static void main(String[] args) {
		HttpClientNetConfig httpClientNetConfig = new HttpClientNetConfig();
		httpClientNetConfig.setConnectionRequestTimeout(100);
		httpClientNetConfig.setConnectTimeout(100);
		httpClientNetConfig.setSocketTimeout(100);
		httpClientNetConfig.setDefaultMaxPerRoute(200);
		httpClientNetConfig.setMaxPoolTotal(100);
		httpClientNetConfig.setNetProtocolType(NetProtocolType.http);

		HttpAsyncClient httpAsyncClient = new HttpAsyncClient(1);
		httpAsyncClient.setConfig(httpClientNetConfig);

		httpAsyncClient.startup();

		Map<String, Object> params = new HashMap<String, Object>();
		String url = "https://github.com/";

		httpAsyncClient.doPost(url, params, new ARequestCallback() {
			@Override
			public void completed(String result) {
				System.out.println(result);
			}

			@Override
			public void failed(Exception ex) {
				System.err.println(ex);
			}

			@Override
			public void cancelled() {
			}
		});

	}
}
