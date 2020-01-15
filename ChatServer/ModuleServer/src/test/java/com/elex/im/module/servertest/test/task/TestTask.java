package com.elex.im.module.servertest.test.task;

import java.util.concurrent.atomic.AtomicInteger;

import com.elex.common.component.task.ITimingTask;
import com.elex.common.component.task.quartz.TaskContext;

public class TestTask implements ITimingTask {
	private AtomicInteger atomic = new AtomicInteger();

	private String key;

	public TestTask(String key) {
		this.key = key;
	}

	@Override
	public String getTimingKey() {
		return key;
	}

	@Override
	public void execute(TaskContext context) {
		for (;;) {
			System.out.println("test_" + atomic.incrementAndGet());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
