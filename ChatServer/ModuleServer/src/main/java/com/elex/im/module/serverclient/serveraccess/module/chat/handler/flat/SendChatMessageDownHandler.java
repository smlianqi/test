package com.elex.im.module.serverclient.serveraccess.module.chat.handler.flat;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.player.IPlayer;
import com.elex.common.net.message.MessageConfig;
import com.elex.im.message.flat.CommondId_Chat;
import com.elex.im.message.flat.SendChatMessageDown;
import com.elex.im.module.serverclient.serveraccess.IChatClientGameHandler;
import com.elex.im.module.serverclient.serveraccess.service.IFlatReqCallBack;
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
			IFlatReqCallBack cb = (IFlatReqCallBack) callBack;
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
		messageConfig.setKey(CommondId_Chat.SendChatMessageDown);
		messageConfig.setMessage(SendChatMessageDown.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}