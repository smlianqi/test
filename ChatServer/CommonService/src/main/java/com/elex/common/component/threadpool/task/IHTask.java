package com.elex.common.component.threadpool.task;

public interface IHTask {
	/**
	 * key
	 * 
	 * @return
	 */
	String getHashKey();

	/**
	 * 组key
	 * 
	 * @return
	 */
	String getGroupKey();

	/**
	 * 是否直接hash
	 * 
	 * @return
	 */
	boolean isDirectHash();
}
