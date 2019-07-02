package com.elex.im.module.common.inner.handler.proto;

import com.elex.common.message.proto.InnerMessage.BindingSocketDown;
import com.elex.common.message.proto.InnerMessage.BindingSocketUp;
import com.elex.common.net.message.IMessage;
import com.elex.common.net.message.Message;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.session.ISession;
import com.elex.im.module.common.inner.InnerMService;
import com.elex.im.module.common.inner.InnerMessageHandler;

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
			logger.debug(">>> BindingSocketUpHandler! message=" + msg);
		}

		String fid = msg.getServerId();
		int index = msg.getIndex();
		String token = msg.getToken();

		boolean isSuccess = service.bindingSocket(fid, index, token, session);
		if (!isSuccess) {
			return;
		}
		// 回复消息
		send(session);
	}

	private void send(ISession session) {
		BindingSocketDown.Builder builder = BindingSocketDown.newBuilder();
		IMessage msg = Message.createProtoMessage(builder.build());
		session.send(msg);
	}

	@Override
	public MessageConfig createMessageConfig() {
		MessageConfig messageConfig = new MessageConfig();
		messageConfig.setKey(BindingSocketUp.MessageEnum.CommondId_VALUE);
		messageConfig.setMessage(BindingSocketUp.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}