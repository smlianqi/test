package com.elex.im.module.serverchat.module.chat.handler.proto;

import com.elex.common.component.player.IPlayer;
import com.elex.common.net.message.MessageConfig;
import com.elex.im.message.proto.ChatMessage.LastMessageOrderInChatUp;
import com.elex.im.module.serverchat.module.chat.ChatMService;
import com.elex.im.module.serverchat.module.chat.ChatMessageHandler;
import com.elex.im.module.serverchat.module.chat.room.request.LastMessageOrderInChatUpContext;

/**
 * Created by Administrator on 2018/5/24.
 */
public class LastMessageOrderInChatUpHandler extends ChatMessageHandler<LastMessageOrderInChatUp> {
    public LastMessageOrderInChatUpHandler(ChatMService service) {
        super(service);
    }

    @Override
    public void loginedHandler(LastMessageOrderInChatUp message, IPlayer player) {
        LastMessageOrderInChatUpContext context = new LastMessageOrderInChatUpContext(message);
        lastMessageOrderInChatUpHandler(player, context);
    }

    public MessageConfig createMessageConfig() {
        MessageConfig messageConfig = new MessageConfig();
        // messageConfig.setKey(20029);
        messageConfig.setKey(LastMessageOrderInChatUp.MessageEnum.CommondId_VALUE);
        messageConfig.setMessage(LastMessageOrderInChatUp.class);
        messageConfig.setMessageHandler(this);
        return messageConfig;
    }
}
