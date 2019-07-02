package com.elex.im.module.serverclient.serveraccess.module;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.elex.common.component.net.handlerconfig.ANetHandlerConfig;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.type.MegProtocolType;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.module.IModuleServiceConfig;
import com.elex.im.module.common.user.UserMService;
import com.elex.im.module.serverclient.serveraccess.IChatClientContext;
import com.elex.im.module.serverclient.serveraccess.service.IReqCallBack;

/**
 * 下行处理器配置
 * 
 * @author mausmars
 *
 */
public class ChatClient2GateHandlerConfig extends ANetHandlerConfig implements IModuleServiceConfig {
	private IReqCallBack callBack;
	// private CountDownLatch sessionBindingCountDownLatch;

	public ChatClient2GateHandlerConfig() {
	}

	@Override
	public void createMessageConfigs(IGlobalContext c) {
		IChatClientContext context = (IChatClientContext) c;

		UserMService userService = new UserMService(c);
		userService = context.getModuleServiceMgr().insertService(userService);

		{
			// 内网指令
			com.elex.im.module.serverclient.serveraccess.module.bindingsocket.handler.flat.InnerDownHandlerConfig bindingSocketHandlerConfig = new com.elex.im.module.serverclient.serveraccess.module.bindingsocket.handler.flat.InnerDownHandlerConfig();
			List<MessageConfig> bindingSocketMessageConfigs = bindingSocketHandlerConfig.createHandler(context);
			getMessageConfigs(MegProtocolType.flat).addAll(bindingSocketMessageConfigs);

			// -----------------------------------------------
			// 用户
			com.elex.im.module.serverclient.serveraccess.module.user.handler.flat.UserHandlerConfig userHandlerConfig = new com.elex.im.module.serverclient.serveraccess.module.user.handler.flat.UserHandlerConfig();
			List<MessageConfig> userMessageConfigs = userHandlerConfig.createHandler(userService, callBack);
			getMessageConfigs(MegProtocolType.flat).addAll(userMessageConfigs);

			// -----------------------------------------------
			// 聊天
			com.elex.im.module.serverclient.serveraccess.module.chat.handler.flat.ChatHandlerConfig chatHandlerConfig = new com.elex.im.module.serverclient.serveraccess.module.chat.handler.flat.ChatHandlerConfig();
			List<MessageConfig> chatMessageConfigs = chatHandlerConfig.createHandler(null, callBack);
			getMessageConfigs(MegProtocolType.flat).addAll(chatMessageConfigs);
		}
		// =================================================
		{
			// 内网指令
			com.elex.im.module.serverclient.serveraccess.module.bindingsocket.handler.proto.InnerDownHandlerConfig bindingSocketHandlerConfig = new com.elex.im.module.serverclient.serveraccess.module.bindingsocket.handler.proto.InnerDownHandlerConfig();
			List<MessageConfig> bindingSocketMessageConfigs = bindingSocketHandlerConfig.createHandler(context);
			getMessageConfigs(MegProtocolType.proto).addAll(bindingSocketMessageConfigs);

			// -----------------------------------------------
			// 用户
			com.elex.im.module.serverclient.serveraccess.module.user.handler.proto.UserHandlerConfig userHandlerConfig = new com.elex.im.module.serverclient.serveraccess.module.user.handler.proto.UserHandlerConfig();
			List<MessageConfig> userMessageConfigs = userHandlerConfig.createHandler(userService, callBack);
			getMessageConfigs(MegProtocolType.proto).addAll(userMessageConfigs);

			// -----------------------------------------------
			// 聊天
			com.elex.im.module.serverclient.serveraccess.module.chat.handler.proto.ChatHandlerConfig chatHandlerConfig = new com.elex.im.module.serverclient.serveraccess.module.chat.handler.proto.ChatHandlerConfig();
			List<MessageConfig> chatMessageConfigs = chatHandlerConfig.createHandler(null, callBack);
			getMessageConfigs(MegProtocolType.proto).addAll(chatMessageConfigs);
		}
	}

	@Override
	public void init(IGlobalContext c) {
	}

	public void setCallBack(IReqCallBack callBack) {
		this.callBack = callBack;
	}

//	public void setSessionBindingCountDownLatch(CountDownLatch sessionBindingCountDownLatch) {
//		this.sessionBindingCountDownLatch = sessionBindingCountDownLatch;
//	}

}
