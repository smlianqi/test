package com.elex.common.util.netaddress;

public enum NetAddressTypeEnum {
	LoopbackAddress(1), // 回环地址
	LoacalAddress(2), // 本机地址
	OutsideAddress(3), // 外网地址
	;

	private int value;

	NetAddressTypeEnum(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}
}
