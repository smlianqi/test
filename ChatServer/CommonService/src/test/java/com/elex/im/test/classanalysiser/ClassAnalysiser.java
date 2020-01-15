package com.elex.im.test.classanalysiser;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ClassAnalysiser {
	public static void main(String[] args) throws Exception {
		ClassAnalysiser test = new ClassAnalysiser();
		test.test();
	}

	private static final Map<String, Class<?>> PRIMITIVE_NAMES = new HashMap<String, Class<?>>();
	static {
		PRIMITIVE_NAMES.put("boolean", Boolean.TYPE);
		PRIMITIVE_NAMES.put("byte", Byte.TYPE);
		PRIMITIVE_NAMES.put("char", Character.TYPE);
		PRIMITIVE_NAMES.put("short", Short.TYPE);
		PRIMITIVE_NAMES.put("int", Integer.TYPE);
		PRIMITIVE_NAMES.put("long", Long.TYPE);
		PRIMITIVE_NAMES.put("float", Float.TYPE);
		PRIMITIVE_NAMES.put("double", Double.TYPE);
		PRIMITIVE_NAMES.put("void", Void.TYPE);
	}

	public void test() {
		Map<Class<?>, VariableType> map = new HashMap<>();

		for (VariableType vt : VariableType.values()) {
			map.put(vt.getCls(), vt);
		}

		Class<Food> cls = Food.class;
		for (Field field : cls.getDeclaredFields()) {
			Class<?> fieldCls = field.getType();

			Type type = field.getGenericType();

			VariableType vt = map.get(fieldCls);

			StringBuilder sb = new StringBuilder();
			sb.append(field.getName());
			sb.append(" | ");
			sb.append(fieldCls.isArray());
			sb.append(" | ");
			sb.append(fieldCls.isPrimitive());
			sb.append(" | ");
			sb.append(fieldCls.isInterface());
			sb.append(" | ");
			sb.append(fieldCls.isEnum());
			sb.append(" | ");
			sb.append(fieldCls.isMemberClass());
			sb.append(" | ");
			sb.append(fieldCls.isLocalClass());
			sb.append(" | ");
			sb.append(fieldCls.isSynthetic());
			sb.append(" | ");
			sb.append(fieldCls.isAnnotation());
			sb.append(" | ");
			sb.append(vt);
			sb.append(" | ");
			sb.append(fieldCls.getTypeName());
			sb.append(" | Type=");
			sb.append(type.getTypeName());
			sb.append(" | ");
			sb.append(fieldCls);

			System.out.println(sb.toString());
		}

		// Class<IAnimal> cls = IAnimal.class;
		//
		// System.out.println(cls.isInterface());
		//
		// Method[] methods = cls.getMethods();
		// for (Method method : methods) {
		// System.out.println(method);
		// Parameter[] parameters = method.getParameters();
		// for (Parameter parameter : parameters) {
		// System.out.println(parameter);
		// }
		// Class<?> returnType = method.getReturnType();
		// System.out.println(returnType);
		// }
	}
}
