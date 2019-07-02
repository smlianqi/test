package com.elex.common.component.data;

import java.util.Map;

import com.elex.common.component.io.IWriteReadable;

public interface ISpread {
	/**
	 * 取数据后; 1.执行数据的转换 2.如果有索引变更，要清空changeIndexs
	 */
	void obtainAfter();

	/**
	 * 存数据前 1.执行数据的转换
	 */
	void saveBefore();

	/**
	 * 存数据后 1.如果有索引变更，要清空changeIndexs
	 */
	void saveAfter();

	/**
	 * 拷贝对象
	 * 
	 * @param isSaveBefore
	 * @return
	 */
	<T> T cloneEntity(boolean isSaveBefore);

	/**
	 * 获取索引变化前的值
	 * 
	 * @return
	 */
	Map<String, Object> getIndexChangeBefore();
}
