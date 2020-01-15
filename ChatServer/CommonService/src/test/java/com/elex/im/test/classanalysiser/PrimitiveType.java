package com.elex.im.test.classanalysiser;

/**
 * 原始类型
 * 
 * @author mausmars
 *
 */
public enum PrimitiveType {
	boolean_obj(Boolean.class), //
	byte_obj(Byte.class), //
	short_obj(Short.class), //
	int_obj(Integer.class), //
	long_obj(Long.class), //
	float_obj(Float.class), //
	double_obj(Double.class), //
	void_obj(Void.class), //
	;
	Class<?> cls;

	PrimitiveType(Class<?> cls) {
		this.cls = cls;
	}

	public Class<?> getCls() {
		return cls;
	}
}
