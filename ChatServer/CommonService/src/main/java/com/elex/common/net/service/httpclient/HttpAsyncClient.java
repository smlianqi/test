package com.elex.common.net.service.httpclient;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

import com.elex.common.net.type.NetProtocolType;
import com.elex.common.net.type.NetServiceType;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

public class HttpAsyncClient implements INetHttpClient {
	protected static final ILogger logger = XLogUtil.logger();

	private int id;
	private HttpClientNetConfig config;
	private RequestConfig requestConfig;

	private CloseableHttpAsyncClient httpAsyncClient;

	public HttpAsyncClient() {
	}

	public HttpAsyncClient(int id) {
		this.id = id;
	}

	@Override
	public void startup() {
		try {
			if (httpAsyncClient != null && httpAsyncClient.isRunning()) {
				return;
			}
			ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
			PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(ioReactor);
			cm.setMaxTotal(config.getMaxPoolTotal());
			cm.setDefaultMaxPerRoute(config.getDefaultMaxPerRoute());

			init();

			NetProtocolType netProtocolType = config.getNetProtocolType();
			HttpAsyncClientBuilder httpAsyncClientBuilder = HttpAsyncClients.custom();

			switch (netProtocolType) {
			case http:
				break;
			case https:
				SSLContext sslcontext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
					@Override
					public boolean isTrusted(java.security.cert.X509Certificate[] x509Certificates, String s)
							throws java.security.cert.CertificateException {
						return true;
					}
				}).build();
				httpAsyncClientBuilder.setSSLContext(sslcontext);
				break;
			default:
				break;
			}
			httpAsyncClientBuilder.setConnectionManager(cm);
			httpAsyncClient = httpAsyncClientBuilder.build();
			httpAsyncClient.start();
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	private void init() {
		RequestConfig.Builder configBuilder = RequestConfig.custom();
		// 设置连接超时
		configBuilder.setConnectTimeout(config.getConnectTimeout());
		// 设置读取超时
		configBuilder.setSocketTimeout(config.getSocketTimeout());
		// 设置从连接池获取连接实例的超时
		configBuilder.setConnectionRequestTimeout(config.getConnectionRequestTimeout());
		requestConfig = configBuilder.build();
	}

	@Override
	public void shutdown() {
		try {
			if (httpAsyncClient != null && httpAsyncClient.isRunning()) {
				httpAsyncClient.close();
			}
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	@Override
	public void doGet(String url, Map<String, Object> params, ARequestCallback cb) {
		String apiUrl = url;
		StringBuilder param = new StringBuilder();
		int i = 0;
		for (String key : params.keySet()) {
			if (i == 0) {
				param.append("?");
			} else {
				param.append("&");
			}
			param.append(key).append("=").append(params.get(key));
			i++;
		}
		apiUrl += param;
		try {
			HttpGet httpGet = new HttpGet(apiUrl);
			send(httpGet, cb);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void doPost(String url, String content, ARequestCallback cb) {
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(requestConfig);

		try {
			StringEntity s = new StringEntity(content);
			s.setContentEncoding("UTF-8");
			s.setContentType("application/json");

			httpPost.setEntity(s);
			// 发送
			send(httpPost, cb);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Override
	public void doPost(String url, Map<String, Object> params, ARequestCallback cb) {
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(requestConfig);

		List<NameValuePair> pairList = new ArrayList<>(params.size());
		try {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
				pairList.add(pair);
			}
			httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));
			// 发送
			send(httpPost, cb);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	private void send(Object message, ARequestCallback cb) throws Exception {
		HttpRequestBase httprequest = (HttpRequestBase) message;
		Future<HttpResponse> future = httpAsyncClient.execute(httprequest, new FutureCallback<HttpResponse>() {
			public void completed(HttpResponse response) {
				if (!cb.isASync()) {
					return;
				}
				String httpStr = ARequestCallback.getEntityContent(response);
				cb.completed(httpStr);
			}

			public void failed(final Exception ex) {
				cb.failed(ex);
			}

			public void cancelled() {
				cb.cancelled();
			}
		});
		cb.setFuture(future);
	}

	@Override
	public NetServiceType getNetServiceType() {
		return NetServiceType.httpclient;
	}

	@Override
	public NetProtocolType getNetProtocolType() {
		return config.getNetProtocolType();
	}
	// ---------------------------------

	@Override
	public int getId() {
		return id;
	}

	public void setConfig(HttpClientNetConfig config) {
		this.config = config;
	}
}
