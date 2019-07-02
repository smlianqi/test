package com.elex.common.component.data.operate;

import com.elex.common.component.threadpool.task.IHashTask;

/**
 * dao操作仓库
 * 
 * @author mausmars
 *
 */
public interface IDaoOperateTask extends IHashTask {
	/**
	 * 
	 * @return
	 */
	String getHashKey();

	/**
	 * 
	 * @param daoOperate
	 */
	void insertDaoOperate(IDaoOperate daoOperate);

}
