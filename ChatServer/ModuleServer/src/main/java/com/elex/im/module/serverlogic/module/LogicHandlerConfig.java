package com.elex.im.module.serverlogic.module;

import java.util.List;

import com.elex.common.component.net.handlerconfig.ANetHandlerConfig;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.type.MegProtocolType;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.module.IModuleServiceConfig;
import com.elex.im.module.common.user.UserMService;
import com.elex.im.module.serverlogic.ILogicContext;

/**
 * 下行处理器配置
 * 
 * @author mausmars
 *
 */
public class LogicHandlerConfig extends ANetHandlerConfig implements IModuleServiceConfig {
	@Override
	public void createMessageConfigs(IGlobalContext c) {
		ILogicContext context = (ILogicContext) c;

		// 服务
		UserMService userService = new UserMService(context);
		userService = context.getModuleServiceMgr().insertService(userService);
		{
			/// -----------------------------------------
			// 用户
			com.elex.im.module.common.user.handler.flat.UserHandlerConfig userHandlerConfig = new com.elex.im.module.common.user.handler.flat.UserHandlerConfig();
			List<MessageConfig> userMessageConfigs = userHandlerConfig.createHandler(userService);
			getMessageConfigs(MegProtocolType.flat).addAll(userMessageConfigs);
		}
		/// ==============================================
		{
			/// -----------------------------------------
			// 用户
			com.elex.im.module.common.user.handler.proto.UserHandlerConfig userHandlerConfig = new com.elex.im.module.common.user.handler.proto.UserHandlerConfig();
			List<MessageConfig> userMessageConfigs = userHandlerConfig.createHandler(userService);
			getMessageConfigs(MegProtocolType.proto).addAll(userMessageConfigs);
		}
	}

	@Override
	public void init(IGlobalContext c) {
		ILogicContext context = (ILogicContext) c;
		// ----------------------
		// 初始化模块服务
		context.getModuleServiceMgr().initMService();
	}
}
