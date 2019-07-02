package com.elex.common.component.ignite.type;

public enum ExtraParamsType {
	RootPath("rootPath"), //
	ServiceName("serviceName"), //
	DataStorageState("isDataStorage"),//
	;
	String key;

	ExtraParamsType(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
}
