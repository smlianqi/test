package com.elex.common.component.database;

import com.elex.common.service.IService;

/**
 * 数据库服务
 * 
 * @author mausmars
 *
 */
public interface IDatabaseService extends IService {
	/**
	 * 开启新session
	 * 
	 * @return
	 */
	ISqlSession openSession();

	/**
	 * 开启新session
	 * 
	 * @param transactionIsolationLevel
	 *            事务隔离等级（参考Connection类）;
	 *            	int TRANSACTION_NONE             = 0;
	 *				int TRANSACTION_READ_UNCOMMITTED = 1;
	 *				int TRANSACTION_READ_COMMITTED   = 2;
	 *				int TRANSACTION_REPEATABLE_READ  = 4;
	 *				int TRANSACTION_SERIALIZABLE     = 8;
	 *            
	 * @return
	 */
	Object openSession(int transactionIsolationLevel);
}
