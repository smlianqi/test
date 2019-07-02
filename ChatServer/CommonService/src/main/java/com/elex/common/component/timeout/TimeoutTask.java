package com.elex.common.component.timeout;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.elex.common.component.task.quartz.TaskContext;
import com.elex.common.util.priorityqueue.PriorityQueueMap;
import com.elex.common.util.priorityqueue.QueueCell;
import com.elex.common.util.time.TimeUtil;

/**
 * 数据超时任务，管理超时的数据
 * 
 * @author mausmars
 *
 */
public class TimeoutTask implements ITimeoutManager {
	private String key;

	// 秒
	private int timeout;
	// 队列
	private PriorityQueueMap priorityQueue = new PriorityQueueMap();
	private Lock lock = new ReentrantLock();

	public TimeoutTask(String key) {
		this.key = key;
	}

	@Override
	public String getTimingKey() {
		return key;
	}

	public void insertTimeoutCheck(String key, Object obj, ITimeoutCallBlack callBlack) {
		// 超时时间（秒）
		long time = TimeUtil.currentSystemTimeSeconds() + timeout;
		insertTimeoutCheck(key, time, obj, callBlack);
	}

	@Override
	public void insertTimeoutCheck(String key, long time, Object obj, ITimeoutCallBlack callBlack) {
		QueueCell cell = new QueueCell();
		cell.setKey(key);
		cell.setTime(time);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Timeout_Object", obj);
		params.put("Timeout_CallBlack", callBlack);
		cell.setObj(params);

		lock.lock();
		try {
			priorityQueue.putCell(cell);
		} finally {
			lock.unlock();
		}
	}

	public void removeTimeoutCheck(String key) {
		lock.lock();
		try {
			priorityQueue.removeCell(key);
		} finally {
			lock.unlock();
		}
	}

	public void resetTime(String key) {
		long time = TimeUtil.currentSystemTimeSeconds() + timeout;
		resetTime(key, time);
	}

	@Override
	public void resetTime(String key, long time) {
		lock.lock();
		try {
			QueueCell cell = priorityQueue.removeCell(key);
			cell.setTime(time);
			priorityQueue.putCell(cell);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void execute(TaskContext context) {
		long executeTime = TimeUtil.currentSystemTimeSeconds();
		for (;;) {
			Set<QueueCell> cells = null;
			lock.lock();
			try {
				cells = priorityQueue.pollCell(executeTime);
			} finally {
				lock.unlock();
			}
			if (cells == null) {
				break;
			}
			for (QueueCell cell : cells) {
				Map<String, Object> params = (Map<String, Object>) cell.getObj();
				Object obj = params.get("Timeout_Object");
				ITimeoutCallBlack callBlack = (ITimeoutCallBlack) params.get("Timeout_CallBlack");
				if (callBlack != null) {
					callBlack.callblack(obj);
				}
			}
		}
	}

	// -----------------------------------------
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}
