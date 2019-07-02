package com.elex.im.test.net.message;

import java.util.List;

import com.elex.common.net.service.netty.filter.http.HttpGetMessage;

public class LoginMessage {
	private HttpGetMessage httpGetMessage;

	public LoginMessage(HttpGetMessage httpGetMessage) {
		this.httpGetMessage = httpGetMessage;
	}

	public List<String> getAParamsValue() {
		return httpGetMessage.getParams().get("a");
	}
}
