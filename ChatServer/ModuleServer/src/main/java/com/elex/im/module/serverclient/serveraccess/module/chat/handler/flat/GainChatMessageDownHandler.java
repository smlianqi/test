package com.elex.im.module.serverclient.serveraccess.module.chat.handler.flat;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.player.IPlayer;
import com.elex.common.net.message.MessageConfig;
import com.elex.im.message.flat.CommondId_Chat;
import com.elex.im.message.flat.GainChatMessageDown;
import com.elex.im.module.serverclient.serveraccess.IChatClientGameHandler;
import com.elex.im.module.serverclient.serveraccess.service.IFlatReqCallBack;
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
			IFlatReqCallBack cb = (IFlatReqCallBack) callBack;
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
		messageConfig.setKey(CommondId_Chat.GainChatMessageDown);
		messageConfig.setMessage(GainChatMessageDown.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}