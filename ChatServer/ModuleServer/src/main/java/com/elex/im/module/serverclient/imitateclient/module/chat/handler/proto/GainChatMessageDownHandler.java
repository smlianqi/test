package com.elex.im.module.serverclient.imitateclient.module.chat.handler.proto;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.player.IPlayer;
import com.elex.common.net.message.MessageConfig;
import com.elex.im.message.proto.ChatMessage.ChatMessageInfo;
import com.elex.im.message.proto.ChatMessage.ChatRoomInfo;
import com.elex.im.message.proto.ChatMessage.ChatRoomMemberInfo;
import com.elex.im.message.proto.ChatMessage.ChatRoomMessageInfo;
import com.elex.im.message.proto.ChatMessage.ChatUserInfo;
import com.elex.im.message.proto.ChatMessage.GainChatMessageDown;
import com.elex.im.module.common.IGameHandler;

public class GainChatMessageDownHandler extends IGameHandler<GainChatMessageDown> {
	@Override
	public void loginedHandler(GainChatMessageDown message, IPlayer player) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> GainChatMessageDownHandler!!! player=" + player.getUserId());
		}

		for (ChatRoomMessageInfo m : message.getChatRoomMessageList()) {
			ChatRoomInfo chatRoomInfo = m.getChatRoom();
			if (chatRoomInfo != null) {
				if (logger.isDebugEnabled()) {
					logger.debug(chatRoomInfo.toString());
				}
			}
			ChatRoomMemberInfo self = m.getSelf();
			if (self != null) {
				if (logger.isDebugEnabled()) {
					logger.debug(self.toString());
				}
			}
			for (ChatUserInfo chatUser : m.getUsersList()) {
				if (logger.isDebugEnabled()) {
					logger.debug(chatUser.getUid());
				}
			}

			for (ChatMessageInfo chatMessageInfo : m.getChatMessageList()) {
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
		messageConfig.setKey(GainChatMessageDown.MessageEnum.CommondId_VALUE);
		messageConfig.setMessage(GainChatMessageDown.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}