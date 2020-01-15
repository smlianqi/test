package com.elex.im.module.serverclient.serveraccess.module.chat.handler.proto;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.player.IPlayer;
import com.elex.common.net.message.MessageConfig;
import com.elex.im.message.proto.ChatMessage.CreateChatRoomDown;
import com.elex.im.module.serverclient.serveraccess.IChatClientGameHandler;
import com.elex.im.module.serverclient.serveraccess.service.IProtoReqCallBack;
import com.elex.im.module.serverclient.serveraccess.service.IReqCallBack;

public class CreateChatRoomDownHandler extends IChatClientGameHandler<CreateChatRoomDown> {
	public CreateChatRoomDownHandler(IReqCallBack callBack) {
		super(callBack);
	}

	@Override
	public void loginedHandler(CreateChatRoomDown message, IPlayer player) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> CreateChatRoomDownHandler!!! ");
		}
		if (callBack != null) {
			IProtoReqCallBack cb = (IProtoReqCallBack) callBack;
			cb.createChatRoomClassBack(player, message);
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