package com.elex.im.module.common.inner.handler.proto;

import com.elex.common.message.proto.InnerMessage.PingCheckUp;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.session.ISession;
import com.elex.im.module.common.inner.InnerMService;
import com.elex.im.module.common.inner.InnerMessageHandler;

/**
 * ping处理
 * 
 * @author mausmars
 *
 */
public class PingCheckUpHandler extends InnerMessageHandler<PingCheckUp> {
	public PingCheckUpHandler(InnerMService service) {
		super(service);
	}

	@Override
	public void inhandle(PingCheckUp message, ISession session, Object attr) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> PingCheckUp!!!");
		}
	}

	public MessageConfig createMessageConfig() {
		MessageConfig messageConfig = new MessageConfig();
		messageConfig.setKey(PingCheckUp.MessageEnum.CommondId_VALUE);
		messageConfig.setMessage(PingCheckUp.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}
