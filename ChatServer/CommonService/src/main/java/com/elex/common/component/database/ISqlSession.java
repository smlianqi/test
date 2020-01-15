package com.elex.common.component.database;

public interface ISqlSession {
	/**
	 * 关闭session
	 */
	void close();

	<T> T getSession();
}
