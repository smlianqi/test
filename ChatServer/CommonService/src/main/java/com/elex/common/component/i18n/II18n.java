package com.elex.common.component.i18n;

import java.util.Locale;

public interface II18n {
	/**
	 * 获取内容
	 * 
	 * @param locale
	 * @param key
	 * @return
	 */
	String getContent(Locale locale, String key);
}
