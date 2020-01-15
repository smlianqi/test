package com.elex.im.module.servertest.test.threadpool;

import com.elex.common.component.threadpool.task.IHashTask;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

public class TestTask2 implements IHashTask {
	protected static final ILogger logger = XLogUtil.logger();

	private String key;
	private String groupKey;

	public TestTask2(String key, String groupKey) {
		this.key = key;
		this.groupKey = groupKey;
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
	public void run() {
		logger.debug("Start! [TestTask2] key=" + key + " ,groupKey=" + groupKey);

		logger.debug("Finished! [TestTask2] key=" + key + " ,groupKey=" + groupKey);
	}
}
