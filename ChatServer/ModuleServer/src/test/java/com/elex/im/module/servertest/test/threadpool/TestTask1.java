package com.elex.im.module.servertest.test.threadpool;

import com.elex.common.component.threadpool.task.IHashCallable;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

public class TestTask1 implements IHashCallable {
	protected static final ILogger logger = XLogUtil.logger();

	private String key;
	private String groupKey;

	public TestTask1(String key, String groupKey) {
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
	public Object call() throws Exception {
		logger.debug("Start! [TestTask1] key=" + key + " ,groupKey=" + groupKey);

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		logger.debug("Finished! [TestTask1] key=" + key + " ,groupKey=" + groupKey);
		return 111;
	}
}
