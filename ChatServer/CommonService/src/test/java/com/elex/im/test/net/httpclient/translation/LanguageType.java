package com.elex.im.test.net.httpclient.translation;

/**
 * 语言类型
 * 
 * @author mausmars
 *
 */
public enum LanguageType {
	AUTO_DETECT(""), // 0
	ARABIC("ar"), // 1
	BULGARIAN("bg"), // 2
	CATALAN("ca"), // 3
	CHINESE_SIMPLIFIED("zh-CHS"), // 4
	CHINESE_TRADITIONAL("zh-CHT"), // 5
	CZECH("cs"), // 6
	DANISH("da"), // 7
	DUTCH("nl"), // 8
	ENGLISH("en"), // 9
	ESTONIAN("et"), // 10
	FINNISH("fi"), // 11
	FRENCH("fr"), // 12
	GERMAN("de"), // 13
	GREEK("el"), // 14
	HAITIAN_CREOLE("ht"), // 15
	HEBREW("he"), // 16
	HINDI("hi"), // 17
	HMONG_DAW("mww"), // 18
	HUNGARIAN("hu"), // 19
	INDONESIAN("id"), // 20
	ITALIAN("it"), // 21
	JAPANESE("ja"), // 22
	KOREAN("ko"), // 23
	LATVIAN("lv"), // 24
	LITHUANIAN("lt"), // 25
	MALAY("ms"), // 26
	NORWEGIAN("no"), // 27
	PERSIAN("fa"), // 28
	POLISH("pl"), // 29
	PORTUGUESE("pt"), // 30
	ROMANIAN("ro"), // 31
	RUSSIAN("ru"), // 32
	SLOVAK("sk"), // 33
	SLOVENIAN("sl"), // 34
	SPANISH("es"), // 35
	SWEDISH("sv"), // 36
	THAI("th"), // 37
	TURKISH("tr"), // 38
	UKRAINIAN("uk"), // 39
	URDU("ur"), // 40
	VIETNAMESE("vi"),// 41
	;

	String value;

	LanguageType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
