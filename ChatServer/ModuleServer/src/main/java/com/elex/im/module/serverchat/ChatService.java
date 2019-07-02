package com.elex.im.module.serverchat;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.service.ServerService;
import com.elex.common.service.ServiceManager;

/**
 * 逻辑服务
 * 
 * @author mausmars
 * 
 */
public class ChatService extends ServerService implements IChatContext {
	public ChatService() {
		super(FunctionType.chat);
	}

	public ChatService(ServiceManager sm) {
		super(FunctionType.chat, sm);
	}

	/**
	 * 开始服务
	 */
	@Override
	public void startup(boolean isBlock) {
		if (logger.isInfoEnabled()) {
			logger.info("ChatService startup!");
		}
		// 调用父类启动流程
		super.startup(isBlock);
	}

	/**
	 * 停止服务
	 */
	@Override
	public void shutdown() {
		if (logger.isInfoEnabled()) {
			logger.info("ChatService shutdown!");
		}
		// 调用父类关闭流程
		super.shutdown();
	}
}
