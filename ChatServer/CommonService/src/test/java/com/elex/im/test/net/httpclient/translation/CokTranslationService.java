package com.elex.im.test.net.httpclient.translation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.elex.common.net.service.httpclient.ARequestCallback;
import com.elex.common.net.service.httpclient.INetHttpClient;
import com.elex.common.util.encryption.Md5Util;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

/**
 * aok 翻译
 * 
 * @author mausmars
 *
 */
public class CokTranslationService {
	protected static final ILogger logger = XLogUtil.logger();

	private final String URL = "https://translate.elexapp.com/api/v2";// "http://translate.elexapp.com/translate1.php";
	private String signKey = "8801EDFED8";
	private String chValue = "6d782d";

	private INetHttpClient netHttpClient;

	public CokTranslationService(INetHttpClient netHttpClient) {
		this.netHttpClient = netHttpClient;
	}

	public void translate(String fromLang, String content, List<String> toLangs, ARequestCallback callback) {
		Map<String, Object> params = createParams(fromLang, content, toLangs);
		netHttpClient.doPost(URL, params, callback);
	}
	// public TranslationResult translate(String content, String toLang, int
	// timeout) {
	// TranslationResult ret = new TranslationResult();
	// ret.setOriginalContent(content);
	// ret.setTranslatedContent(content);
	// List<String> toLangs = new ArrayList<>(1);
	// toLangs.add(toLang);
	// Map<String, String> result = translate("auto", content, toLangs, timeout);
	// if (result != null) {
	// ret.setTranslatedContent(result.get(toLang));
	// }
	// if (result.containsKey("sf")) {
	// // sf 字段保存了翻译接口检测到的原语言
	// ret.setDetectedLanguage(result.get("sf"));
	// }
	// return ret;
	// }

	// public String translate(String fromLang, String toLang, String content) {
	// return translate(fromLang, toLang, content, 0);
	// }

	/**
	 * 翻译到指定的语言
	 * 
	 * @param fromLang
	 *            原语言，如果需要自动检测，写成auto
	 * @param toLang
	 * @param content
	 * @param timeout
	 *            读超时时间
	 * @return
	 */
	// public String translate(String fromLang, String toLang, String content, int
	// timeout) {
	// List<String> toLangs = new ArrayList<>(1);
	// toLangs.add(toLang);
	// Map<String, String> result = translate(fromLang, content, toLangs, timeout);
	// if (result != null) {
	// return result.get("translateMsg");
	// }
	// return content;
	// }

	// public Map<String, String> translate(String fromLang, String content,
	// List<String> toLang) {
	// return translate(fromLang, content, toLang, 0);
	// }

	// 创建参数
	private Map<String, Object> createParams(String fromLang, String content, List<String> toLangs) {
		if (StringUtils.isEmpty(fromLang)) {
			fromLang = "auto";
		}
		StringBuilder toLangSB = new StringBuilder();
		int index = 1;
		for (String toLang : toLangs) {
			toLangSB.append(toLang);
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
		params.put("sf", fromLang);
		params.put("tf", toLangSB.toString());
		params.put("ch", chValue);
		params.put("t", t);
		params.put("sig", signmd5);
		return params;
	}
}
