package com.elex.im.test.classanalysiser;

import java.util.List;
import java.util.Map;
import java.util.Set;

public enum VariableType {
	// 基础
	byte_base(byte.class), //
	short_base(short.class), //
	int_base(int.class), //
	long_base(long.class), //
	float_base(float.class), //
	double_base(double.class), //
	boolean_base(boolean.class), //
	void_base(void.class), //

	byte_obj(Byte.TYPE), //
	short_obj(Short.TYPE), //
	int_obj(Integer.TYPE), //
	long_obj(Long.TYPE), //
	float_obj(Float.TYPE), //
	double_obj(Double.TYPE), //
	boolean_obj(Boolean.TYPE), //
	void_obj(Void.TYPE), //

	// 字符串
	String_obj(String.class), //
	Character_obj(Character.class), //

	// 对象
	Object_obj(Object.class), //

	// 枚举类型
	Enum_obj(Enum.class), //

	// 集合
	List_obj(List.class), //
	Set_obj(Set.class), //
	Map_obj(Map.class),//
	;

	Class<?> cls;

	VariableType(Class<?> cls) {
		this.cls = cls;
	}

	public Class<?> getCls() {
		return cls;
	}
}
