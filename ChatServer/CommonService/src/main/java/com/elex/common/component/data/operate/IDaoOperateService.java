package com.elex.common.component.data.operate;

import com.elex.common.component.data.IDao;
import com.elex.common.component.threadpool.IPoolExecutor;

/**
 * dao操作服务
 * 
 * @author mausmars
 *
 */
public interface IDaoOperateService {
	/**
	 * 执行
	 */
	void execute(IPoolExecutor poolExecutor);

	/**
	 * 插入操作
	 * 
	 * @param daoOperate
	 */
	void insertDaoOperate(IDao<?> dao, String methdoName, Object[] params, Class<?>[] paramTypes,
			IDaoCallBack daoCallBack, String daoGroupType, String mainFieldValue);

}
