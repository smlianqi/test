package com.elex.im.module.common.inner.handler.flat;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

import com.elex.common.message.BroadcastType;
import com.elex.common.message.flat.BroadcastUp;
import com.elex.common.message.flat.CommondId_Inner;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.session.ISession;
import com.elex.common.util.ByteBufferUtil;
import com.elex.im.module.common.inner.InnerMService;
import com.elex.im.module.common.inner.InnerMessageHandler;

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
		BroadcastType broadcastType = BroadcastType.valueOf((int) message.broadcastType());

		ByteBuffer bb = message.contentAsByteBuffer();
		byte[] data = ByteBufferUtil.byteBuffer2Bytes(bb);

		switch (broadcastType) {
		case Users:
			List<String> uids = new LinkedList<>();
			for (int i = 0; i < message.targetUidsLength(); i++) {
				uids.add(message.targetUids(i));
			}
			service.forwardUsers((int) message.commandId(), data, uids, session);
			break;
		case Whole:
			service.forwardWhole((int) message.commandId(), data, session);
			break;
		default:
			break;
		}
	}

	public MessageConfig createMessageConfig() {
		MessageConfig messageConfig = new MessageConfig();
		messageConfig.setKey(CommondId_Inner.BroadcastUp);
		messageConfig.setMessage(BroadcastUp.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}
