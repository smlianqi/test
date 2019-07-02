package com.elex.common.component.data.cache;

/**
 * CacheKey类型
 * 
 * @author mausmars
 *
 */
public enum CacheKeyType {
	CK_Main(1, "main"), // 主key
	CK_Single(2, "single"), // 单key
	CK_Multi(3, "multi"), // 多key
	CK_Complex(4, "complex"), // 复杂key
	;

	int value;
	String suffix;

	CacheKeyType(int value, String suffix) {
		this.value = value;
		this.suffix = suffix;
	}

	public int value() {
		return value;
	}

	public String suffix() {
		return suffix;
	}
}
