package com.elex.im.module.common.error.handler.proto;

import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.session.ISession;
import com.elex.im.message.proto.ErrorMessage.ErrorMessageDown;
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
		messageConfig.setKey(ErrorMessageDown.MessageEnum.CommondId_VALUE);
		messageConfig.setMessage(ErrorMessageDown.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}
