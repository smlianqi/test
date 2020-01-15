package com.elex.im.module.common.error.handler.proto;

import java.util.ArrayList;
import java.util.List;

import com.elex.common.net.message.MessageConfig;
import com.elex.common.service.IGlobalContext;
import com.elex.im.module.common.error.ErrorMService;

/**
 * 
 */
public class ErrorHandlerConfig {
	public List<MessageConfig> createHandler(IGlobalContext c, ErrorMService service) {
		// 消息配置
		List<MessageConfig> messageConfigs = new ArrayList<MessageConfig>();

		// 上行处理

		// 下行处理（只是为了注册消息）
		{
			ErrorMessageDownHandler downHandler = new ErrorMessageDownHandler(service);
			messageConfigs.add(downHandler.createMessageConfig());
		}
		{
			SuccessMessageDownHandler downHandler = new SuccessMessageDownHandler(service);
			messageConfigs.add(downHandler.createMessageConfig());
		}
		return messageConfigs;
	}
}
