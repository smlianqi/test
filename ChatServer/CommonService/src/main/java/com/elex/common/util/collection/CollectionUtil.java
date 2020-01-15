package com.elex.common.util.collection;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CollectionUtil {
	public static <T, U> List<U> convert(List<T> from, Function<T, U> fun) {
		return from.stream().map(fun).collect(Collectors.toList());
	}

	public static void modifyMapValue(int key, double value, Map<Integer, Double> map, boolean isIncrease) {
		Double v = map.get(key);
		if (v == null) {
			v = (double) value;
		} else {
			if (isIncrease) {
				v += value;
			} else {
				v -= value;
			}
		}
		map.put(key, v);
	}
}
