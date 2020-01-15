package com.elex.im.module.serverclient.serveraccess.module.bindingsocket.handler.flat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.elex.common.net.message.MessageConfig;
import com.elex.common.service.IGlobalContext;
import com.elex.im.module.common.inner.handler.flat.BindingSocketUpHandler;
import com.elex.im.module.common.inner.handler.flat.BroadcastUpHandler;
import com.elex.im.module.common.inner.handler.flat.PingCheckUpHandler;

/**
 * 
 */
public class InnerDownHandlerConfig {
	public List<MessageConfig> createHandler(IGlobalContext c) {
		// IGlobalContext context = (IGlobalContext) c;

		// 消息配置
		List<MessageConfig> messageConfigs = new ArrayList<MessageConfig>();

		// 下行处理
		BindingSocketDownHandler bindingSocketDownHandler = new BindingSocketDownHandler();
		// bindingSocketDownHandler.setContext(context);
		// bindingSocketDownHandler.setSessionBindingCountDownLatch(sessionBindingCountDownLatch);
		messageConfigs.add(bindingSocketDownHandler.createMessageConfig());

		// 上行处理（只是为了注册消息）
		BindingSocketUpHandler bindingSocketUpHandler = new BindingSocketUpHandler(null);
		messageConfigs.add(bindingSocketUpHandler.createMessageConfig());

		PingCheckUpHandler pingCheckUpHandler = new PingCheckUpHandler(null);
		messageConfigs.add(pingCheckUpHandler.createMessageConfig());

		BroadcastUpHandler broadcastUpHandler = new BroadcastUpHandler(null);
		messageConfigs.add(broadcastUpHandler.createMessageConfig());

		return messageConfigs;
	}
}
