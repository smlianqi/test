package com.elex.im.module.servertest.test.ignite_mysql.chatmessage;

import java.util.Map;
import java.util.Set;

import com.elex.common.component.data.ISpread;
import com.elex.common.util.json.JsonUtil;

/**
 * 聊天消息
 * 
 * @author mausmars
 *
 */
public class ChatMessage implements ISpread {
	/** 房间id(全服固定id，用户组合id，工会id) */
	// @QuerySqlField(index = true, orderedGroups = { @QuerySqlField.Group(name =
	// "roomId_orderId_idx", order = 0) })
	private String roomId;

	/** 房间消息序列 */
	// @QuerySqlField(index = true, descending = true, orderedGroups = {
	// @QuerySqlField.Group(name = "roomId_orderId_idx", order = 1, descending =
	// true) })
	private long orderId;

	/** 所属用户 */
	private String uid;

	/** 消息类型 */
	private int contentType;

	/** 消息内容 */
	private String content;

	/** AT的用户id，可以为空 */
	private String atUids;
	private transient Set<String> atUidsSet;

	/** 客户端发送消息时的时间（客户端生成） */
	private long sendedTime;

	/** 服务端接收到消息时的时间 （服务端生成） */
	private long receivedTime;

	/** 客户端扩展字段 */
	private String clientExt;

	/** 服务端扩展字段 */
	private String serverExt;

	public ChatMessage() {
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAtUids() {
		return atUids;
	}

	public void setAtUids(String atUids) {
		this.atUids = atUids;
	}

	public Set<String> getAtUidsSet() {
		return atUidsSet;
	}

	public void setAtUidsSet(Set<String> atUidsSet) {
		this.atUidsSet = atUidsSet;
	}

	public long getSendedTime() {
		return sendedTime;
	}

	public void setSendedTime(long sendedTime) {
		this.sendedTime = sendedTime;
	}

	public long getReceivedTime() {
		return receivedTime;
	}

	public void setReceivedTime(long receivedTime) {
		this.receivedTime = receivedTime;
	}

	public int getContentType() {
		return contentType;
	}

	public void setContentType(int contentType) {
		this.contentType = contentType;
	}

	@Override
	public void obtainAfter() {
		atUidsSet = JsonUtil.gsonString2Obj(atUids, Set.class);
	}

	@Override
	public void saveBefore() {
		atUids = JsonUtil.gsonObj2String(atUidsSet);
	}

	@Override
	public void saveAfter() {

	}

	@Override
	public <T> T cloneEntity(boolean isSaveBefore) {
		return null;
	}

	@Override
	public Map<String, Object> getIndexChangeBefore() {
		return null;
	}

	public String getClientExt() {
		return clientExt;
	}

	public void setClientExt(String clientExt) {
		this.clientExt = clientExt;
	}

	public String getServerExt() {
		return serverExt;
	}

	public void setServerExt(String serverExt) {
		this.serverExt = serverExt;
	}

	@Override
	public String toString() {
		return "ChatMessage [roomId=" + roomId + ", orderId=" + orderId + ", uid=" + uid + ", contentType="
				+ contentType + ", content=" + content + ", atUids=" + atUids + ", sendedTime=" + sendedTime
				+ ", receivedTime=" + receivedTime + ", clientExt=" + clientExt + ", serverExt=" + serverExt + "]";
	}
}
