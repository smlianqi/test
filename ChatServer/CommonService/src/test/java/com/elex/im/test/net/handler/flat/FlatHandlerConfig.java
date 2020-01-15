package com.elex.im.test.net.handler.flat;

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
public class FlatHandlerConfig {
	public List<MessageConfig> createHandler() {
		// 消息配置
		List<MessageConfig> messageConfigs = new ArrayList<MessageConfig>();

		FlatTest1UpHandler testUpHandler = new FlatTest1UpHandler();
		messageConfigs.add(testUpHandler.createMessageConfig());

		FlatTest1DownHandler testDownHandler = new FlatTest1DownHandler();
		messageConfigs.add(testDownHandler.createMessageConfig());

		// web处理器
		LoginUpdataHandler loginUpdataHandler = new LoginUpdataHandler();
		messageConfigs.add(loginUpdataHandler.createMessageConfig());

		return messageConfigs;
	}
}
