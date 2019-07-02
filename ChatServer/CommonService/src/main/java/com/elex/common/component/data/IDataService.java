package com.elex.common.component.data;

import com.elex.common.component.data.operate.IDaoOperateService;
import com.elex.common.component.database.ISqlSession;
import com.elex.common.service.IService;

/**
 * 数据服务
 * 
 * @author mausmars
 *
 */
public interface IDataService extends IService {
	/**
	 * 
	 * @param pojoClassName
	 * @return
	 */
	DaoKit getDaoKit(String pojoClassName);

	DaoKit getDaoKit(Class<?> pojoClass);

	/**
	 * 获取dao操作服务
	 * 
	 * @return
	 */
	IDaoOperateService getDaoOperateService();

	/**
	 * 数据操作执行
	 */
	void daoOperateExecute();

	/**
	 * 开启session
	 * 
	 * @param isThreadBox
	 *            是否保存在线程box中
	 * @return
	 */
	ISqlSession openSession(boolean isThreadBox);

	void closeThreadBoxSession();
}
