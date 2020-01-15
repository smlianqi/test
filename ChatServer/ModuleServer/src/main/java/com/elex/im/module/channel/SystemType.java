package com.elex.im.module.channel;

/**
 * 系统类型
 * 
 * @author mausmars
 *
 */
public enum SystemType {
	ST_IOS(1000), // IOS
	ST_Android(2000), // 安卓
	;
	private int value;

	SystemType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
