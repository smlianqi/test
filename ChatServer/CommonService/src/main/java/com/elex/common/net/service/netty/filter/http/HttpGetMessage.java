package com.elex.common.net.service.netty.filter.http;

import java.util.List;
import java.util.Map;

public class HttpGetMessage {
	private String path;
	private Map<String, List<String>> params;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Map<String, List<String>> getParams() {
		return params;
	}

	public void setParams(Map<String, List<String>> params) {
		this.params = params;
	}
}
