/**
 * 
 */
package com.elex.common.util.idcreate;

/**
 * @author Administrator
 * 
 */
public class IdChange {
	// 数字开始的ascii码
	private static byte numStart = '0';
	private static byte numCount = 10;

	// 字母开始的ascii码
	private static byte capitalLetterStart = 'A';
	private static byte lowerLetterStart = 'a';
	private static byte letterCount = 26;

	// 除数
	private double[] divisors;
	// 最大值
	private double maxValue;

	private int maxDigit;

	public IdChange(int maxDigit) {
		if (maxDigit <= 0) {
			throw new RuntimeException();
		}
		this.divisors = new double[maxDigit];
		for (int i = 0; i < maxDigit; i++) {
			divisors[i] = Math.pow(numCount + letterCount, i);
		}
		this.maxValue = Math.pow(numCount + letterCount, maxDigit);
		this.maxDigit = maxDigit;
	}

	/**
	 * id转化到key
	 * 
	 * @param id
	 * @return
	 */
	public String id2AccountKey(long id) {
		if (id < 0 || id > maxValue) {
			// id超过取值范围
			return null;
		}

		char[] chars = new char[divisors.length];
		calculate(id, divisors.length - 1, chars);
		return new String(chars);
	}

	private void calculate(long value, int pow, char[] chars) {
		if (pow < 0) {
			return;
		}
		double divisor = divisors[pow];
		long a = (long) (value / divisor);

		char c = 0;
		if (a <= numCount - 1) {
			c = (char) (numStart + a);
		} else if (a <= letterCount + numCount) {
			c = (char) (lowerLetterStart + a - numCount);
		} else {
			throw new RuntimeException();
		}
		chars[pow] = c;
		value -= divisor * a;
		calculate(value, pow - 1, chars);
	}

	/**
	 * key转化到id
	 * 
	 * @param key
	 * @return
	 */
	public long accountKey2Id(String key) {
		if (key.length() > divisors.length) {
			return 0;
		}

		long id = 0;
		byte[] bytes = key.getBytes();
		for (int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];
			if (b >= numStart && b < numStart + numCount) {
				id += (b - numStart) * divisors[i];
			} else if (b >= capitalLetterStart && b < capitalLetterStart + letterCount) {
				id += (b - capitalLetterStart + numCount) * divisors[i];
			} else if (b >= lowerLetterStart && b < lowerLetterStart + letterCount) {
				id += (b - lowerLetterStart + numCount) * divisors[i];
			} else {
				// 如果给定的key不是数字和字母的，返回0
				return 0;
			}
		}
		return id;
	}

	public int getMaxDigit() {
		return maxDigit;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public static void main(String args[]) {
		IdChange change = new IdChange(7);

		long rid = change.accountKey2Id("00000aa");
		System.out.println("rid=" + rid);

		long testId = 10001;
		String str = change.id2AccountKey(testId);
		System.out.println("str=" + str);
		System.out.println("==============================");
		long uid = rid + testId;
		str = change.id2AccountKey(uid);
		System.out.println("str=" + str);

		long uid1 = change.accountKey2Id(str);
		System.out.println("uid1=" + uid1);
		System.out.println(uid == uid1);
	}
}
