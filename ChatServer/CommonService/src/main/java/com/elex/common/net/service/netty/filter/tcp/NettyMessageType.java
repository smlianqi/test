package com.elex.common.net.service.netty.filter.tcp;

import io.netty.buffer.ByteBuf;

/**
 * 消息类型 消息由四部分组成： |xxx |消息ID 4|扩展字段8|后续长度 4|后续内容（Protobuf） |xxx | MSG ID | BODY
 * LEN | BODY |
 * 
 * @author mausmars
 *
 */
public class NettyMessageType {
	// 标志：xxx
	public static byte[] mark = "xxx".getBytes();
	// 消息ID
	public int commandLen;
	// 扩展字段
	public int extendLen;
	// 包体长度
	public int packsizeLen;
	// Protobuf
	public int headerLen;

	public NettyMessageType() {
		this.commandLen = 4;// 字节
		this.extendLen = 8;// 字节
		this.packsizeLen = 4;// 字节
		this.headerLen = this.commandLen + this.packsizeLen + this.extendLen;
	}

	public int getMarkLength() {
		return mark.length;
	}

	public boolean checkMark(Object d) {
		ByteBuf data = (ByteBuf) d;
		// 验证指令mark
		byte[] cmark = new byte[mark.length];
		data.readBytes(cmark);
		for (int i = 0; i < mark.length; i++) {
			if (cmark[i] != mark[i]) {
				return false;
			}
		}
		return true;
	}

	public int getHeaderLen() {
		return headerLen;
	}

	public int getPackSizePosition() {
		return commandLen + extendLen;
	}

	public int getPackSizeLen() {
		return packsizeLen;
	}

	public int getCommandLen() {
		return commandLen;
	}

	public NettyMessage createPackage() {
		return new NettyMessage();
	}

	public byte[] getMark() {
		return mark;
	}
}
