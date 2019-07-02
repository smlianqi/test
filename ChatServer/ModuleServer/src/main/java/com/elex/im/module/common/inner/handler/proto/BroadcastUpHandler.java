package com.elex.im.module.common.inner.handler.proto;

import com.elex.common.message.BroadcastType;
import com.elex.common.message.proto.InnerMessage.BroadcastUp;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.session.ISession;
import com.elex.im.module.common.inner.InnerMService;
import com.elex.im.module.common.inner.InnerMessageHandler;
import com.google.protobuf.ByteString;

/**
 * 广播消息处理
 * 
 * @author mausmars
 *
 */
public class BroadcastUpHandler extends InnerMessageHandler<BroadcastUp> {
	public BroadcastUpHandler(InnerMService service) {
		super(service);
	}

	@Override
	public void inhandle(BroadcastUp message, ISession session, Object attr) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> BroadcastUpHandler! message=" + message);
		}

		BroadcastType broadcastType = BroadcastType.valueOf(message.getBroadcastType());

		ByteString byteString = message.getContent();
		byte[] data = byteString.toByteArray();

		switch (broadcastType) {
		case Users:
			service.forwardUsers(message.getCommandId(), data, message.getTargetUidsList(), session);
			break;
		case Whole:
			service.forwardWhole(message.getCommandId(), data, session);
			break;
		default:
			break;
		}
	}

	public MessageConfig createMessageConfig() {
		MessageConfig messageConfig = new MessageConfig();
		messageConfig.setKey(BroadcastUp.MessageEnum.CommondId_VALUE);
		messageConfig.setMessage(BroadcastUp.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}
