package com.elex.im.module.serverclient.serveraccess.module.chat.handler.proto;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.player.IPlayer;
import com.elex.common.net.message.MessageConfig;
import com.elex.im.message.proto.ChatMessage.GainChatMessageDown;
import com.elex.im.module.serverclient.serveraccess.IChatClientGameHandler;
import com.elex.im.module.serverclient.serveraccess.service.IProtoReqCallBack;
import com.elex.im.module.serverclient.serveraccess.service.IReqCallBack;

public class GainChatMessageDownHandler extends IChatClientGameHandler<GainChatMessageDown> {
	public GainChatMessageDownHandler(IReqCallBack callBack) {
		super(callBack);
	}

	@Override
	public void loginedHandler(GainChatMessageDown message, IPlayer player) {
		if (logger.isDebugEnabled()) {
			logger.debug("Client GainChatMessageDownHandler!!! ");
		}
		if (callBack != null) {
			IProtoReqCallBack cb = (IProtoReqCallBack) callBack;
			cb.gainChatMessageClassBack(player, message);
		}
	}

	@Override
	public FunctionType getFunctionType() {
		return FunctionType.chat;
	}

	public MessageConfig createMessageConfig() {
		MessageConfig messageConfig = new MessageConfig();
		// messageConfig.setKey(20022);
		messageConfig.setKey(GainChatMessageDown.MessageEnum.CommondId_VALUE);
		messageConfig.setMessage(GainChatMessageDown.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}