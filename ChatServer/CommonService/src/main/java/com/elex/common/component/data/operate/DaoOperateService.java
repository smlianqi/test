package com.elex.common.component.data.operate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.elex.common.component.data.IDao;
import com.elex.common.component.data.IDataService;
import com.elex.common.component.threadpool.IPoolExecutor;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;
import com.elex.common.util.string.StringUtil;

/**
 * dao操作管理器（这里按处理线程处理）
 * 
 * @author mausmars
 *
 */
public class DaoOperateService implements IDaoOperateService {
	protected static final ILogger logger = XLogUtil.logger();

	// {GroupKey:IDaoOperateTask}
	private Map<String, IDaoOperateTask> daoOperateStoreMap = new HashMap<>();

	private IDataService dataService;

	public DaoOperateService(IDataService dataService) {
		this.dataService = dataService;
	}

	/**
	 * 插入数据操作
	 * 
	 * @param daoOperate
	 */
	@Override
	public void insertDaoOperate(IDao<?> dao, String methdoName, Object[] params, Class<?>[] paramTypes,
			IDaoCallBack daoCallBack, String daoGroupType, String mainFieldValue) {

		// {daoGroupType_mainFieldValue}
		StringBuilder groupKeySB = new StringBuilder();
		groupKeySB.append(daoGroupType);
		groupKeySB.append(StringUtil.SeparatorUnderline);
		groupKeySB.append(mainFieldValue);

		ReflectDaoOperate daoOperate = new ReflectDaoOperate();
		// TODO 这个key暂时没有用，这里的key是考虑合并操作用的
		daoOperate.setKey("");
		daoOperate.setGroupKey(groupKeySB.toString());
		daoOperate.setDao(dao);
		daoOperate.setParams(params);
		daoOperate.setParamTypes(paramTypes);
		daoOperate.setMethdoName(methdoName);
		daoOperate.setDaoCallBack(daoCallBack);

		String groupKey = daoOperate.getGroupKey();
		IDaoOperateTask daoOperateStore = daoOperateStoreMap.get(groupKey);
		if (daoOperateStore == null) {
			daoOperateStore = new DaoOperateTask("", groupKey, dataService);
			daoOperateStoreMap.put(groupKey, daoOperateStore);
		}
		daoOperateStore.insertDaoOperate(daoOperate);
	}

	@Override
	public void execute(IPoolExecutor poolExecutor) {
		Iterator<Entry<String, IDaoOperateTask>> it = daoOperateStoreMap.entrySet().iterator();

		for (; it.hasNext();) {
			Entry<String, IDaoOperateTask> entry = it.next();
			IDaoOperateTask task = entry.getValue();
			if (poolExecutor != null) {
				// 放入线程池执行
				if (logger.isDebugEnabled()) {
					logger.debug("TaskDao put pool! key=" + task.getHashKey() + ", groupKey=" + task.getGroupKey());
				}
				poolExecutor.execute(task);
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("TaskDao immediately execute! key=" + task.getHashKey() + ", groupKey="
							+ task.getGroupKey());
				}
				// 直接执行
				task.run();
			}
			// 移除
			it.remove();
		}
	}
}
