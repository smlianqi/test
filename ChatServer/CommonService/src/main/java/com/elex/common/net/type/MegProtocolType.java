package com.elex.common.net.type;

/**
 * 消息协议类型
 * 
 * @author mausmars
 *
 */
public enum MegProtocolType {
	proto(1), // proto
	flat(2), // flat
	bytes(3),// 直接发送二进制
	;
	int value;

	MegProtocolType(int value) {
		this.value = value;
	}

	public static MegProtocolType valueOf(int value) {
		value--;
		if (value < 0 || value > 1) {
			return null;
		}
		return MegProtocolType.values()[value];
	}

	public int getValue() {
		return value;
	}
}
