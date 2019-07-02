package com.elex.im.module.serverchat;

import com.elex.common.component.net.INetCreateSessionHandler;
import com.elex.common.component.timeout.ITimeoutManager;
import com.elex.common.net.session.ISession;
import com.elex.common.net.type.SessionAttachType;
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

	private ITimeoutManager timeoutManager;

	@Override
	public void init(ITimeoutManager timeoutManager) {
		this.timeoutManager = timeoutManager;
	}

	@Override
	public void execute(ISession session) {
		if (logger.isDebugEnabled()) {
			logger.debug("CreateSessionHandler sessionId = " + session.getSessionId());
		}
		session.setAttach(SessionAttachType.TimeoutManager, timeoutManager);
	}
}
