package com.elex.im.module.servertest.test.classloadertest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.elex.common.util.string.StringUtil;

/**
 * 测试初始化proto消息
 * 
 * @author mausmars
 *
 */
public class TestInitProtoMessage {
	public static void main(String[] args) throws Exception {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		// Class<?> clazz = SoldierImmediateUpgradeUp.class;

		Map<Class, Class> classMap = new HashMap<Class, Class>();
		classMap.put(Integer.class, int.class);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("count", 1);
		map.put("soldierType", 1);

		String clazzName = "message.SoldierMessage$SoldierImmediateUpgradeUp";
		Class<?> clazz = Class.forName(clazzName, true, classLoader);

		Map<String, Object> paramsList = new HashMap<String, Object>();
		Method method = clazz.getDeclaredMethod("newBuilder", null);
		com.google.protobuf.GeneratedMessage.Builder<?> builder = (com.google.protobuf.GeneratedMessage.Builder<?>) method
				.invoke(null, null);

		for (Entry<String, Object> entry : map.entrySet()) {
			String str = StringUtil.upperFirstString(entry.getKey());
			Object obj = entry.getValue();

			Class paramsClass = obj.getClass();
			if (classMap.containsKey(obj.getClass())) {
				paramsClass = classMap.get(paramsClass);
			}
			Method setMethod = builder.getClass().getMethod("set" + str, paramsClass);
			setMethod.invoke(builder, obj);
		}
		System.out.println(builder);

		Method buildMethod = builder.getClass().getMethod("build", null);
		com.google.protobuf.GeneratedMessage message = (com.google.protobuf.GeneratedMessage) buildMethod
				.invoke(builder, null);

		System.out.println(message);
	}
}
