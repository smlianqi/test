package com.elex.im.module.serverchat.module;

import java.util.List;

import com.elex.common.component.net.handlerconfig.ANetHandlerConfig;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.type.MegProtocolType;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.module.IModuleServiceConfig;
import com.elex.im.module.common.error.ErrorMService;
import com.elex.im.module.common.inner.InnerMService;
import com.elex.im.module.serverchat.IChatContext;
import com.elex.im.module.serverchat.module.chat.ChatMService;
import com.elex.im.module.serverchat.module.user.ChatUserMService;

/**
 * 下行处理器配置
 * 
 * @author mausmars
 *
 */
public class ChatHandlerConfig extends ANetHandlerConfig implements IModuleServiceConfig {
	@Override
	public void createMessageConfigs(IGlobalContext c) {
		IChatContext context = (IChatContext) c;

		// 服务
		ChatMService chatMService = new ChatMService(context);
		chatMService = context.getModuleServiceMgr().insertService(chatMService);

		// 服务，保证一个服务只有一个service
		ChatUserMService userService = new ChatUserMService(context);
		userService.setPlayerInitOffline(chatMService);
		userService = context.getModuleServiceMgr().insertService(userService);

		// 内部指令服务
		InnerMService innerService = new InnerMService(context);
		innerService = context.getModuleServiceMgr().insertService(innerService);

		// 统一回复指令服务
		ErrorMService errorMService = new ErrorMService(context);
		errorMService = context.getModuleServiceMgr().insertService(errorMService);
		{
			// flat
			// 内网指令
			com.elex.im.module.common.inner.handler.flat.InnerHandlerConfig bindingSocketHandlerConfig = new com.elex.im.module.common.inner.handler.flat.InnerHandlerConfig();
			List<MessageConfig> bindingSocketMessageConfigs = bindingSocketHandlerConfig.createHandler(context,
					innerService);
			getMessageConfigs(MegProtocolType.flat).addAll(bindingSocketMessageConfigs);

			/// -----------------------------------------
			// 统一回复
			com.elex.im.module.common.error.handler.flat.ErrorHandlerConfig errorHandlerConfig = new com.elex.im.module.common.error.handler.flat.ErrorHandlerConfig();
			List<MessageConfig> errorMessageConfigs = errorHandlerConfig.createHandler(context, errorMService);
			getMessageConfigs(MegProtocolType.flat).addAll(errorMessageConfigs);

			/// -----------------------------------------
			// 用户
			com.elex.im.module.common.user.handler.flat.UserHandlerConfig userHandlerConfig = new com.elex.im.module.common.user.handler.flat.UserHandlerConfig();
			List<MessageConfig> userMessageConfigs = userHandlerConfig.createHandler(userService);
			getMessageConfigs(MegProtocolType.flat).addAll(userMessageConfigs);

			/// -----------------------------------------
			// 聊天
			com.elex.im.module.serverchat.module.chat.handler.flat.ChatMHandlerConfig chatMHandlerConfig = new com.elex.im.module.serverchat.module.chat.handler.flat.ChatMHandlerConfig();
			List<MessageConfig> chatMessageConfigs = chatMHandlerConfig.createHandler(chatMService);
			getMessageConfigs(MegProtocolType.flat).addAll(chatMessageConfigs);
		}
		// =================================================================
		{
			// proto
			// 内网指令
			com.elex.im.module.common.inner.handler.proto.InnerHandlerConfig bindingSocketHandlerConfig = new com.elex.im.module.common.inner.handler.proto.InnerHandlerConfig();
			List<MessageConfig> bindingSocketMessageConfigs = bindingSocketHandlerConfig.createHandler(context,
					innerService);
			getMessageConfigs(MegProtocolType.proto).addAll(bindingSocketMessageConfigs);

			/// -----------------------------------------
			// 统一回复
			com.elex.im.module.common.error.handler.proto.ErrorHandlerConfig errorHandlerConfig = new com.elex.im.module.common.error.handler.proto.ErrorHandlerConfig();
			List<MessageConfig> errorMessageConfigs = errorHandlerConfig.createHandler(context, errorMService);
			getMessageConfigs(MegProtocolType.proto).addAll(errorMessageConfigs);
			
			/// -----------------------------------------
			// 用户
			com.elex.im.module.common.user.handler.proto.UserHandlerConfig userHandlerConfig = new com.elex.im.module.common.user.handler.proto.UserHandlerConfig();
			List<MessageConfig> userMessageConfigs = userHandlerConfig.createHandler(userService);
			getMessageConfigs(MegProtocolType.proto).addAll(userMessageConfigs);

			/// -----------------------------------------
			// 聊天
			com.elex.im.module.serverchat.module.chat.handler.proto.ChatMHandlerConfig chatMHandlerConfig = new com.elex.im.module.serverchat.module.chat.handler.proto.ChatMHandlerConfig();
			List<MessageConfig> chatMessageConfigs = chatMHandlerConfig.createHandler(chatMService);
			getMessageConfigs(MegProtocolType.proto).addAll(chatMessageConfigs);
		}
	}

	@Override
	public void init(IGlobalContext c) {
		IChatContext context = (IChatContext) c;
		// ----------------------
		// 初始化模块服务
		context.getModuleServiceMgr().initMService();
	}
}
