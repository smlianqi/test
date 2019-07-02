package com.elex.common.component.data;

import java.util.List;

public interface ITableDao {
	/**
	 * 创建表
	 * 
	 * @param arg
	 */
	void createTable(Object attach);

	/**
	 * 获取全部表名
	 * 
	 * @param arg
	 */
	List<String> selectAllTableName(Object attach);
}
