package com.elex.im.module.serverlogic;

import com.elex.common.component.net.INetClosedSessionHandler;
import com.elex.common.net.session.ISession;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

/**
 * 关闭 session 处理器
 * 
 * @author mausmars
 *
 */
public class ClosedSessionHandler implements INetClosedSessionHandler {
	protected static final ILogger logger = XLogUtil.logger();

	private ILogicContext context;

	public ClosedSessionHandler(ILogicContext context) {
		this.context = context;
	}

	@Override
	public void execute(ISession session) {
		if (logger.isDebugEnabled()) {
			logger.debug("ClosedSessionHandler sessionId = " + session.getSessionId());
		}

	}
}
