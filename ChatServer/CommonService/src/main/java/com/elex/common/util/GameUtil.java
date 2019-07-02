package com.elex.common.util;

import java.util.List;

import com.elex.common.util.idcreate.IdChange;

public class GameUtil {
	public static boolean positionState(int value, int position) {
		return (value >> position & 1) == 1;
	}

	public static int setPositionState(int value, int position) {
		int v = 1 << position;
		return value | v;
	}

	//TODO
//	private static IdChange idChange = new IdChange(113123123);
	private static IdChange idChange = new IdChange(1);

	/**
	 * 
	 * @param key
	 * @return
	 */
	public static String id2AccountKey(long id) {
		return idChange.id2AccountKey(id);
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public static long accountKey2Id(String key) {
		return idChange.accountKey2Id(key);
	}

	/**
	 * 得到剩余时间
	 * 
	 * @param overTime
	 * @return
	 */
	public static int getRemainderTime(long overTime) {
		if (overTime <= 0) {
			// 如果等于0，证明没训练
			return 0;
		}
		long remainderTime = overTime - System.currentTimeMillis();
		return remainderTime < 0 ? -1 : (int) remainderTime;
	}

	/**
	 * 集合中去最大值
	 * 
	 * @param maxIdList
	 * @return
	 */
	public static long getMaxValue(List<Long> maxIdList) {
		long maxId = 0;
		if (maxIdList == null || maxIdList.isEmpty()) {
			return maxId;
		}

		for (Long v : maxIdList) {
			if (v == null) {
				continue;
			}
			if (v > maxId) {
				maxId = v;
			}
		}
		return maxId;
	}
	
	/**
	 * 集合中去最大值
	 * 
	 * @param maxIdList
	 * @return
	 */
	public static int getMaxIntValue(List<Integer> maxIdList) {
		int maxId = 0;
		if (maxIdList == null || maxIdList.isEmpty()) {
			return maxId;
		}

		for (Integer v : maxIdList) {
			if (v == null) {
				continue;
			}
			if (v > maxId) {
				maxId = v;
			}
		}
		return maxId;
	}

	/**
	 * 得到中间值
	 * 
	 * @param min
	 * @param count
	 * @param max
	 * @return
	 */
	public static long getAmongValue(long min, long count, long max) {
		if (count < min) {
			return min;
		}
		if (count > max) {
			return max;
		}
		return count;
	}
	
	
	public static void main(String[] args) {
		GameUtil.getAmongValue(0, 100, 222);
	}
}
