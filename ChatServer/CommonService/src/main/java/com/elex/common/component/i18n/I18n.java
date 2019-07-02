package com.elex.common.component.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class I18n implements II18n {
	protected static final Logger logger = LogManager.getLogger(I18n.class);

	private Map<Locale, ResourceBundle> resourceBundleMap = new HashMap<Locale, ResourceBundle>();

	public I18n(String baseName) {
		init(baseName);
	}

	public String getContent(Locale locale, String key) {
		ResourceBundle rb = resourceBundleMap.get(locale);
		if (rb == null) {
			return "";
		}
		if (!rb.containsKey(key)) {
			logger.error("key map value is null! key=" + key);
			return "";
		}
		String s = rb.getString(key);
		if (s == null) {
			logger.error("key map value is null! key=" + key);
			return "";
		}
		return s;
	}

	// -----------------------------
	private void init(String baseName) {
		// TODO 暂时就2种语言
		addResourceBundle(baseName, Locale.CHINESE);
		addResourceBundle(baseName, Locale.ENGLISH);
	}

	private void addResourceBundle(String baseName, Locale locale) {
		ResourceBundle rb = ResourceBundle.getBundle(baseName, locale);
		if (rb != null) {
			resourceBundleMap.put(locale, rb);
		}
	}

}
