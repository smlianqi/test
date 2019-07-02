package com.elex.im.module.serverclient.serveraccess.module.chat.handler.proto;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.player.IPlayer;
import com.elex.common.net.message.MessageConfig;
import com.elex.im.message.proto.ChatMessage.SendChatMessageDown;
import com.elex.im.module.serverclient.serveraccess.IChatClientGameHandler;
import com.elex.im.module.serverclient.serveraccess.service.IProtoReqCallBack;
import com.elex.im.module.serverclient.serveraccess.service.IReqCallBack;

public class SendChatMessageDownHandler extends IChatClientGameHandler<SendChatMessageDown> {
	public SendChatMessageDownHandler(IReqCallBack callBack) {
		super(callBack);
	}

	@Override
	public void loginedHandler(SendChatMessageDown message, IPlayer player) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> SendChatMessageDownHandler!!! player=" + player.getUserId());
		}
		if (callBack != null) {
			IProtoReqCallBack cb = (IProtoReqCallBack) callBack;
			cb.sendChatMessageClassBack(player, message);
		}
	}

	@Override
	public FunctionType getFunctionType() {
		return FunctionType.chat;
	}

	public MessageConfig createMessageConfig() {
		MessageConfig messageConfig = new MessageConfig();
		// messageConfig.setKey(20024);
		messageConfig.setKey(SendChatMessageDown.MessageEnum.CommondId_VALUE);
		messageConfig.setMessage(SendChatMessageDown.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}