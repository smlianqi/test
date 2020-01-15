package com.elex.common.util.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Json2Json {
	/**
	 * 传统json数组转换成新的
	 * 
	 * @param jsonList
	 * @return新的json
	 */
	public static List<List<String>> transform(List<String> jsonList) {
		if (jsonList.size() == 0) {
			return NullJson(jsonList);
		}
		// 装转换完的完整json
		List<List<String>> allList = new ArrayList<List<String>>();
		// 新的json格式的值部分
		List<List<String>> allValueList = new ArrayList<List<String>>();
		// 新的json的字段名部分
		List<String> fieldList = getFieldNameList(jsonList);
		allList.add(fieldList);
		allValueList = getValueList(jsonList);
		allList.addAll(allValueList);
		// 返回装有新json的list
		return allList;
	}

	private static List<List<String>> NullJson(List<String> jsonList) {
		List<List<String>> allList = new ArrayList<List<String>>();
		return allList;
	}

	/**
	 * 转换方法的辅助方法，获得所有字段名部分
	 */
	private static List<String> getFieldNameList(List<String> jsonList) {
		String firstJson = jsonList.get(0);
		firstJson = changeMark(firstJson, ',', '.');
		return normalGetFiled(firstJson);
	}

	/**
	 * 获得字段名的辅助方法 当json中没有嵌套数组时使用此方法获得字段名
	 */
	private static List<String> normalGetFiled(String firstJson) {
		List<String> fieldList = new ArrayList<String>();
		String str2 = firstJson.replace("{", "");
		String field[] = str2.split(",");
		for (int i = 0; i < field.length; i++) {
			String everyField[] = field[i].split(":");
			fieldList.add(everyField[0]);
		}
		return fieldList;
	}

	/**
	 * 转换方法的辅助方法，获得所有的值部分 先循环装有完整json串的数组，对每个单独的json中的嵌套部分的逗号换成句号
	 * 对处理完的每个单独的json，用逗号分隔，在冒号分隔，就得到了每个json的value部分 最后将句号换回成逗号 都存到list中返回
	 */
	private static List<List<String>> getValueList(List<String> jsonList) {
		List<List<String>> allValueList = new ArrayList<List<String>>();
		for (int i = 0; i < jsonList.size(); i++) {
			List<String> valueList = new ArrayList<String>();
			String vstr = jsonList.get(i);
			vstr = changeMark(vstr, ',', '.');

			String vfield[] = vstr.split(",");
			for (int j = 0; j < vfield.length; j++) {
				String everyValue[] = vfield[j].split(":");
				String value = everyValue[1];
				String trueValue = value.replace("}", "");
				trueValue = trueValue.replace('.', ',');
				valueList.add(trueValue);
			}
			allValueList.add(valueList);
		}
		return allValueList;
	}

	/**
	 * 用正则表达式后的嵌套json部分 将嵌套部分中的逗号换成特定符号
	 */
	private static String changeMark(String firstJson, char mark, char needMark) {
		Map<String, String> change = new HashMap<String, String>();
		if (firstJson.contains("[")) {
			Pattern p = Pattern.compile("\\[(.*?)\\]");
			Matcher m = p.matcher(firstJson);
			while (m.find()) {
				String s = m.group(1);
				String changeS = s.replace(mark, needMark);
				change.put(s, changeS);
			}
			for (String everyChange : change.keySet()) {
				firstJson = firstJson.replace(everyChange, change.get(everyChange));
			}
		}
		return firstJson;
	}

	/**
	 * 用正则表达式将json中1,2,3这样的字符串中的，变成.号，部分 将嵌套部分中的逗号换成特定符号
	 */
	private static String changeMarkString(String firstJson, char mark, char needMark) {
		Map<String, String> change = new HashMap<String, String>();

		Pattern p = Pattern.compile("\\:\"*,]");
		Matcher m = p.matcher(firstJson);
		while (m.find()) {
			String s = m.group(1);
//			System.out.println(s);
			// String changeS = s.replace(mark, needMark);
			// change.put(s, changeS);
		}
		// for (String everyChange : change.keySet()) {
		// firstJson = firstJson.replace(everyChange, change.get(everyChange));
		// }

		return firstJson;
	}

}
