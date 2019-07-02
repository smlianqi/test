package com.elex.common.component.io;

import java.io.IOException;

/**
 * 输入流接口
 * 
 * @author mausmars
 *
 */
public interface IInputStream {
	/**
	 * 返回此输入流下一个方法调用可以不受阻塞地从此输入流读取（或跳过）的估计字节数
	 * 
	 * @return
	 */
	int available() throws IOException;

	/**
	 * 在此输入流中标记当前的位置
	 * 
	 * @param readlimit
	 */
	void mark(int readlimit);

	/**
	 * 跳过和丢弃此输入流中数据的 n 个字节
	 * 
	 * @param n
	 * @return
	 */
	long skip(long n) throws IOException;

	/**
	 * 测试此输入流是否支持 mark 和 reset 方法
	 * 
	 * @return
	 */
	boolean markSupported();

	/**
	 * 将此流重新定位到最后一次对此输入流调用 mark 方法时的位置
	 */
	void reset() throws IOException;

	/**
	 * 关闭此输入流并释放与该流关联的所有系统资源
	 */
	void close() throws IOException;
}
