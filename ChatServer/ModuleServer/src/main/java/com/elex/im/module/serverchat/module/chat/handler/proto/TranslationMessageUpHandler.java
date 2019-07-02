package com.elex.im.module.serverchat.module.chat.handler.proto;

import com.elex.common.component.player.IPlayer;
import com.elex.common.net.message.MessageConfig;
import com.elex.im.message.proto.ChatMessage.TranslationMessageUp;
import com.elex.im.module.serverchat.module.chat.ChatMService;
import com.elex.im.module.serverchat.module.chat.ChatMessageHandler;
import com.elex.im.module.serverchat.module.chat.room.request.TranslationMessageReqContext;

public class TranslationMessageUpHandler extends ChatMessageHandler<TranslationMessageUp> {
	public TranslationMessageUpHandler(ChatMService service) {
		super(service);
	}

	@Override
	public void loginedHandler(TranslationMessageUp message, IPlayer player) {
		TranslationMessageReqContext context = new TranslationMessageReqContext(message);
		translationMessageUpHandler(player, context);
	}

	@Override
	public MessageConfig createMessageConfig() {
		MessageConfig messageConfig = new MessageConfig();
		messageConfig.setKey(TranslationMessageUp.MessageEnum.CommondId_VALUE);
		messageConfig.setMessage(TranslationMessageUp.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}

}
