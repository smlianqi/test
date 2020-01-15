package com.elex.im.module.serverchat.module.chat.room.request;

import com.elex.im.module.serverchat.module.chat.room.request.gainCondition.AllNewestCondition;
import com.elex.im.module.serverchat.module.chat.room.request.gainCondition.IGainCondition;
import com.elex.im.module.serverchat.module.chat.room.request.gainCondition.RoomMultiCondition;
import com.elex.im.module.serverchat.module.chat.room.request.gainCondition.RoomNewestCondition;
import com.elex.im.module.serverchat.module.chat.room.request.gainCondition.RoomPageCondition;
import com.elex.im.module.serverchat.module.chat.type.GainType;

public class GainChatMessageReqContext {
	private String uid;
	private long clientSendedTime;
	private GainType gainType;

	private IGainCondition condition;

	// ------------------------------
	public GainChatMessageReqContext() {
	}

	public GainChatMessageReqContext(com.elex.im.message.proto.ChatMessage.GainChatMessageUp message) {
		this.uid = message.getUid();
		this.clientSendedTime = message.getSendedTime();
		this.gainType = GainType.valueOf(message.getGainType());

		switch (gainType) {
		case AllNewest: {
			this.condition = new AllNewestCondition(message.getAllNewest());
			break;
		}
		case RoomMulti: {
			this.condition = new RoomMultiCondition(message.getRoomMulti());
			break;
		}
		case RoomPage: {
			this.condition = new RoomPageCondition(message.getRoomPage());
			break;
		}
		case RoomNewest: {
			this.condition = new RoomNewestCondition(message.getRoomNewest());
			break;
		}
		default:
			break;
		}
	}

	public GainChatMessageReqContext(com.elex.im.message.flat.GainChatMessageUp message) {
		this.uid = message.uid();
		this.clientSendedTime = message.sendedTime();
		this.gainType = GainType.valueOf((int) message.gainType());

		switch (gainType) {
		case AllNewest: {
			this.condition = new AllNewestCondition(message.allNewest());
			break;
		}
		case RoomMulti: {
			this.condition = new RoomMultiCondition(message.roomMulti());
			break;
		}
		case RoomPage: {
			this.condition = new RoomPageCondition(message.roomPage());
			break;
		}
		case RoomNewest: {
			this.condition = new RoomNewestCondition(message.roomNewest());
			break;
		}
		default:
			break;
		}
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public long getClientSendedTime() {
		return clientSendedTime;
	}

	public void setClientSendedTime(long clientSendedTime) {
		this.clientSendedTime = clientSendedTime;
	}

	public GainType getGainType() {
		return gainType;
	}

	public void setGainType(GainType gainType) {
		this.gainType = gainType;
	}

	public <T extends IGainCondition> T getCondition() {
		return (T) condition;
	}

	public void setCondition(IGainCondition condition) {
		this.condition = condition;
	}
}
