package com.elex.im.module.translation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.elex.common.net.service.httpclient.ARequestCallback;
import com.elex.common.net.service.httpclient.INetHttpClient;
import com.elex.common.util.encryption.Md5Util;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;
import com.elex.im.module.translation.type.LanguageType;

/**
 * aok 翻译
 * 
 * @author mausmars
 *
 */
public class CokTranslationService implements ITranslationService {
	protected static final ILogger logger = XLogUtil.logger();

	private final String URL = "https://translate.elexapp.com/api/v2";// "http://translate.elexapp.com/translate1.php";
	private String signKey = "8801EDFED8";
	private String chValue = "6d782d";

	private INetHttpClient netHttpClient;

	public CokTranslationService(INetHttpClient netHttpClient) {
		this.netHttpClient = netHttpClient;
	}

	public void translate(LanguageType fromLang, String content, Set<LanguageType> toLangs, ARequestCallback callback) {
		Map<String, Object> params = createParams(fromLang, content, toLangs);
		netHttpClient.doPost(URL, params, callback);
	}

	@Override
	public void translate(LanguageType fromLang, String content, LanguageType toLang, ARequestCallback callback) {
		Set<LanguageType> toLangs = new HashSet<>(0);
		toLangs.add(toLang);

		Map<String, Object> params = createParams(fromLang, content, toLangs);
		netHttpClient.doPost(URL, params, callback);
	}

	// 创建参数
	private Map<String, Object> createParams(LanguageType fromLang, String content, Set<LanguageType> toLangs) {
		if (fromLang == null) {
			fromLang = LanguageType.auto;
		}
		StringBuilder toLangSB = new StringBuilder();
		int index = 1;
		for (LanguageType toLang : toLangs) {
			toLangSB.append(toLang.getGoogleCode());
			if (index < toLangs.size()) {
				toLangSB.append(',');
			}
			index++;
		}
		// 新接口翻译 跟替代客户端的-------------
		long t = System.currentTimeMillis() / 1000;

		StringBuilder sbDig = new StringBuilder();
		sbDig.append(content).append(toLangSB.toString()).append(chValue).append(t).append(signKey);
		String signmd5 = Md5Util.md5Encode(sbDig.toString());

		Map<String, Object> params = new HashMap<>();
		params.put("sc", content);
		params.put("sf", fromLang.getGoogleCode());// 翻译用的谷歌的标准
		params.put("tf", toLangSB.toString());
		params.put("ch", chValue);
		params.put("t", t);
		params.put("sig", signmd5);
		return params;
	}
}
