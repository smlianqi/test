package com.elex.common.util.base64;

import org.apache.commons.codec.binary.Base64;

public class Base64Util {
	/**
	 * 解码
	 * 
	 * @param base64Data
	 * @return
	 */
	public static byte[] decode(byte[] base64Data) {
		return Base64.decodeBase64(base64Data);
	}

	/**
	 * 编码
	 * 
	 * @param base64Data
	 * @return
	 */
	public static byte[] encode(byte[] base64Data) {
		return Base64.encodeBase64(base64Data);
	}

	/**
	 * 编码
	 * 
	 * @param base64Data
	 * @return
	 */
	public static byte[] encode2Byte(String str) {
		return Base64.encodeBase64(str.getBytes());
	}

	/**
	 * 编码
	 * 
	 * @param base64Data
	 * @return
	 */
	public static String encode(String str) {
		return Base64.encodeBase64String(str.getBytes());
	}
}
