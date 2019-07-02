package com.elex.im.module;

import java.util.HashMap;
import java.util.Map;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.net.rounter.IMessageRounter;

/**
 * 消息路由
 * 
 * @author mausmars
 *
 */
public class MessageRounter implements IMessageRounter {
	private Map<String, FunctionType> map = new HashMap<>();

	public MessageRounter() {
		init();
	}

	private void init() {
		// -------------------------------------------
		// user
		map.put("10001", FunctionType.chat);
		map.put("10002", FunctionType.client);
		map.put("10003", FunctionType.chat);
		map.put("10004", FunctionType.chat);
		// -------------------------------------------
		// error
		map.put("90001", FunctionType.client);
		map.put("90002", FunctionType.client);
		// -------------------------------------------
		// 聊天
		map.put("20001", FunctionType.chat);
		map.put("20002", FunctionType.client);
		map.put("20003", FunctionType.chat);
		map.put("20004", FunctionType.client);
		map.put("20021", FunctionType.chat);
		map.put("20022", FunctionType.client);
		map.put("20023", FunctionType.chat);
		map.put("20024", FunctionType.client);
		map.put("20025", FunctionType.chat);
		map.put("20026", FunctionType.client);
	}

	@Override
	public FunctionType forward(String commandId) {
		return map.get(commandId);
	}
}
