package com.elex.im.module.serverchat.module.chat.handler.proto;

import com.elex.common.component.player.IPlayer;
import com.elex.common.net.message.MessageConfig;
import com.elex.im.message.proto.ChatMessage.ManagerChatRoomMemberUp;
import com.elex.im.module.serverchat.module.chat.ChatMService;
import com.elex.im.module.serverchat.module.chat.ChatMessageHandler;
import com.elex.im.module.serverchat.module.chat.room.request.ManagerChatRoomMemberReqContext;

/**
 * 管理聊天室成员处理
 * 
 * @author mausmars
 *
 */
public class ManagerChatRoomMemberUpHandler extends ChatMessageHandler<ManagerChatRoomMemberUp> {
	public ManagerChatRoomMemberUpHandler(ChatMService service) {
		super(service);
	}

	@Override
	public void loginedHandler(ManagerChatRoomMemberUp message, IPlayer player) {
		ManagerChatRoomMemberReqContext context = new ManagerChatRoomMemberReqContext(message);
		managerChatRoomMemberUpHandler(player, context);
	}

	public MessageConfig createMessageConfig() {
		MessageConfig messageConfig = new MessageConfig();
		// messageConfig.setKey(20003);
		messageConfig.setKey(ManagerChatRoomMemberUp.MessageEnum.CommondId_VALUE);
		messageConfig.setMessage(ManagerChatRoomMemberUp.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}