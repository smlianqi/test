package com.elex.common.util.json;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonUtil {
	protected static final ILogger logger = XLogUtil.logger();

	private static Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

	public static String obj2String(Object obj) {
		JSONObject jsonObject = JSONObject.fromObject(obj);
		return jsonObject.toString();
	}

	public static String gsonObj2String(Object obj) {
		return gson.toJson(obj);
	}

	public static <T> T gsonString2Obj(String str, Type type) {
		return gson.fromJson(str, type);
	}

	public static <T> T gsonString2Obj(String str, Class<T> classOfT) {
		return gson.fromJson(str, classOfT);
	}

	public static Gson getGson() {
		return gson;
	}

	/**
	 * 数组（list）到字符
	 * 
	 * @param jsonString
	 * @return
	 */
	public static String arrayObj2String(Object obj) {
		JSONArray jsonObject = JSONArray.fromObject(obj);
		return jsonObject.toString();
	}

	/**
	 * 字符到list
	 * 
	 * @param jsonString
	 * @return
	 */
	public static List string2ArrayObj(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		return (List) JSONArray.toCollection(jsonArray);
	}

	public static <T extends Object> T[] string2ArrayObj(String jsonString, Class<T> cls) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		T[] objects = (T[]) JSONArray.toArray(jsonArray, cls);
		return objects;
	}

	public static <T> T string2Object(String jsonString, Class<T> clazz) {
		JSONObject jsonObject = null;
		try {
			jsonObject = JSONObject.fromObject(jsonString);
		} catch (Exception e) {
			logger.error("", e);
		}
		return (T) JSONObject.toBean(jsonObject, clazz);
	}

	@SuppressWarnings("unchecked")
	public static <T> T transferJsonTOJavaBean(String jsonStr, Class<T> classz) {
		// FastJSON实现
		return JSON.parseObject(jsonStr, classz);
	}

	public static String transferJavaBeanToJson(Object obj) {
		// FastJSON实现
		return JSON.toJSONString(obj, SerializerFeature.WriteDateUseDateFormat);
	}

	public static String transferJavaBeanToJson(Object obj, String formate) {
		// FastJSON实现
		return JSON.toJSONString(obj, SerializerFeature.DisableCheckSpecialChar);
	}

	public static String transferListToJson(List list) {
		String ret = "";
		for (Object obj : list) {
			ret += transferJavaBeanToJson(obj) + ";;";
		}
		return ret;
	}

	public static <T> List<T> transferJsonToList(String str, Class<T> classz) {
		List<T> list = new ArrayList<T>();
		if (str != null && !str.equals("")) {
			String[] splitStr = str.split(";;");
			for (String s : splitStr) {
				list.add(transferJsonTOJavaBean(s, classz));
			}
		}
		return list;
	}

	public static void main(String args[]) {
		test2();
	}

	private static void test2() {
		String str = "[{\"test1\":\"1\",\"test2\":11},{\"test1\":\"2\",\"test2\":22}]";
		List<Map<String, Object>> tests = gsonString2Obj(str, new TypeToken<List<Map<String, Object>>>() {
		}.getType());
		for (Map<String, Object> test : tests) {
			System.out.println(test.get("test1"));
			System.out.println(test.get("test2"));
		}
	}

	private static void test1() {
		List<Map<String, List<Integer>>> test1 = new ArrayList<Map<String, List<Integer>>>();

		Map<String, List<Integer>> map1 = new HashMap<String, List<Integer>>();
		List<Integer> list11 = new ArrayList<Integer>();
		list11.add(11);

		List<Integer> list12 = new ArrayList<Integer>();
		list12.add(12);
		map1.put("110", list11);
		map1.put("120", list12);
		test1.add(map1);

		Map<String, List<Integer>> map2 = new HashMap<String, List<Integer>>();
		List<Integer> list21 = new ArrayList<Integer>();
		list21.add(21);

		List<Integer> list22 = new ArrayList<Integer>();
		list22.add(22);
		map2.put("210", list21);
		map2.put("220", list22);
		test1.add(map2);

		String str = JsonUtil.gsonObj2String(test1);
		System.out.println(str);

		List<Map<String, List<Integer>>> test2 = JsonUtil.transferJsonTOJavaBean(str, List.class);
		System.out.println(test2);
	}
}
