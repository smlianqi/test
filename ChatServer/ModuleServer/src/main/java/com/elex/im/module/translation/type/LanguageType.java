package com.elex.im.module.translation.type;

import java.util.HashMap;
import java.util.Map;

/**
 * 语言类型
 * 
 * @author mausmars
 *
 */
public enum LanguageType {
	/** 自动 */
	auto("auto", "auto", "auto"), //
	/** 中文简体 */
	zh_Hans("zh-Hans", "zh-CHS", "zh-CN"), //
	/** 中文繁体 */
	zh_Hant("zh-Hant", "zh-CHT", "zh-TW"), //
	/** 阿拉伯语 */
	ar("ar", "ar", "ar"), //
	/** 保加利亚语 */
	bg("bg", "bg", "bg"), //
	/** 加泰罗尼亚 */
	ca("ca", "ca", "ca"), //
	/** 捷克 */
	cs("cs", "cs", "cs"), //
	/** 丹麦语 */
	da("da", "da", "da"), //
	/** 荷兰语 */
	nl("nl", "nl", "nl"), //
	/** 英语 */
	en("en", "en", "en"), //
	/** 爱沙尼亚语 */
	et("et", "et", "et"), //
	/** 芬兰语 */
	fi("fi", "fi", "fi"), //
	/** 法语 */
	fr("fr", "fr", "fr"), //
	/** 德语 */
	de("de", "de", "de"), //
	/** 希腊语 */
	el("el", "el", "el"), //
	/** 海地克里奥尔语 */
	ht("ht", "ht", "ht"), //
	/** 希伯来语 */
	he("he", "he", "he"), //
	/** 印地语 */
	hi("hi", "hi", "hi"), //
	/** 匈牙利语 */
	hu("hu", "hu", "hu"), //
	/** 印度尼西亚 */
	id("id", "id", "id"), //
	/** 意大利语 */
	it("it", "it", "it"), //
	/** 日语 */
	ja("ja", "ja", "ja"), //
	/** 韩语 */
	ko("ko", "ko", "ko"), //
	/** 拉脱维亚语 */
	lv("lv", "lv", "lv"), //
	/** 立陶宛语 */
	lt("lt", "lt", "lt"), //
	/** 马来语 */
	ms("ms", "ms", "ms"), //
	/** 挪威语 */
	no("no", "no", "no"), //
	/** 波斯语 */
	fa("fa", "fa", "fa"), //
	/** 波兰 */
	pl("pl", "pl", "pl"), //
	/** 葡萄牙语 */
	pt("pt", "pt", "pt"), //
	/** 罗马尼亚语 */
	ro("ro", "ro", "ro"), //
	/** 俄语 */
	ru("ru", "ru", "ru"), //
	/** 斯洛伐克 */
	sk("sk", "sk", "sk"), //
	/** 斯洛文尼亚 */
	sl("sl", "sl", "sl"), //
	/** 西班牙语 */
	es("es", "es", "es"), //
	/** 瑞典语 */
	sv("sv", "sv", "sv"), //
	/** 泰语 */
	th("th", "th", "th"), //
	/** 土耳其语 */
	tr("tr", "tr", "tr"), //
	/** 乌克兰 */
	uk("uk", "uk", "uk"), //
	/** 乌尔都语 */
	ur("ur", "ur", "ur"), //
	/** 越南 */
	vi("vi", "vi", "vi"), //
	/** 苗族昂 */
	mww("mww", "mww", "mww"),//

	;

	String isoCode;// 标准码
	String microsoftCode;// 微软码
	String googleCode;// 谷歌码

	LanguageType(String isoCode, String microsoftCode, String googleCode) {
		this.isoCode = isoCode;
		this.microsoftCode = microsoftCode;
		this.googleCode = googleCode;
	}

	public String getIsoCode() {
		return isoCode;
	}

	public String getMicrosoftCode() {
		return microsoftCode;
	}

	public String getGoogleCode() {
		return googleCode;
	}

	static Map<String, LanguageType> map = new HashMap<>();
	static Map<String, LanguageType> googleMap = new HashMap<>();
	static {
		for (LanguageType type : LanguageType.values()) {
			map.put(type.getIsoCode(), type);
			googleMap.put(type.getGoogleCode(), type);
		}
	}

	public static LanguageType getLanguageType(String lt) {
		return map.get(lt);
	}

	public static LanguageType getLanguageTypeByGoogle(String lt) {
		return googleMap.get(lt);
	}
}
