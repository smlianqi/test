package com.elex.im.test.flattest;

import java.nio.ByteBuffer;

import com.elex.im.test.flattest.message.TestFlat;
import com.elex.im.test.flattest.message.TestFlatBean;
import com.google.flatbuffers.FlatBufferBuilder;

public class Test {
	public static void main(String[] args) {
		FlatBufferBuilder builder = new FlatBufferBuilder(0);

		long[] test = new long[2];
		// builder.startVector(8, test.length, 0);
		// for (Long atOrders : test) {
		// builder.addLong(atOrders);
		// }
		// int test5Offset = builder.endVector();

		int test5Offset = TestFlatBean.createTest5Vector(builder, test);

		int test1Offset = builder.createString("test_111");
		TestFlatBean.startTestFlatBean(builder);
		TestFlatBean.addTest1(builder, test1Offset);
		TestFlatBean.addTest5(builder, test5Offset);

		int testBean1Offset = TestFlatBean.endTestFlatBean(builder);

		int[] data = new int[2];
		for (int i = 0; i < data.length; i++) {
			TestFlatBean.startTestFlatBean(builder);
			TestFlatBean.addTest1(builder, test1Offset);
			int tbo = TestFlatBean.endTestFlatBean(builder);

			data[i] = tbo;
		}
		int testBean2Offset = TestFlat.createTestBean2Vector(builder, data);

		TestFlat.startTestFlat(builder);
		TestFlat.addTestBean1(builder, testBean1Offset);
		TestFlat.addTestBean2(builder, testBean2Offset);
		int john = TestFlat.endTestFlat(builder);
		builder.finish(john);

		ByteBuffer byteBuffer = builder.dataBuffer();
	}
}
