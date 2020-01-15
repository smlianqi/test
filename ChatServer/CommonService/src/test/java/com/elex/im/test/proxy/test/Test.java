package com.elex.im.test.proxy.test;

public class Test extends ATest implements ITest {
	private int a;

	public Test(int a) {
		this.a = a;
	}

	@Override
	public int test(int value) {
		return a + value;
	}

	@Override
	public int base(int value) {
		return a + value + 1;
	}

}
