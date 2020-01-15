package com.elex.im.test.net.handler.flat;

import com.elex.common.net.handler.IMessageInHandler;
import com.elex.common.net.message.IMessage;
import com.elex.common.net.message.Message;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.session.ISession;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;
import com.elex.im.test.net.message.flat.CommondId_Test;
import com.elex.im.test.net.message.flat.Test1Bean;
import com.elex.im.test.net.message.flat.Test1Down;
import com.elex.im.test.net.message.flat.Test1Up;
import com.google.flatbuffers.FlatBufferBuilder;

public class FlatTest1UpHandler implements IMessageInHandler<Test1Up> {
	protected static final ILogger logger = XLogUtil.logger();

	@Override
	public void inhandle(Test1Up message, ISession session, Object attr) {
		if (logger.isInfoEnabled()) {
			logger.info(">>> TestUpHandler sessionId=" + session.getSessionId());
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

		// 回复消息
		FlatBufferBuilder builder = new FlatBufferBuilder(0);

		int tokenOffset = builder.createString(token);
		int userIdStrOffset = builder.createString(userId);
		int testBeanOffset = Test1Bean.createTest1Bean(builder, tokenOffset, rid, userIdStrOffset, asid);

		Test1Down.startTest1Down(builder);
		Test1Down.addTestBean(builder, testBeanOffset);
		int john = Test1Down.endTest1Down(builder);
		builder.finish(john);

		IMessage msg = Message.createFlatMessage(builder.dataBuffer(), Test1Down.class);

		session.send(msg);
	}

	public MessageConfig createMessageConfig() {
		MessageConfig messageConfig = new MessageConfig();
		messageConfig.setKey(CommondId_Test.Test1Up);
		messageConfig.setMessage(Test1Up.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}
