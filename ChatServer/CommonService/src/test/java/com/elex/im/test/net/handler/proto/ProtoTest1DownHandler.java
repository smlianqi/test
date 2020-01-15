package com.elex.im.test.net.handler.proto;

import com.elex.common.net.handler.IMessageInHandler;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.session.ISession;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;
import com.elex.im.test.net.message.proto.Test1Message;
import com.elex.im.test.net.message.proto.Test1Message.Test1Down;

public class ProtoTest1DownHandler implements IMessageInHandler<Test1Down> {
	protected static final ILogger logger = XLogUtil.logger();

	@Override
	public void inhandle(Test1Down message, ISession session, Object attr) {
		if (logger.isInfoEnabled()) {
			logger.info(">>> TestDownHandler sessionId=" + session.getSessionId());
		}

		// 显示消息内容
		Test1Message.Test1Bean testBean = message.getTestBean();

		String userId = testBean.getUserIdStr();
		int rid = testBean.getRid();
		String token = testBean.getToken();
		int asid = testBean.getAsid();

		if (logger.isInfoEnabled()) {
			logger.info("sessionId=" + session.getSessionId() + " Message [userId = " + userId + ",rid = " + rid
					+ ",token=" + token + ",asid = " + asid + "]");
		}
	}

	public MessageConfig createMessageConfig() {
		MessageConfig messageConfig = new MessageConfig();
		messageConfig.setKey(1100101);
		messageConfig.setMessage(Test1Down.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}
