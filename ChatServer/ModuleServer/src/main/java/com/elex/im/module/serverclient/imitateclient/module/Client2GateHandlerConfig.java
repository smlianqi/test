package com.elex.im.module.serverclient.imitateclient.module;

import java.util.List;

import com.elex.common.component.net.handlerconfig.ANetHandlerConfig;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.type.MegProtocolType;
import com.elex.common.service.IGlobalContext;
import com.elex.im.module.common.inner.InnerMService;
import com.elex.im.module.common.user.UserMService;
import com.elex.im.module.serverclient.imitateclient.IClientContext;

/**
 * 下行处理器配置
 * 
 * @author mausmars
 *
 */
public class Client2GateHandlerConfig extends ANetHandlerConfig {
	@Override
	public void createMessageConfigs(IGlobalContext c) {
		IClientContext context = (IClientContext) c;

		UserMService userService = new UserMService(context);
		userService = context.getModuleServiceMgr().insertService(userService);

		InnerMService innerMService = new InnerMService(context);
		innerMService = context.getModuleServiceMgr().insertService(innerMService);
		{
			// 内网指令
			com.elex.im.module.common.inner.handler.flat.InnerHandlerConfig innerHandlerConfig = new com.elex.im.module.common.inner.handler.flat.InnerHandlerConfig();
			List<MessageConfig> innerMessageConfigs = innerHandlerConfig.createHandler(context, innerMService);
			getMessageConfigs(MegProtocolType.flat).addAll(innerMessageConfigs);

			/// -----------------------------------------
			// 用户
			com.elex.im.module.common.user.handler.flat.UserHandlerConfig userHandlerConfig = new com.elex.im.module.common.user.handler.flat.UserHandlerConfig();
			List<MessageConfig> userMessageConfigs = userHandlerConfig.createHandler(userService);
			getMessageConfigs(MegProtocolType.flat).addAll(userMessageConfigs);

			/// -----------------------------------------
			// 聊天
			com.elex.im.module.serverclient.imitateclient.module.chat.handler.flat.ChatHandlerConfig chatHandlerConfig = new com.elex.im.module.serverclient.imitateclient.module.chat.handler.flat.ChatHandlerConfig();
			List<MessageConfig> chatMessageConfigs = chatHandlerConfig.createHandler(null);
			getMessageConfigs(MegProtocolType.flat).addAll(chatMessageConfigs);
		}
		// =================================================
		{
			// 内网指令
			com.elex.im.module.common.inner.handler.proto.InnerHandlerConfig innerHandlerConfig = new com.elex.im.module.common.inner.handler.proto.InnerHandlerConfig();
			List<MessageConfig> innerMessageConfigs = innerHandlerConfig.createHandler(context, innerMService);
			getMessageConfigs(MegProtocolType.proto).addAll(innerMessageConfigs);

			/// -----------------------------------------
			// 用户
			com.elex.im.module.common.user.handler.proto.UserHandlerConfig userHandlerConfig = new com.elex.im.module.common.user.handler.proto.UserHandlerConfig();
			List<MessageConfig> userMessageConfigs = userHandlerConfig.createHandler(userService);
			getMessageConfigs(MegProtocolType.proto).addAll(userMessageConfigs);

			/// -----------------------------------------
			// 聊天
			com.elex.im.module.serverclient.imitateclient.module.chat.handler.proto.ChatHandlerConfig chatHandlerConfig = new com.elex.im.module.serverclient.imitateclient.module.chat.handler.proto.ChatHandlerConfig();
			List<MessageConfig> chatMessageConfigs = chatHandlerConfig.createHandler(null);
			getMessageConfigs(MegProtocolType.proto).addAll(chatMessageConfigs);

		}
	}
}
