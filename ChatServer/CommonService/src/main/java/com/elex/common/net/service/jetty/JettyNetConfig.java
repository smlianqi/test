package com.elex.common.net.service.jetty;

import java.util.Map;

import javax.servlet.http.HttpServlet;

public class JettyNetConfig {
	protected String host; // host
	protected int port; // 端口

	protected int threadPoolCount = 200;
	protected int maxInactiveInterval = 86400000;
	protected String contextPath = "/paycheck";
	protected Map<String, HttpServlet> servletMap;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getThreadPoolCount() {
		return threadPoolCount;
	}

	public void setThreadPoolCount(int threadPoolCount) {
		this.threadPoolCount = threadPoolCount;
	}

	public int getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

	public void setMaxInactiveInterval(int maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public Map<String, HttpServlet> getServletMap() {
		return servletMap;
	}

	public void setServletMap(Map<String, HttpServlet> servletMap) {
		this.servletMap = servletMap;
	}
}
