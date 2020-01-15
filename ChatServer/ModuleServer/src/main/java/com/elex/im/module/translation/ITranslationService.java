package com.elex.im.module.translation;

import java.util.Set;

import com.elex.common.net.service.httpclient.ARequestCallback;
import com.elex.im.module.translation.type.LanguageType;

/**
 * 翻译服务
 * 
 * @author mausmars
 *
 */
public interface ITranslationService {
	void translate(LanguageType fromLang, String content, Set<LanguageType> toLangs, ARequestCallback callback);

	void translate(LanguageType fromLang, String content, LanguageType toLangs, ARequestCallback callback);
}
