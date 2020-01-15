package com.elex.im.module.serverclient.imitateclient.module.chat.handler.flat;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.player.IPlayer;
import com.elex.common.net.message.MessageConfig;
import com.elex.im.message.flat.ChatMessageInfo;
import com.elex.im.message.flat.ChatRoomInfo;
import com.elex.im.message.flat.ChatRoomMemberInfo;
import com.elex.im.message.flat.ChatRoomMessageInfo;
import com.elex.im.message.flat.ChatUserInfo;
import com.elex.im.message.flat.CommondId_Chat;
import com.elex.im.message.flat.GainChatMessageDown;
import com.elex.im.module.common.IGameHandler;

public class GainChatMessageDownHandler extends IGameHandler<GainChatMessageDown> {
	@Override
	public void loginedHandler(GainChatMessageDown message, IPlayer player) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> GainChatMessageDownHandler!!! player=" + player.getUserId());
		}

		for (int i = 0; i < message.chatRoomMessageLength(); i++) {
			ChatRoomMessageInfo m = message.chatRoomMessage(i);
			ChatRoomInfo chatRoomInfo = m.chatRoom();
			if (chatRoomInfo != null) {
				if (logger.isDebugEnabled()) {
					logger.debug(chatRoomInfo.toString());
				}
			}
			ChatRoomMemberInfo self = m.self();
			if (self != null) {
				if (logger.isDebugEnabled()) {
					logger.debug(self.toString());
				}
			}
			for (int j = 0; j < m.usersLength(); j++) {
				ChatUserInfo chatUser = m.users(j);
				if (logger.isDebugEnabled()) {
					logger.debug(chatUser.uid());
				}
			}

			for (int j = 0; j < m.chatMessageLength(); j++) {
				ChatMessageInfo chatMessageInfo = m.chatMessage(j);
				if (logger.isDebugEnabled()) {
					logger.debug(chatMessageInfo.toString());
				}
			}
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