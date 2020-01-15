package com.elex.im.test.net;

import com.elex.common.net.handler.IClosedSessionHandler;
import com.elex.common.net.session.ISession;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

public class ClosedSessionHandler implements IClosedSessionHandler {
	protected static final ILogger logger = XLogUtil.logger();

	@Override
	public void execute(ISession session) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> close!!! ");
		}
	}
}
