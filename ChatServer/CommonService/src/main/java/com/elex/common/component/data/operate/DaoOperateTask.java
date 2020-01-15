package com.elex.common.component.data.operate;

import java.util.LinkedList;
import java.util.List;

import com.elex.common.component.data.IDataService;
import com.elex.common.component.database.ISqlSession;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

/**
 * dao操作仓库
 * 
 * @author mausmars
 *
 */
public class DaoOperateTask implements IDaoOperateTask {
	protected static final ILogger logger = XLogUtil.logger();

	private String key;
	private String groupKey;
	private List<IDaoOperate> daoOperates;

	private IDataService dataService;

	public DaoOperateTask(String key, String groupKey, IDataService dataService) {
		this.key = key;
		this.groupKey = groupKey;
		this.dataService = dataService;
		this.daoOperates = new LinkedList<IDaoOperate>();
	}

	@Override
	public String getHashKey() {
		return key;
	}

	@Override
	public String getGroupKey() {
		return groupKey;
	}

	@Override
	public boolean isDirectHash() {
		return false;
	}

	@Override
	public void insertDaoOperate(IDaoOperate daoOperate) {
		daoOperates.add(daoOperate);
	}

	@Override
	public void run() {
		// 合并操作
		// reFlush();

		// 操作dao
		if (logger.isDebugEnabled()) {
			logger.debug("DaoOperateTask run! key=" + key + ", groupKey=" + groupKey);
		}

		ISqlSession sqlSession = dataService.openSession(false);
		try {
			for (IDaoOperate daoOperate : daoOperates) {
				daoOperate.execute(sqlSession.getSession());
			}
		} finally {
			if (sqlSession != null) {
				// 释放session
				sqlSession.close();
			}
		}
	}

	// private void reFlush() {
	// List<IDaoOperate> cbs = new LinkedList<IDaoOperate>();
	// Map<String, IDaoOperate> daoCallBackMap = new HashMap<String,
	// IDaoOperate>();
	// for (IDaoOperate daoOperate : daoOperates) {
	// if (!daoOperate.canMerge()) {
	// // 不能合并
	// cbs.add(daoOperate);
	// continue;
	// }
	// String key = daoOperate.getKey();
	// if (!daoCallBackMap.containsKey(key)) {
	// daoCallBackMap.put(key, daoOperate);
	// cbs.add(daoOperate);
	// } else {
	// IDaoOperate oldBc = daoCallBackMap.get(key);
	// // 开始合并
	// oldBc.merge(daoOperate);
	// }
	// }
	// for (IDaoOperate daoOperate : cbs) {
	// if (!daoOperate.canExecute()) {
	// // 不可以执行
	// continue;
	// }
	// // 执行数据库操作
	// daoOperate.execute();
	// }
	// }
}
