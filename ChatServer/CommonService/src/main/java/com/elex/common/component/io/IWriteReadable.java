package com.elex.common.component.io;

import java.io.IOException;

/**
 * 可读写接口
 * 
 * @author mausmars
 *
 */
public interface IWriteReadable {
	/**
	 * 将字段信息写入流
	 * 
	 * @param bytes
	 * @throws IOException
	 */
	void write(IDataOutput out) throws IOException;

	/**
	 * 读出流信息
	 * 
	 * @param bytes
	 * @throws IOException
	 */
	void read(IDataInput in) throws IOException;

	/**
	 * 获取序列化信息
	 * 
	 * @param bytes
	 * @throws IOException
	 */
	byte[] getBytes() throws IOException;

	/**
	 * 初始化字段
	 * 
	 * @param bytes
	 * @throws IOException
	 */
	void initField(byte[] bytes) throws IOException;
}
