package com.elex.im.module.servertest;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.service.ServerService;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

/**
 * 战场服务
 * 
 * @author mausmars
 * 
 */
public class TestService extends ServerService implements ITestContext {
	protected static final ILogger logger = XLogUtil.logger();

	public TestService(FunctionType functionType) {
		super(functionType);
	}

	/**
	 * 开始服务
	 */
	@Override
	public void startup(boolean isBlock) {
		if (logger.isInfoEnabled()) {
			logger.info("TestService startup!");
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
			logger.info("TestService shutdown!");
		}
		// 调用父类关闭流程
		super.shutdown();
	}

}
