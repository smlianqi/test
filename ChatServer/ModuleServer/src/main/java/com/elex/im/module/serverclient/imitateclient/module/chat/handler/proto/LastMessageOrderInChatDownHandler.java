package com.elex.im.module.serverclient.imitateclient.module.chat.handler.proto;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.player.IPlayer;
import com.elex.common.net.message.MessageConfig;
import com.elex.im.message.proto.ChatMessage.LastMessageOrderInChatDown;
import com.elex.im.module.common.IGameHandler;

/**
 * Created by Administrator on 2018/5/24.
 */
public class LastMessageOrderInChatDownHandler extends IGameHandler<LastMessageOrderInChatDown> {
    @Override
    public void loginedHandler(LastMessageOrderInChatDown message, IPlayer player) {
        if (logger.isDebugEnabled()) {
            logger.debug(">>> LastMessageOrderInChatDownHandler!!! player=" + player.getUserId());
        }
    }

    @Override
    public FunctionType getFunctionType() {
        return FunctionType.chat;
    }

    public MessageConfig createMessageConfig() {
        MessageConfig messageConfig = new MessageConfig();
        // messageConfig.setKey(20030);
        messageConfig.setKey(LastMessageOrderInChatDown.MessageEnum.CommondId_VALUE);
        messageConfig.setMessage(LastMessageOrderInChatDown.class);
        messageConfig.setMessageHandler(this);
        return messageConfig;
    }
}
