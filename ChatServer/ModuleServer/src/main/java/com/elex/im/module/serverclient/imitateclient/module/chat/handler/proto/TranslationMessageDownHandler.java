package com.elex.im.module.serverclient.imitateclient.module.chat.handler.proto;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.player.IPlayer;
import com.elex.common.net.message.MessageConfig;
import com.elex.im.message.proto.ChatMessage.TranslationMessageDown;
import com.elex.im.module.common.IGameHandler;

public class TranslationMessageDownHandler extends IGameHandler<TranslationMessageDown> {
	@Override
	public void loginedHandler(TranslationMessageDown message, IPlayer player) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> TranslationMessageDownHandler!!! player=" + player.getUserId());
		}
	}

	@Override
	public FunctionType getFunctionType() {
		return FunctionType.chat;
	}

	public MessageConfig createMessageConfig() {
		MessageConfig messageConfig = new MessageConfig();
		// messageConfig.setKey(20024);
		messageConfig.setKey(TranslationMessageDown.MessageEnum.CommondId_VALUE);
		messageConfig.setMessage(TranslationMessageDown.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}
