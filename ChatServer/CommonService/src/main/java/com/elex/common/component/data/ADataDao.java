package com.elex.common.component.data;

import java.util.List;

import com.elex.common.component.ignite.IIgniteService;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

public abstract class ADataDao<T> implements IDao<T> {
	protected static final ILogger logger = XLogUtil.logger();

	protected IIgniteService igniteService;

	public ADataDao(IIgniteService igniteService) {
		this.igniteService = igniteService;
	}

	@Override
	public void insert(T pojo, Object attach) {
	}

	@Override
	public void update(T pojo, Object attach) {
	}

	@Override
	public void remove(T pojo, Object attach) {
	}

	@Override
	public void clear(Object attach) {
	}

	@Override
	public void createTable(Object attach) {
	}

	@Override
	public void batchInsert(List<T> pojos, Object attach) {
	}

	@Override
	public void batchUpdate(List<T> pojos, Object attach) {
	}

	@Override
	public void batchRemove(List<T> pojos, Object attach) {
	}

	@Override
	public List<T> selectAll(Object attach) {
		return null;
	}

	@Override
	public long selectCount(Object attach) {
		return 0;
	}

	@Override
	public List<T> selectPaging(int start, int count, Object attach) {
		return null;
	}
}
