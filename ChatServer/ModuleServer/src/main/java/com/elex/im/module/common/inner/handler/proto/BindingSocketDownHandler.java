package com.elex.im.module.common.inner.handler.proto;

import com.elex.common.message.proto.InnerMessage.BindingSocketDown;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.session.ISession;
import com.elex.im.module.common.inner.InnerMService;
import com.elex.im.module.common.inner.InnerMessageHandler;

public class BindingSocketDownHandler extends InnerMessageHandler<BindingSocketDown> {
	public BindingSocketDownHandler(InnerMService service) {
		super(service);
	}

	@Override
	public void inhandle(BindingSocketDown message, ISession session, Object attr) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> BindingSocketDownHandler!!! id=" + session.getSessionId());
		}
	}

	@Override
	public MessageConfig createMessageConfig() {
		MessageConfig messageConfig = new MessageConfig();
		messageConfig.setKey(BindingSocketDown.MessageEnum.CommondId_VALUE);
		messageConfig.setMessage(BindingSocketDown.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}