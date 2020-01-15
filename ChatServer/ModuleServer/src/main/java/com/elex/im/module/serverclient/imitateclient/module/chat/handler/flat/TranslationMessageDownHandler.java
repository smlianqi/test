package com.elex.im.module.serverclient.imitateclient.module.chat.handler.flat;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.player.IPlayer;
import com.elex.common.net.message.MessageConfig;
import com.elex.im.message.flat.CommondId_Chat;
import com.elex.im.message.flat.TranslationMessageDown;
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
		messageConfig.setKey(CommondId_Chat.TranslationMessageDown);
		messageConfig.setMessage(TranslationMessageDown.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}
