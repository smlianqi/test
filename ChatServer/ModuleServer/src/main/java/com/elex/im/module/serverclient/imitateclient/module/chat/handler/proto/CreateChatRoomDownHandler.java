package com.elex.im.module.serverclient.imitateclient.module.chat.handler.proto;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.player.IPlayer;
import com.elex.common.net.message.MessageConfig;
import com.elex.im.message.proto.ChatMessage.CreateChatRoomDown;
import com.elex.im.module.common.IGameHandler;

public class CreateChatRoomDownHandler extends IGameHandler<CreateChatRoomDown> {
	@Override
	public void loginedHandler(CreateChatRoomDown message, IPlayer player) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> CreateChatRoomDownHandler!!! player=" + player.getUserId());
		}
	}

	@Override
	public FunctionType getFunctionType() {
		return FunctionType.chat;
	}

	public MessageConfig createMessageConfig() {
		MessageConfig messageConfig = new MessageConfig();
		// messageConfig.setKey(20002);
		messageConfig.setKey(CreateChatRoomDown.MessageEnum.CommondId_VALUE);
		messageConfig.setMessage(CreateChatRoomDown.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}