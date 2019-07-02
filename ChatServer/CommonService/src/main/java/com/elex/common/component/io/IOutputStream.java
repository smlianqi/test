package com.elex.common.component.io;

import java.io.IOException;

/**
 * 输出流接口
 * 
 * @author mausmars
 *
 */
public interface IOutputStream {
	/**
	 * 刷新此输出流并强制写出所有缓冲的输出字节
	 */
	void flush() throws IOException;

	/**
	 * 关闭此输出流并释放与此流有关的所有系统资源
	 */
	void close() throws IOException;
}
