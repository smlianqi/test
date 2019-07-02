package com.elex.im.module.channel;

import java.util.HashMap;
import java.util.Map;

/**
 * 渠道类型
 * 
 * @author mausmars
 *
 */
public enum ChannelType {
	RT_Elex(1001), // 智明
	RT_Apple(1002), // 苹果

	RT_3K(2001), //
	RT_91(2002), //
	RT_AD360(2003), //
	RT_AD3G(2004), //
	RT_ADBR(2005), //
	RT_ADDK(2006), //
	RT_ADDL(2007), //
	RT_ADDXAYX(2008), //
	RT_IAPPAY(2009), //
	RT_ADMMY(2010), //
	RT_ADSanXing(2011), //
	RT_ADYYC(2012), //
	RT_ADVIVO(2013), //
	RT_ADWDJ(2014), //
	RT_ADXA(2015), //
	RT_ADDXMYD(2016), //
	RT_ADYUNDING(2017), //
	RT_ADANZHI(2018), //
	RT_DL(2019), //
	RT_HaiMa(2020), //
	RT_IAPPAY2(2021), //
	RT_IOS91(2022), //
	RT_ADJIFENG(2023), //
	RT_JY(2024), //
	RT_ADOPPO(2025), //
	RT_ADKUDONG(2026), //
	RT_KY(2027), //
	RT_ADMEIZU(2028), //
	RT_ADENDUO(2029), //
	RT_PP(2030), //
	RT_ADQVWAN(2031), //
	RT_TBT(2032), //
	RT_UC(2033), //
	RT_ADWUCAISHIKONG(2034), //
	RT_XM(2035), //
	RT_ADXIONGMAOWAN(2036), //
	RT_ADFREEYUNDING(2037), //
	RT_ADYINGYONGHUI(2038), //
	RT_WEBYUNDING(2039), //
	RT_ADYUNDINGJINLI(2040),//
	;
	private int value;

	ChannelType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	private static Map<Integer, ChannelType> enums;

	static {
		enums = new HashMap<>();
		for (ChannelType ct : ChannelType.values()) {
			enums.put(ct.value, ct);
		}
	}

	public static ChannelType getEnum(int index) {
		return enums.get(index);
	}

}
