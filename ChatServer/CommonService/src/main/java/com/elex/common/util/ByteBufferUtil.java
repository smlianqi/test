package com.elex.common.util;

import java.nio.ByteBuffer;

public class ByteBufferUtil {
	public static byte[] byteBuffer2Bytes(ByteBuffer bb) {
		byte[] data = new byte[bb.remaining()];
		bb.get(data, 0, data.length);
		return data;
	}
}
