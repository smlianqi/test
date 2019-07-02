package com.elex.common.net.message;

import com.elex.common.net.handler.IMessageInHandler;

/**
 * 指令、消息、处理类封装
 * 
 * @author mausmars
 *
 */
public class MessageConfig {
	private String key;
	private Class<?> message;
	private IMessageInHandler<?> messageHandler;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setKey(int key) {
		this.key = String.valueOf(key);
	}

	public Class<?> getMessage() {
		return message;
	}

	public void setMessage(Class<?> message) {
		this.message = message;
	}

	public <T> T getMessageHandler() {
		return (T) messageHandler;
	}

	public void setMessageHandler(IMessageInHandler<?> messageHandler) {
		this.messageHandler = messageHandler;
	}
}
