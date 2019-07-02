package com.elex.common.net.service.netty.filter.tcp;

import java.util.Arrays;

import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

import io.netty.buffer.ByteBuf;

/**
 * 消息类型
 * 
 * @author mausmars
 */
public class NettyMessage {
	protected static final ILogger logger = XLogUtil.logger();

	/**
	 * 指令id 4byte
	 */
	protected int commandId;
	/**
	 * 扩展 8byte
	 */
	protected long extend;
	/**
	 * 包体总长度 4byte
	 */
	protected int packSize;
	/**
	 * 包体数据
	 */
	protected byte[] bodyByte = new byte[0];

	public NettyMessage() {
	}

	public void readFields(ByteBuf data) {
		data.skipBytes(NettyMessageType.mark.length);// 标记
		this.commandId = data.readInt();// 指令
		this.extend = data.readLong();// 指令
		this.packSize = data.readInt();// 包体长度
		this.bodyByte = new byte[packSize];
		data.readBytes(bodyByte);// 包体
	}

	public void write(ByteBuf data) {
		data.writeBytes(NettyMessageType.mark);// 标记
		data.writeInt(commandId);// 指令
		data.writeLong(extend);// 扩展
		this.packSize = bodyByte.length;
		data.writeInt(packSize);// 包体长度
		data.writeBytes(bodyByte);// 包体

		// if (logger.isDebugEnabled()) {
		// logger.debug("NettyMessage write! commandId=" + commandId + " ,packSize=" +
		// this.packSize);
		// }
	}

	public byte[] getBodyByte() {
		return bodyByte;
	}

	public void setBodyByte(byte[] bodyByte) {
		this.bodyByte = bodyByte;
	}

	public int getPackSize() {
		return packSize;
	}

	public void setPackSize(int packSize) {
		this.packSize = packSize;
	}

	public int getCommandId() {
		return commandId;
	}

	public void setCommandId(int commandId) {
		this.commandId = commandId;
	}

	public long getExtend() {
		return extend;
	}

	public void setExtend(long extend) {
		this.extend = extend;
	}

	@Override
	public String toString() {
		return "Message [body" + Arrays.toString(bodyByte) + "]";
	}
}
