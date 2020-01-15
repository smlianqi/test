package com.elex.im.module.serverclient.serveraccess.module.bindingsocket.handler.proto;

import com.elex.common.message.proto.InnerMessage.BindingSocketDown;
import com.elex.common.net.handler.IMessageInHandler;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.session.ISession;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

public class BindingSocketDownHandler implements IMessageInHandler<BindingSocketDown> {
	protected static final ILogger logger = XLogUtil.logger();

	// private IGlobalContext context;
//	protected CountDownLatch sessionBindingCountDownLatch;

	public BindingSocketDownHandler() {
	}

	@Override
	public void inhandle(BindingSocketDown message, ISession session, Object attr) {
		if (logger.isDebugEnabled()) {
			logger.debug("BindingSocketDownHandler");
		}
		// 数量减一
//		sessionBindingCountDownLatch.countDown();
	}

	public MessageConfig createMessageConfig() {
		MessageConfig messageConfig = new MessageConfig();
		// messageConfig.setKey(99902);
		messageConfig.setKey(BindingSocketDown.MessageEnum.CommondId_VALUE);
		messageConfig.setMessage(BindingSocketDown.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}

	// -----------------------------------------------
//	public void setSessionBindingCountDownLatch(CountDownLatch sessionBindingCountDownLatch) {
//		this.sessionBindingCountDownLatch = sessionBindingCountDownLatch;
//	}

	// public void setContext(IGlobalContext context) {
	// this.context = context;
	// }

}