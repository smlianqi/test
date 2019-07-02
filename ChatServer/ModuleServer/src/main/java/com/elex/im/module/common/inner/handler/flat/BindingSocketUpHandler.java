package com.elex.im.module.common.inner.handler.flat;

import com.elex.common.message.flat.BindingSocketDown;
import com.elex.common.message.flat.BindingSocketUp;
import com.elex.common.message.flat.CommondId_Inner;
import com.elex.common.net.message.IMessage;
import com.elex.common.net.message.Message;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.session.ISession;
import com.elex.im.module.common.inner.InnerMService;
import com.elex.im.module.common.inner.InnerMessageHandler;
import com.google.flatbuffers.FlatBufferBuilder;

/**
 * 绑定socket请求
 * 
 * @author mausmars
 *
 */
public class BindingSocketUpHandler extends InnerMessageHandler<BindingSocketUp> {
	public BindingSocketUpHandler(InnerMService service) {
		super(service);
	}

	@Override
	public void inhandle(BindingSocketUp msg, ISession session, Object attr) {
		if (logger.isDebugEnabled()) {
			logger.debug("BindingSocketUpHandler! message=" + msg);
		}

		String fid = msg.serverId();
		int index = (int) msg.index();
		String token = msg.token();

		boolean isSuccess = service.bindingSocket(fid, index, token, session);
		if (!isSuccess) {
			return;
		}
		// 回复消息
		send(session);
	}

	private void send(ISession session) {
		FlatBufferBuilder builder = new FlatBufferBuilder(0);

		BindingSocketDown.startBindingSocketDown(builder);
		int john = BindingSocketDown.endBindingSocketDown(builder);
		builder.finish(john);

		IMessage msg = Message.createFlatMessage(builder.dataBuffer(), BindingSocketDown.class);
		session.send(msg);
	}

	public MessageConfig createMessageConfig() {
		MessageConfig messageConfig = new MessageConfig();
		// messageConfig.setKey(99901);
		messageConfig.setKey(CommondId_Inner.BindingSocketUp);
		messageConfig.setMessage(BindingSocketUp.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}