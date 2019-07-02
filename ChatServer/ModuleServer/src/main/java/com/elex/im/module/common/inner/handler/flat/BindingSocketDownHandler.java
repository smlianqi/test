package com.elex.im.module.common.inner.handler.flat;

import com.elex.common.message.flat.BindingSocketDown;
import com.elex.common.message.flat.CommondId_Inner;
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

	public MessageConfig createMessageConfig() {
		MessageConfig messageConfig = new MessageConfig();
		// messageConfig.setKey(99902);
		messageConfig.setKey(CommondId_Inner.BindingSocketDown);
		messageConfig.setMessage(BindingSocketDown.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}