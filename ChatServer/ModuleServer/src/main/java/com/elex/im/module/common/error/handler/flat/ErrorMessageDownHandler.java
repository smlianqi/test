package com.elex.im.module.common.error.handler.flat;

import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.session.ISession;
import com.elex.im.message.flat.CommondId_Error;
import com.elex.im.message.flat.ErrorMessageDown;
import com.elex.im.module.common.error.ErrorMService;
import com.elex.im.module.common.error.ErrorMessageHandler;

public class ErrorMessageDownHandler extends ErrorMessageHandler<ErrorMessageDown> {
	public ErrorMessageDownHandler(ErrorMService service) {
		super(service);
	}

	@Override
	public void inhandle(ErrorMessageDown message, ISession session, Object attr) {

	}

	public MessageConfig createMessageConfig() {
		MessageConfig messageConfig = new MessageConfig();
		// messageConfig.setCommonId(10002);
		messageConfig.setKey(CommondId_Error.ErrorMessageDown);
		messageConfig.setMessage(ErrorMessageDown.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}
