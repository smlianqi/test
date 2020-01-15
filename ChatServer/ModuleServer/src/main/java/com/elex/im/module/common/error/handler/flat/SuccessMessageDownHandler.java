package com.elex.im.module.common.error.handler.flat;

import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.session.ISession;
import com.elex.im.message.flat.CommondId_Error;
import com.elex.im.message.flat.SuccessMessageDown;
import com.elex.im.module.common.error.ErrorMService;
import com.elex.im.module.common.error.ErrorMessageHandler;

public class SuccessMessageDownHandler extends ErrorMessageHandler<SuccessMessageDown> {
	public SuccessMessageDownHandler(ErrorMService service) {
		super(service);
	}

	@Override
	public void inhandle(SuccessMessageDown message, ISession session, Object attr) {

	}

	public MessageConfig createMessageConfig() {
		MessageConfig messageConfig = new MessageConfig();
		// messageConfig.setCommonId(10002);
		messageConfig.setKey(CommondId_Error.SuccessMessageDown);
		messageConfig.setMessage(SuccessMessageDown.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}
