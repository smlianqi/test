package com.elex.im.module.servergate.module;

import com.elex.common.component.net.handlerconfig.ANetHandlerConfig;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.type.MegProtocolType;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.module.IModuleServiceConfig;
import com.elex.im.module.common.error.ErrorMService;
import com.elex.im.module.common.inner.InnerMService;
import com.elex.im.module.common.user.UserMService;
import com.elex.im.module.servergate.IGateContext;

import java.util.List;

/**
 * 下行处理器配置
 * 
 * @author mausmars
 *
 */
public class GateHandlerConfig extends ANetHandlerConfig implements IModuleServiceConfig {
	@Override
	public void createMessageConfigs(IGlobalContext c) {
		IGateContext context = (IGateContext) c;

		// 服务
		UserMService userService = new UserMService(context);
		userService = context.getModuleServiceMgr().insertService(userService);

		// 内部指令服务
		InnerMService innerService = new InnerMService(context);
		innerService = context.getModuleServiceMgr().insertService(innerService);

		// 内部指令服务
		ErrorMService errorMService = new ErrorMService(context);
		errorMService = context.getModuleServiceMgr().insertService(errorMService);

		{
			// 内网指令
			com.elex.im.module.common.inner.handler.flat.InnerHandlerConfig bindingSocketHandlerConfig = new com.elex.im.module.common.inner.handler.flat.InnerHandlerConfig();
			List<MessageConfig> bindingSocketMessageConfigs = bindingSocketHandlerConfig.createHandler(context,
					innerService);
			getMessageConfigs(MegProtocolType.flat).addAll(bindingSocketMessageConfigs);

			// 用户
			com.elex.im.module.common.user.handler.flat.UserHandlerConfig userHandlerConfig = new com.elex.im.module.common.user.handler.flat.UserHandlerConfig();
			List<MessageConfig> userMessageConfigs = userHandlerConfig.createHandler(userService);
			getMessageConfigs(MegProtocolType.flat).addAll(userMessageConfigs);

			// 统一回复
			com.elex.im.module.common.error.handler.flat.ErrorHandlerConfig errorUpHandlerConfig = new com.elex.im.module.common.error.handler.flat.ErrorHandlerConfig();
			List<MessageConfig> errorMessageConfigs = errorUpHandlerConfig.createHandler(context, errorMService);
			getMessageConfigs(MegProtocolType.flat).addAll(errorMessageConfigs);

		}
		/// ==============================================
		{
			// 内网指令
			com.elex.im.module.common.inner.handler.proto.InnerHandlerConfig bindingSocketHandlerConfig = new com.elex.im.module.common.inner.handler.proto.InnerHandlerConfig();
			List<MessageConfig> bindingSocketMessageConfigs = bindingSocketHandlerConfig.createHandler(context,
					innerService);
			getMessageConfigs(MegProtocolType.proto).addAll(bindingSocketMessageConfigs);

			/// -----------------------------------------
			// 用户
			com.elex.im.module.common.user.handler.proto.UserHandlerConfig userHandlerConfig = new com.elex.im.module.common.user.handler.proto.UserHandlerConfig();
			List<MessageConfig> userMessageConfigs = userHandlerConfig.createHandler(userService);
			getMessageConfigs(MegProtocolType.proto).addAll(userMessageConfigs);

			// 统一回复
			com.elex.im.module.common.error.handler.proto.ErrorHandlerConfig errorUpHandlerConfig = new com.elex.im.module.common.error.handler.proto.ErrorHandlerConfig();
			List<MessageConfig> errorMessageConfigs = errorUpHandlerConfig.createHandler(context, errorMService);
			getMessageConfigs(MegProtocolType.proto).addAll(errorMessageConfigs);
		}
	}

	@Override
	public void init(IGlobalContext c) {
		IGateContext context = (IGateContext) c;
		// ----------------------
		// 初始化模块服务
		context.getModuleServiceMgr().initMService();
	}
}
