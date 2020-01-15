package com.elex.im.module.serverclient.serveraccess.module.chat.handler.proto;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.player.IPlayer;
import com.elex.common.net.message.MessageConfig;
import com.elex.im.message.proto.ChatMessage.TranslationMessageDown;
import com.elex.im.module.serverclient.serveraccess.IChatClientGameHandler;
import com.elex.im.module.serverclient.serveraccess.service.IProtoReqCallBack;
import com.elex.im.module.serverclient.serveraccess.service.IReqCallBack;

public class TranslationMessageDownHandler extends IChatClientGameHandler<TranslationMessageDown> {
	public TranslationMessageDownHandler(IReqCallBack callBack) {
		super(callBack);
	}

	@Override
	public void loginedHandler(TranslationMessageDown message, IPlayer player) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> TranslationMessageDownHandler!!! player=" + player.getUserId());
		}
		if (callBack != null) {
			IProtoReqCallBack cb = (IProtoReqCallBack) callBack;
			cb.translationMessageClassBack(player, message);
		}
	}

	@Override
	public FunctionType getFunctionType() {
		return FunctionType.chat;
	}

	public MessageConfig createMessageConfig() {
		MessageConfig messageConfig = new MessageConfig();
		messageConfig.setKey(TranslationMessageDown.MessageEnum.CommondId_VALUE);
		messageConfig.setMessage(TranslationMessageDown.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}
