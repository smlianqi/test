package com.elex.im.test.quartz;

import java.util.Date;

import org.quartz.CronExpression;

import com.elex.common.util.time.TimeUtil;

public class TestQuartzTime {
	public static void main(String[] args) throws Exception {
		String startTime = "0 0 10 ? * FRI";
		CronExpression ce = new CronExpression(startTime);

		int weekTime = 7 * 24 * 3600 * 1000;

		Date currentDate = TimeUtil.strDate2DateHMS("2017-12-01 10:00:00");
		Date date = ce.getTimeAfter(currentDate);

		long difference = date.getTime() % weekTime;// 差值为了计算版本
		long time = date.getTime() - weekTime;
		int offset = 20 * 60 * 1000;

		long currentTime = currentDate.getTime();
		if (currentTime >= time) {
			if (currentTime <= time + offset) {
				// 启动当前任务
				long version = (time - difference) / weekTime;
				System.out.println("当前周五任务 " + version);
			} else {
				// 启动下一周任务
				time = date.getTime();
				long version = (time - difference) / weekTime;
				System.out.println("下一周五任务 " + version);
			}
		}
	}
}
