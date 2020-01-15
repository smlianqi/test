package com.elex.im.module.serverclient.imitateclient.module.chat.handler.proto;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.player.IPlayer;
import com.elex.common.net.message.MessageConfig;
import com.elex.im.message.proto.ChatMessage.ChatMessageInfo;
import com.elex.im.message.proto.ChatMessage.ChatRoomInfo;
import com.elex.im.message.proto.ChatMessage.SendChatMessageDown;
import com.elex.im.module.common.IGameHandler;

public class SendChatMessageDownHandler extends IGameHandler<SendChatMessageDown> {
	@Override
	public void loginedHandler(SendChatMessageDown message, IPlayer player) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> SendChatMessageDownHandler!!! player=" + player.getUserId());
		}
		ChatRoomInfo chatRoomInfo = message.getChatRoom();
		if (chatRoomInfo != null) {
			if (logger.isDebugEnabled()) {
				logger.debug(chatRoomInfo.toString());
			}
		}

		ChatMessageInfo chatMessageInfo = message.getChatMessage();
		if (chatMessageInfo != null) {
			if (logger.isDebugEnabled()) {
				logger.debug(chatMessageInfo.toString());
			}
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