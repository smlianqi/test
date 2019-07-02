package com.elex.common.net.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

/**
 * MessageConfig管理器
 * 
 * @author mausmars
 *
 */
public class MessageConfigMgr implements IMessageConfigMgr {
	protected static final ILogger logger = XLogUtil.logger();

	// {消息cls:消息配置}
	private Map<Class<?>, MessageConfig> clsMap = new HashMap<Class<?>, MessageConfig>();
	// {指令号:消息配置}
	private Map<String, MessageConfig> keyMap = new HashMap<String, MessageConfig>();

	public MessageConfigMgr(List<MessageConfig> messageConfigs) {
		for (MessageConfig messageConfig : messageConfigs) {
			keyMap.put(messageConfig.getKey(), messageConfig);
			clsMap.put(messageConfig.getMessage(), messageConfig);
		}
	}

	@Override
	public String getKey(Class<?> msgClass) {
		MessageConfig messageConfig = clsMap.get(msgClass);
		if (messageConfig == null) {
			logger.error("MessageConfig is not existent!!! msgClass=" + msgClass);
			return null;
		}
		return messageConfig.getKey();
	}

	@Override
	public MessageConfig getMessageConfig(String key) {
		return keyMap.get(key);
	}
}
