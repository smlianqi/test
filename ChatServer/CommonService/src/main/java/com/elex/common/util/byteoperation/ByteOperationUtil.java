package com.elex.common.util.byteoperation;

public class ByteOperationUtil {
	public static int setIntNByte(int a, int index) {
		if (index < 0 || index > 32) {
			throw new RuntimeException();
		}
		return a |= (1 << index);
	}

	public static boolean checkIntNByte(int a, int index) {
		if (index < 0 || index > 32) {
			throw new RuntimeException();
		}
		int t = 1 << index;
		return (a & t) == t;
	}

	public static void main(String args[]) {
		int a = 0;
		a = setIntNByte(a, 3);
		System.out.println(a);
		a = setIntNByte(a, 1);
		System.out.println(a);
		a = setIntNByte(a, 0);
		System.out.println(a);
		a = setIntNByte(a, 3);
		System.out.println(a);
		System.out.println("===============");
		System.out.println(checkIntNByte(a, 0));
		System.out.println(checkIntNByte(a, 1));
		System.out.println(checkIntNByte(a, 2));
		System.out.println(checkIntNByte(a, 3));
	}
}
