package com.elex.common.component.i18n;

public enum I18NType {
	I18N_Message("message"), // 消息国际化
	;
	private String value;

	I18NType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
