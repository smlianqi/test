package com.elex.im.module.common.error;

import com.elex.common.net.handler.IMessageInHandler;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

public abstract class ErrorMessageHandler<T> implements IMessageInHandler<T> {
	protected static final ILogger logger = XLogUtil.logger();

	protected ErrorMService service;

	public ErrorMessageHandler(ErrorMService service) {
		this.service = service;
	}

	public abstract MessageConfig createMessageConfig();
}
