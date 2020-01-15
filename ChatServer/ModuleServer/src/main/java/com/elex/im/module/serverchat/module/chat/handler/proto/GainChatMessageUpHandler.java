package com.elex.im.module.serverchat.module.chat.handler.proto;

import com.elex.common.component.player.IPlayer;
import com.elex.common.net.message.MessageConfig;
import com.elex.im.message.proto.ChatMessage.GainChatMessageUp;
import com.elex.im.module.serverchat.module.chat.ChatMService;
import com.elex.im.module.serverchat.module.chat.ChatMessageHandler;
import com.elex.im.module.serverchat.module.chat.room.request.GainChatMessageReqContext;

/**
 * 获取聊天消息处理
 * 
 * @author mausmars
 *
 */
public class GainChatMessageUpHandler extends ChatMessageHandler<GainChatMessageUp> {
	public GainChatMessageUpHandler(ChatMService service) {
		super(service);
	}

	@Override
	public void loginedHandler(GainChatMessageUp message, IPlayer player) {
		GainChatMessageReqContext context = new GainChatMessageReqContext(message);
		gainChatMessageUpHandler(player, context);
	}

	public MessageConfig createMessageConfig() {
		MessageConfig messageConfig = new MessageConfig();
		// messageConfig.setKey(20021);
		messageConfig.setKey(GainChatMessageUp.MessageEnum.CommondId_VALUE);
		messageConfig.setMessage(GainChatMessageUp.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}