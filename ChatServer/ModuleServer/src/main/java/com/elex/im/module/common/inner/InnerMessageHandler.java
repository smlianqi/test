package com.elex.im.module.common.inner;

import com.elex.common.net.handler.IMessageInHandler;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

public abstract class InnerMessageHandler<T> implements IMessageInHandler<T> {
	protected static final ILogger logger = XLogUtil.logger();

	protected InnerMService service;

	public InnerMessageHandler(InnerMService service) {
		this.service = service;
	}

	public abstract MessageConfig createMessageConfig();
}
