package com.elex.common.component.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.elex.common.service.AbstractService;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.config.IServiceConfig;

/**
 * I18nService.国际化内容管理
 * 
 * @author mausmars
 * @since 0.1
 */
public class I18nService extends AbstractService<Object> implements II18nService {
	// {国际化类型，国际化服务}
	private Map<I18NType, II18n> i18nContentMap = new HashMap<I18NType, II18n>();
	private Locale defaultLocale;

	public I18nService(IServiceConfig serviceConfig, IGlobalContext globalContext) {
		super(serviceConfig, globalContext);
	}

	@Override
	public void initService() throws Exception {
		String filePath = null;
		Locale defaultLocale = null;

		if (defaultLocale == null) {
			this.defaultLocale = Locale.ENGLISH;
		} else {
			this.defaultLocale = defaultLocale;
		}

		this.defaultLocale = Locale.ENGLISH;

		for (I18NType ct : I18NType.values()) {
			String basename = filePath + "/" + ct.getValue();
			I18n i18nContent = new I18n(basename);
			i18nContentMap.put(ct, i18nContent);
		}
	}

	@Override
	public void startupService() throws Exception {

	}

	@Override
	public void shutdownService() throws Exception {

	}

	private II18n getI18nContent(I18NType type) {
		return i18nContentMap.get(type);
	}

	public String getContent(I18NType type, String key) {
		II18n i18nContent = getI18nContent(type);
		if (i18nContent == null) {
			return "";
		}
		return i18nContent.getContent(defaultLocale, key);
	}

	public String getContent(I18NType type, Locale locale, String key) {
		II18n i18nContent = getI18nContent(type);
		if (i18nContent == null) {
			return "";
		}
		return i18nContent.getContent(locale, key);
	}

}
