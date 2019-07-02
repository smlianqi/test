package com.elex.common.component.data.type;

/**
 * 表配置类型
 * 
 * @author mausmars
 * 
 */
public enum TableConfigType {
	Package(0), // 根
	Database(1), // 依赖数据库
	LocalCache(2), // 依赖本地缓存
	RemoteCache(3), // 依赖远端缓存
	CreateTable(4), // 创建表
	Prefix(5),// 是否有前缀
	;
	private int value;

	TableConfigType(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}

	public int configIndex() {
		return value - 2;
	}
}
