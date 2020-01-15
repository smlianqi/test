package com.elex.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class OrderListMap {
	private Object[] list;
	// 对象，索引
	private Map<Object, Integer> map;
	// 默认对象
	private Object nullObj;
	// 最后索引
	int lastIndex;

	public OrderListMap(int capacity, Object nullObj) {
		this.nullObj = nullObj;
		map = new HashMap<Object, Integer>();
		list = new Object[capacity];
		lastIndex = capacity - 1;

		for (int i = 0; i < list.length; i++) {
			list[i] = nullObj;
		}
	}

	public int getCapacity() {
		return list.length;
	}

	public boolean isContain(Object obj) {
		return map.containsKey(obj);
	}

	public Object getObject(int index) {
		if (index < 0 || index > lastIndex) {
			return null;
		}
		return list[index];
	}

	/**
	 * 内部交换, sIndex替换tindex
	 * 
	 * @param sIndex
	 * @param tindex
	 */
	public void exChange(int sIndex, int tindex) {
		// 移除索引
		map.remove(list[sIndex]);
		map.remove(list[tindex]);
		Object temp = list[tindex];
		list[tindex] = list[sIndex];
		list[sIndex] = temp;

		if (list[sIndex] != nullObj) {
			map.put(list[sIndex], sIndex);
		}
		if (list[tindex] != nullObj) {
			map.put(list[tindex], tindex);
		}
	}

	public Object setObject(int index, Object obj) {
		if (index < 0 || index > lastIndex) {
			return null;
		}
		if (map.containsKey(obj)) {
			// 包含相同的
			int i = map.get(obj);
			if (i == index) {
				// 替换当前位置
				Object oldObj = list[i];
				list[i] = obj;
				map.put(obj, i);
				return oldObj;
			}
			if (list[i].equals(obj)) {
				// 不能替换
				return null;
			}
		}
		// 不包含相同的或可以设置
		Object oldObj = list[index];
		list[index] = obj;
		if (!oldObj.equals(nullObj)) {
			// 移除
			map.remove(oldObj);
		}
		if (!obj.equals(nullObj)) {
			map.put(obj, index);
		}
		return oldObj;
	}

	// 插件可以有相同的物品
	public Object set2Object(int index, Object obj) {
		if (index < 0 || index > lastIndex) {
			return null;
		}
		// 不包含相同的或可以设置
		Object oldObj = list[index];
		list[index] = obj;
		if (!oldObj.equals(nullObj)) {
			// 移除
			map.remove(oldObj);
		}
		if (!obj.equals(nullObj)) {
			map.put(obj, index);
		}
		return oldObj;
	}

	public Object set3Object(int index, Object obj) {
		if (index < 0 || index > lastIndex) {
			return null;
		}
		// 不包含相同的或可以设置
//		if (!isEmpty(index)) {
//			return null;
//		}
		list[index] = obj;
		if (!obj.equals(nullObj)) {
			map.put(obj, index);
			return obj;
		}
		return null;
	}

	public boolean isEmpty(int index) {
		if (index < 0 || index > lastIndex) {
			return false;
		}
		Object oldObj = list[index];
		if (!oldObj.equals(nullObj)) {
			return false;
		}
		return true;

	}

	public Object removeObjectByIndex(int index) {
		if (index < 0 || index > lastIndex) {
			return null;
		}
		Object obj = list[index];
		map.remove(obj);
		list[index] = nullObj;
		return obj;
	}

	public Object removeObjectByObject(Object obj) {
		if (!map.containsKey(obj)) {
			return null;
		}
		int index = map.get(obj);
		list[index] = nullObj;
		map.remove(obj);
		return obj;
	}

	public String getListString(String separator) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.length; i++) {
			sb.append(String.valueOf(list[i]));
			if (i < lastIndex) {
				sb.append(separator);
			}
		}
		return sb.toString();
	}

	public Set<Map.Entry<Object, Integer>> getcs() {
		Set<Map.Entry<Object, Integer>> c = map.entrySet();
		return c;
	}

	public Object[] getList() {
		return list;
	}

	public Map<Object, Integer> getMap() {
		return map;
	}

	public static void main(String args[]) {
		String separator = ",";
		OrderListMap test = new OrderListMap(6, 0);
		test.set3Object(0, 0);
		test.set3Object(5, 2);
		test.set3Object(5, 3);
		test.set3Object(2, 4);
		test.set3Object(3, 5);
		test.set3Object(4, 6);
		System.out.println(test.getListString(separator) + "|" + test.getcs());
		Object isSuccess = test.setObject(0, 1);
		System.out.println(test.getListString(separator) + "|" + isSuccess + "|" + test.getcs());
		isSuccess = test.setObject(0, 4);
		System.out.println(test.getListString(separator) + "|" + isSuccess + "|" + test.getcs());
		test.removeObjectByIndex(0);
		System.out.println(test.getListString(separator) + "|" + test.getcs() + " removeObjectByIndex 0");
		test.removeObjectByObject(3);
		System.out.println(test.getListString(separator) + "|" + test.getcs() + " removeObjectByObject 3");
	}
}
