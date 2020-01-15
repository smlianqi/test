package com.elex.common.component.i18n;

import java.util.Locale;

public interface II18nService {
	String getContent(I18NType type, String key);

	String getContent(I18NType type, Locale locale, String key);
}
