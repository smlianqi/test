package com.elex.common.util.random;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RandomUtil {
	private static Random random = new Random();
	// 扩大倍数
	private static int Multiple = 1000000;
	// 百分比
	public final static int Percent = 100;

	public static boolean isWinning(double n) {
		int value = random.nextInt(Multiple);
		return value < n * Multiple;
	}

	/**
	 * 按比例随机
	 * 
	 * @param rates
	 * @return
	 */
	public static int getRatioRandom(List<Integer> rates) {
		int total = 0;
		for (int rate : rates) {
			total += rate;
		}
		if (total <= 0) {
			return -1;
		}
		int n = random.nextInt(total);

		total = -1;
		int index = -1;
		for (int rate : rates) {
			total += rate;
			index++;
			if (n <= total) {
				return index;
			}
		}
		return index;
	}

	/**
	 * 顺序随机(随机一个N以内的数) List<Integer> rates={1,10，100}则N为100，第几次随机出来的值满足条件则返回当前下标
	 * 
	 * @param rates
	 * @return
	 */
	public static int getOrderRandom(List<Integer> rates, int n) {
		int index = -1;

		int value = random.nextInt(n);
		for (int rate : rates) {
			index++;
			if (value <= rate) {
				return index;
			}
		}
		return index;
	}

	/**
	 * 顺序随机
	 * 
	 * @param rates
	 * @return
	 */
	public static int getOrderRandom(List<Integer> rates, int n, Random random) {
		int index = -1;

		int value = random.nextInt(n);
		for (int rate : rates) {
			index++;
			if (value <= rate) {
				return index;
			}
		}
		return index;
	}

	/**
	 * 成功几率随机
	 * 
	 * @param rates
	 * @return
	 */
	public static boolean getSuccessRandom(int ratios, int n) {
		if (ratios <= 0) {
			return false;
		}
		if (ratios >= n) {
			return true;
		}
		int value = random.nextInt(n);
		if (value <= ratios) {
			return true;
		}
		return false;
	}

	/**
	 * 成功几率随机(带随机数种子)
	 * 
	 * @param rates
	 * @return
	 */
	public static boolean getSuccessRandom(Random r, int ratios, int n) {
		if (ratios <= 0) {
			return false;
		}
		if (ratios >= n) {
			return true;
		}
		int value = r.nextInt(n);
		if (value <= ratios) {
			return true;
		}
		return false;
	}

	/**
	 * 区间随机 [start,end) 取count个
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static Set<Integer> regionRandom(int start, int end, int count, int max) {
		if (start < 0 || end < 0 || start >= end) {
			return null;
		}

		Set<Integer> randomNum = new HashSet<Integer>();
		int dif = end - start;
		if (dif <= count) {
			// 如果总的数量 取的数量小于
			for (int i = start; i < end; i++) {
				randomNum.add(i);
			}
			return randomNum;
		}
		int value = count * 3;
		if (dif <= value) {
			List<Integer> list = new ArrayList<Integer>(value);
			for (int i = start; i < end; i++) {
				list.add(i);
			}
			list = (List<Integer>) norepeatRrandomByList(list, count);
			randomNum.addAll(list);
			return randomNum;
		}

		for (int i = 0; i < max; i++) {
			int r = random.nextInt(dif) + start;
			if (randomNum.contains(r)) {
				continue;
			}
			randomNum.add(r);
			if (randomNum.size() >= count) {
				break;
			}
		}
		return randomNum;
	}

	/**
	 * 区间随机 [start,end) 取1个(带随机数种子)
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static int regionRandom(int start, int end, Random r) {
		if (start < 0 || end < 0 || start >= end) {
			return 0;
		}
		int dif = end - start;
		int rand = r.nextInt(dif) + start;
		return rand;
	}

	/**
	 * 区间随机 [start,end) 取1个(带随机数种子)
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static int regionRandom(int start, int end) {
		if (start < 0 || end < 0 || start >= end) {
			return 0;
		}
		int dif = end - start;
		int rand = random.nextInt(dif) + start;
		return rand;
	}

	/**
	 * 区间随机 [start,end] 取1个(带随机数种子)
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static int regionRandom2(int start, int end) {
		if (start < 0 || end < 0 || start > end) {
			return 0;
		}
		if (start == end) {
			return start;
		}

		int dif = end - start + 1;
		int rand = random.nextInt(dif) + start;
		return rand;
	}

	public static int generalRrandom(int value) {
		return random.nextInt(value);
	}

	public static int generalRrandom() {
		return random.nextInt();
	}

	public static List<Integer> norepeatRrandomByRandomNum(int randomNum, int n) {
		List<Integer> clist = new ArrayList<Integer>();
		for (int i = 0; i < randomNum; i++) {
			clist.add(i);
		}
		List<Integer> chooseList = new ArrayList<Integer>(n);
		int size = clist.size();
		for (int i = 0; i < n; i++) {
			int index = random.nextInt(size);
			addChooseNum(index, size, clist, chooseList);
			size--;
		}
		return chooseList;
	}

	/**
	 * 按比例probabilityList随机n个list的内容
	 * 
	 * @param value
	 * @return
	 */
	public static List<?> norepeatRrandomByList(List<?> list, List<Integer> probabilityList, int n) {
		if (list == null || list.isEmpty() || probabilityList == null || probabilityList.isEmpty()
				|| probabilityList.size() != list.size() || n <= 0 || n > list.size()) {
			return new ArrayList<Object>();
		}

		List<Object> clist = new ArrayList<Object>();
		clist.addAll(list);
		List<Integer> cprobabilityList = new ArrayList<Integer>();
		cprobabilityList.addAll(probabilityList);

		int totalProbability = 0;
		for (int probability : probabilityList) {
			totalProbability += probability;
		}

		List<Object> chooseList = new ArrayList<Object>();
		for (int i = n; i > 0; i--) {
			int randomNum = random.nextInt(totalProbability);

			int index = getIndex(cprobabilityList, randomNum);
			if (index == -1) {
				break;
			}
			Object obj = clist.remove(index);
			totalProbability -= cprobabilityList.remove(index);
			chooseList.add(obj);
		}
		return chooseList;
	}

	public static void main(String args[]) {
		List<Integer> list = new ArrayList<Integer>();
		list.add(3);
		list.add(3);
		list.add(3);
		list.add(1);
		System.out.println(getRatioRandom(list));

	}

	private static int getIndex(List<Integer> probabilityList, int randomNum) {
		int probabilityNum = 0;
		int index = -1;

		for (int probability : probabilityList) {
			probabilityNum += probability;
			index++;
			if (probabilityNum >= randomNum) {
				return index;
			}
		}
		return index;
	}

	/**
	 * 不重复随机，取n个值
	 * 
	 * @param value
	 * @return
	 */
	public static <T> List<T> norepeatRrandomByList(List<T> list, int n) {
		if (list == null || list.isEmpty() || n <= 0) {
			return new ArrayList<T>();
		}
		if (n > list.size()) {
			return list;
		}

		List<T> clist = new ArrayList<T>();
		clist.addAll(list);
		List<T> chooseList = new ArrayList<T>(n);
		int size = clist.size();
		for (int i = 0; i < n; i++) {
			int index = random.nextInt(size);
			addChoose(index, size, clist, chooseList);
			size--;
		}
		return chooseList;
	}

	private static void addChooseNum(int index, int size, List<Integer> clist, List<Integer> chooseList) {
		int lastIndex = size - 1;
		Integer temp = clist.get(index);
		clist.set(index, clist.get(lastIndex));
		chooseList.add(temp);
	}

	private static <T> void addChoose(int index, int size, List<T> clist, List<T> chooseList) {
		int lastIndex = size - 1;
		T temp = clist.get(index);
		clist.set(index, clist.get(lastIndex));
		chooseList.add(temp);
	}
}
