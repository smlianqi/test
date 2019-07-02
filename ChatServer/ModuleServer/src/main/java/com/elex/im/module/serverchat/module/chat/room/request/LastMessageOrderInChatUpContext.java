package com.elex.im.module.serverchat.module.chat.room.request;

import java.util.Map;

/**
 * Created by Administrator on 2018/5/24.
 */
public class LastMessageOrderInChatUpContext {

    private String userId;
    private Map<String,String> roomToMsgOrder;

    public LastMessageOrderInChatUpContext(com.elex.im.message.proto.ChatMessage.LastMessageOrderInChatUp message) {
        this.roomToMsgOrder = message.getRoomToMsgOrderMap();
        this.userId = message.getUserId();
    }

    public String getLastOrderInRoom(String roomId){
        if(this.roomToMsgOrder == null){
            return null;
        }
        return this.roomToMsgOrder.get(roomId);
    }

    public Map<String,String> getRoomToMsgOrder(){
        return  this.roomToMsgOrder;
    }
    public String getUserId(){
        return this.userId;
    }
}
