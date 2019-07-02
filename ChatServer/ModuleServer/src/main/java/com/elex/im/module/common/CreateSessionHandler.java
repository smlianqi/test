package com.elex.im.module.common;

import com.elex.common.component.net.INetCreateSessionHandler;
import com.elex.common.component.timeout.ITimeoutManager;
import com.elex.common.net.session.ISession;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

/**
 * 创建session处理器
 * 
 * @author mausmars
 *
 */
public class CreateSessionHandler implements INetCreateSessionHandler {
	protected static final ILogger logger = XLogUtil.logger();

	@Override
	public void init(ITimeoutManager timeoutManager) {
	}

	@Override
	public void execute(ISession session) {
		if (logger.isDebugEnabled()) {
			logger.debug("CreateSessionHandler sessionId = " + session.getSessionId());
		}
	}
}
