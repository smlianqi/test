package com.elex.common.net.service.httpclient;

import com.elex.common.net.type.NetProtocolType;

public class HttpClientNetConfig {
	private int maxPoolTotal = 100;
	private int connectTimeout = 200;// 单位ms 和服务器建立连接的timeout
	private int socketTimeout = 1000;// 单位ms 从服务器读取数据的timeout
	private int connectionRequestTimeout = 200;// 单位ms 从连接池获取连接的timeout
	private int defaultMaxPerRoute = 200;
	private NetProtocolType netProtocolType;

	public int getMaxPoolTotal() {
		return maxPoolTotal;
	}

	public void setMaxPoolTotal(int maxPoolTotal) {
		this.maxPoolTotal = maxPoolTotal;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public int getConnectionRequestTimeout() {
		return connectionRequestTimeout;
	}

	public void setConnectionRequestTimeout(int connectionRequestTimeout) {
		this.connectionRequestTimeout = connectionRequestTimeout;
	}

	public int getDefaultMaxPerRoute() {
		return defaultMaxPerRoute;
	}

	public void setDefaultMaxPerRoute(int defaultMaxPerRoute) {
		this.defaultMaxPerRoute = defaultMaxPerRoute;
	}

	public NetProtocolType getNetProtocolType() {
		return netProtocolType;
	}

	public void setNetProtocolType(NetProtocolType netProtocolType) {
		this.netProtocolType = netProtocolType;
	}
}
