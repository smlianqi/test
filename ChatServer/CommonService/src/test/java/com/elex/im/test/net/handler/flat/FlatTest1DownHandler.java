package com.elex.im.test.net.handler.flat;

import com.elex.common.net.handler.IMessageInHandler;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.session.ISession;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;
import com.elex.im.test.net.message.flat.CommondId_Test;
import com.elex.im.test.net.message.flat.Test1Bean;
import com.elex.im.test.net.message.flat.Test1Down;

public class FlatTest1DownHandler implements IMessageInHandler<Test1Down> {
	protected static final ILogger logger = XLogUtil.logger();

	@Override
	public void inhandle(Test1Down message, ISession session, Object attr) {
		if (logger.isInfoEnabled()) {
			logger.info(">>> TestDownHandler sessionId=" + session.getSessionId());
		}

		// 显示消息内容
		Test1Bean testBean = message.testBean();

		String userId = testBean.userIdStr();
		int rid = (int) testBean.rid();
		String token = testBean.token();
		int asid = (int) testBean.asid();

		if (logger.isInfoEnabled()) {
			logger.info("sessionId=" + session.getSessionId() + " Message [userId = " + userId + ",rid = " + rid
					+ ",token=" + token + ",asid = " + asid + "]");
		}
	}

	public MessageConfig createMessageConfig() {
		MessageConfig messageConfig = new MessageConfig();
		messageConfig.setKey(CommondId_Test.Test1Down);
		messageConfig.setMessage(Test1Down.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}
