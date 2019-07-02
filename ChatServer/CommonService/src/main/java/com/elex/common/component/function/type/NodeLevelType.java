package com.elex.common.component.function.type;

/**
 * 节点层类型枚举
 * 
 * @author mausmars
 * 
 */
public enum NodeLevelType {
	Module(0), // 模块（sms，lock）
	Group(1), // 组
	Region(2), // 区
	Type(3), // 功能类型
	Id(4), // 功能id
	;
	private int value;

	NodeLevelType(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}

	public int configIndex() {
		return value - 2;
	}
}
