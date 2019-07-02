package com.elex.im.test.net.httpclient.translation;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.elex.common.net.service.httpclient.ARequestCallback;
import com.elex.common.net.service.httpclient.HttpAsyncClient;
import com.elex.common.net.service.httpclient.HttpClientNetConfig;
import com.elex.common.net.type.NetProtocolType;
import com.elex.common.util.json.JsonUtil;

public class TranslationTestStart {
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

		CokTranslationService translationService = new CokTranslationService(httpAsyncClient);

		List<String> toLang = new LinkedList<>();
		toLang.add("en");
		toLang.add("fr");

		// Map<String, String> ddd = translationService.translate("zh-CHS", "风味酸牛奶 Hello
		// world", toLang, 10000);
		boolean isSync = false;
		ARequestCallback cb = new ARequestCallback(isSync) {
			@Override
			public void completed(String response) {
				Map<String, String> map = JsonUtil.gsonString2Obj(response, Map.class);
				System.out.println("异步>>> " + map);
				// return result;
			}

			@Override
			public void failed(Exception ex) {
				System.err.println(ex);
			}

			@Override
			public void cancelled() {
			}
		};
		translationService.translate("", "Hello world 风味酸牛奶 ", toLang, cb);

		Future<String> future = cb.getFuture();
		if (future != null) {
			String response = null;
			try {
				response = future.get();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Map<String, String> map = JsonUtil.gsonString2Obj(response, Map.class);
			System.out.println("同步>>> " + map);
		}

		//
		// System.out.println(translationResult.getTranslatedContent());
		// System.out.println(translationResult.getOriginalContent());
		// System.out.println(translationResult.getDetectedLanguage());
	}
}
