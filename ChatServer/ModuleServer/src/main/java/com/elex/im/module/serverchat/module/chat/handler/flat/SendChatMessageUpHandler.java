package com.elex.im.module.serverchat.module.chat.handler.flat;

import com.elex.common.component.player.IPlayer;
import com.elex.common.net.message.MessageConfig;
import com.elex.im.message.flat.CommondId_Chat;
import com.elex.im.message.flat.SendChatMessageUp;
import com.elex.im.module.serverchat.module.chat.ChatMService;
import com.elex.im.module.serverchat.module.chat.ChatMessageHandler;
import com.elex.im.module.serverchat.module.chat.room.request.SendMessageReqContext;

/**
 * 发送聊天消息处理
 * 
 * @author mausmars
 *
 */
public class SendChatMessageUpHandler extends ChatMessageHandler<SendChatMessageUp> {
	public SendChatMessageUpHandler(ChatMService service) {
		super(service);
	}

	@Override
	public void loginedHandler(SendChatMessageUp message, IPlayer player) {
		SendMessageReqContext context = new SendMessageReqContext(message);
		sendChatMessageUpHandler(player, context);
	}

	public MessageConfig createMessageConfig() {
		MessageConfig messageConfig = new MessageConfig();
		// messageConfig.setKey(20023);
		messageConfig.setKey(CommondId_Chat.SendChatMessageUp);
		messageConfig.setMessage(SendChatMessageUp.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}