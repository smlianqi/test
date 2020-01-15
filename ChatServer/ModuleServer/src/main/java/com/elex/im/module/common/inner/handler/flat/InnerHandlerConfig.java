package com.elex.im.module.common.inner.handler.flat;

import java.util.ArrayList;
import java.util.List;

import com.elex.common.net.message.MessageConfig;
import com.elex.common.service.IGlobalContext;
import com.elex.im.module.common.inner.InnerMService;

/**
 * 
 */
public class InnerHandlerConfig {
	public List<MessageConfig> createHandler(IGlobalContext c, InnerMService service) {
		// 消息配置
		List<MessageConfig> messageConfigs = new ArrayList<MessageConfig>();

		// 上行处理
		{
			BindingSocketUpHandler upHandler = new BindingSocketUpHandler(service);
			messageConfigs.add(upHandler.createMessageConfig());
		}
		{
			PingCheckUpHandler upHandler = new PingCheckUpHandler(service);
			messageConfigs.add(upHandler.createMessageConfig());
		}
		{
			BroadcastUpHandler upHandler = new BroadcastUpHandler(service);
			messageConfigs.add(upHandler.createMessageConfig());
		}
		// 下行处理（只是为了注册消息）
		{
			BindingSocketDownHandler downHandler = new BindingSocketDownHandler(service);
			messageConfigs.add(downHandler.createMessageConfig());
		}
		return messageConfigs;
	}
}
