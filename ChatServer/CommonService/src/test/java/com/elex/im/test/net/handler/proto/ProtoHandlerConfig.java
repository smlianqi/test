package com.elex.im.test.net.handler.proto;

import java.util.ArrayList;
import java.util.List;

import com.elex.common.net.message.MessageConfig;
import com.elex.im.test.net.handler.LoginUpdataHandler;

/**
 * 下行处理器配置
 * 
 * @author mausmars
 *
 */
public class ProtoHandlerConfig {
	public List<MessageConfig> createHandler() {
		// 消息配置
		List<MessageConfig> messageConfigs = new ArrayList<MessageConfig>();

		ProtoTest1UpHandler testUpHandler = new ProtoTest1UpHandler();
		messageConfigs.add(testUpHandler.createMessageConfig());

		ProtoTest1DownHandler testDownHandler = new ProtoTest1DownHandler();
		messageConfigs.add(testDownHandler.createMessageConfig());

		// web处理器
		LoginUpdataHandler loginUpdataHandler = new LoginUpdataHandler();
		messageConfigs.add(loginUpdataHandler.createMessageConfig());

		return messageConfigs;
	}
}
