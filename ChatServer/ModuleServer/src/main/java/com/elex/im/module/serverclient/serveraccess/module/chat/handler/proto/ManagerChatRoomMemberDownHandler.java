package com.elex.im.module.serverclient.serveraccess.module.chat.handler.proto;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.player.IPlayer;
import com.elex.common.net.message.MessageConfig;
import com.elex.im.message.proto.ChatMessage.ManagerChatRoomMemberDown;
import com.elex.im.module.serverclient.serveraccess.IChatClientGameHandler;
import com.elex.im.module.serverclient.serveraccess.service.IProtoReqCallBack;
import com.elex.im.module.serverclient.serveraccess.service.IReqCallBack;

public class ManagerChatRoomMemberDownHandler extends IChatClientGameHandler<ManagerChatRoomMemberDown> {
	public ManagerChatRoomMemberDownHandler(IReqCallBack callBack) {
		super(callBack);
	}

	@Override
	public void loginedHandler(ManagerChatRoomMemberDown message, IPlayer player) {
		if (logger.isDebugEnabled()) {
			logger.debug("Client ManagerChatRoomMemberDownHandler!!! ");
		}
		if (callBack != null) {
			IProtoReqCallBack cb = (IProtoReqCallBack) callBack;
			cb.managerChatRoomMemberClassBack(player, message);
		}
	}

	@Override
	public FunctionType getFunctionType() {
		return FunctionType.chat;
	}

	public MessageConfig createMessageConfig() {
		MessageConfig messageConfig = new MessageConfig();
		// messageConfig.setKey(20004);
		messageConfig.setKey(ManagerChatRoomMemberDown.MessageEnum.CommondId_VALUE);
		messageConfig.setMessage(ManagerChatRoomMemberDown.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}