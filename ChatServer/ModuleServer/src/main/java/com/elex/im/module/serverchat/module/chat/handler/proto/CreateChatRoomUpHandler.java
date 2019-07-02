package com.elex.im.module.serverchat.module.chat.handler.proto;

import com.elex.common.component.player.IPlayer;
import com.elex.common.net.message.MessageConfig;
import com.elex.im.message.proto.ChatMessage.CreateChatRoomUp;
import com.elex.im.module.serverchat.module.chat.ChatMService;
import com.elex.im.module.serverchat.module.chat.ChatMessageHandler;
import com.elex.im.module.serverchat.module.chat.room.request.CreateChatRoomReqContext;

/**
 * 创建聊天室处理
 * 
 * @author mausmars
 *
 */
public class CreateChatRoomUpHandler extends ChatMessageHandler<CreateChatRoomUp> {
	public CreateChatRoomUpHandler(ChatMService service) {
		super(service);
	}

	@Override
	public void loginedHandler(CreateChatRoomUp message, IPlayer player) {
		CreateChatRoomReqContext context = new CreateChatRoomReqContext(message);
		createChatRoomUpHandler(player, context);
	}

	public MessageConfig createMessageConfig() {
		MessageConfig messageConfig = new MessageConfig();
		// messageConfig.setKey(20001);
		messageConfig.setKey(CreateChatRoomUp.MessageEnum.CommondId_VALUE);
		messageConfig.setMessage(CreateChatRoomUp.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}