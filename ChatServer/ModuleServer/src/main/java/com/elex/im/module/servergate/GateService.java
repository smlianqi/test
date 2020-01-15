package com.elex.im.module.servergate;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.service.ServerService;
import com.elex.common.service.ServiceManager;

/**
 * 逻辑服务
 * 
 * @author mausmars
 * 
 */
public class GateService extends ServerService implements IGateContext {
	public GateService() {
		super(FunctionType.gate);
	}

	public GateService(ServiceManager sm) {
		super(FunctionType.logic, sm);
	}

	/**
	 * 开始服务
	 */
	@Override
	public void startup(boolean isBlock) {
		if (logger.isInfoEnabled()) {
			logger.info("GateService startup!");
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
			logger.info("GateService shutdown!");
		}
		// 调用父类关闭流程
		super.shutdown();
	}
}
