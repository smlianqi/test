package com.elex.im.module.serverclient.imitateclient.module.chat.handler.flat;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.player.IPlayer;
import com.elex.common.net.message.MessageConfig;
import com.elex.im.message.flat.ChatMessageInfo;
import com.elex.im.message.flat.ChatRoomInfo;
import com.elex.im.message.flat.CommondId_Chat;
import com.elex.im.message.flat.SendChatMessageDown;
import com.elex.im.module.common.IGameHandler;

public class SendChatMessageDownHandler extends IGameHandler<SendChatMessageDown> {
	@Override
	public void loginedHandler(SendChatMessageDown message, IPlayer player) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> SendChatMessageDownHandler!!! player=" + player.getUserId());
		}
		ChatRoomInfo chatRoomInfo = message.chatRoom();
		if (chatRoomInfo != null) {
			if (logger.isDebugEnabled()) {
				logger.debug(chatRoomInfo.toString());
			}
		}

		ChatMessageInfo chatMessageInfo = message.chatMessage();
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
		messageConfig.setKey(CommondId_Chat.SendChatMessageDown);
		messageConfig.setMessage(SendChatMessageDown.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}