package com.elex.common.component.data;

import java.util.List;

/**
 * 数据操作口IDao
 * 
 * @author mausmars
 *
 * @param <T>
 */
public interface IDao<T extends Object> {
	Class<T> getPojoClass();

	/**
	 * 通用插入
	 * 
	 * @param attach
	 * @return
	 */
	void insert(T pojo, Object attach);

	/**
	 * 通用更新
	 * 
	 * @param attach
	 * @return
	 */
	void update(T pojo, Object attach);

	/**
	 * 通用删除
	 * 
	 * @param attach
	 * @return
	 */
	void remove(T pojo, Object attach);

	/**
	 * 清除整个表
	 * 
	 * @param attach
	 * @return
	 */
	void clear(Object attach);

	/**
	 * 创建表
	 * 
	 * @param attach
	 */
	void createTable(Object attach);

	/**
	 * 通用批量插入
	 * 
	 * @param attach
	 * @return
	 */
	void batchInsert(List<T> pojos, Object attach);

	/**
	 * 通用批量更新
	 * 
	 * @param attach
	 * @return
	 */
	void batchUpdate(List<T> pojos, Object attach);

	/**
	 * 通用批量删除
	 * 
	 * @param attach
	 * @return
	 */
	void batchRemove(List<T> pojos, Object attach);

	/**
	 * 查询全部
	 * 
	 * @param attach
	 * @return
	 */
	List<T> selectAll(Object attach);

	/**
	 * 查询数量
	 * 
	 * @param attach
	 * @return
	 */
	long selectCount(Object attach);

	/**
	 * 分页查询
	 * 
	 * @param start
	 * @param count
	 * @param attach
	 * @return
	 */
	List<T> selectPaging(int start, int count, Object attach);
}
