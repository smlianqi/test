package com.elex.common.util.time;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class TimeUtil {
	public static final int MinutesPerHour = 60;// 每小时分钟数
	public static final int MillisecondsPerMinute = 60000;// 每分钟毫秒数
	public static final int MillisecondsPerSeconds = 1000;// 每秒钟毫秒数
	public static final int MilliDayPerSeconds = 86400000;// 每天毫秒数

	public static long currentSystemTime() {
		return System.currentTimeMillis();
	}

	public static long currentSystemTimeSeconds() {
		return System.currentTimeMillis() / MillisecondsPerSeconds;
	}

	public static long getTodayStartTime() {
		Calendar currentDate = new GregorianCalendar();
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		currentDate.set(Calendar.MILLISECOND, 0);
		return currentDate.getTime().getTime();
	}

	/** 当天的小时差 */
	public static long getTodayStartTime(int hour) {
		Calendar currentDate = new GregorianCalendar();
		currentDate.set(Calendar.HOUR_OF_DAY, hour);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		currentDate.set(Calendar.MILLISECOND, 0);
		return currentDate.getTime().getTime();
	}

	/** 向后取time整点时间 */
	public static long getTimeWhole(long time) {
		Calendar currentDate = new GregorianCalendar();
		currentDate.setTime(new Date(time));

		int hour = currentDate.get(Calendar.HOUR_OF_DAY);
		int minute = currentDate.get(Calendar.MINUTE);
		int second = currentDate.get(Calendar.SECOND);
		int millisecond = currentDate.get(Calendar.MILLISECOND);

		if (minute != 0 || second != 0 || millisecond != 0) {
			hour++;
		}
		currentDate.set(Calendar.HOUR_OF_DAY, hour);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		currentDate.set(Calendar.MILLISECOND, 0);
		return currentDate.getTime().getTime();
	}

	public static int getTimeWholeHour(long time) {
		Calendar currentDate = new GregorianCalendar();
		currentDate.setTime(new Date(time));

		int hour = currentDate.get(Calendar.HOUR_OF_DAY);
		int minute = currentDate.get(Calendar.MINUTE);
		int second = currentDate.get(Calendar.SECOND);
		int millisecond = currentDate.get(Calendar.MILLISECOND);

		if (minute != 0 || second != 0 || millisecond != 0) {
			hour++;
		}
		return hour;
	}

	public static int getIntervalNum(int cdTime, int hour) {
		Calendar cal = Calendar.getInstance();
		TimeZone timeZone = cal.getTimeZone();
		long time = System.currentTimeMillis() + timeZone.getRawOffset() - hour * 3600000;
		double dayNum = Math.ceil(time / ((cdTime * MillisecondsPerSeconds)));
		return (int) dayNum;
	}

	public static int getIntervalNum(int cdTime, long time, int hour) {
		Calendar cal = Calendar.getInstance();
		TimeZone timeZone = cal.getTimeZone();
		long timeTemp = time + timeZone.getRawOffset() - hour * 3600000;
		double dayNum = Math.ceil(timeTemp / ((cdTime * MillisecondsPerSeconds)));
		return (int) dayNum;
	}

	public static void main(String[] args) {
		long t2 = strDate2DateHMS("2015-2-10 18:50:01").getTime();
		int t = TimeUtil.getIntervalNum(3600, 0);
		int t1 = TimeUtil.getIntervalNum(3600, t2, 0);
		System.out.println(t);
		System.out.println(t1);
		System.out.println(t2);

		long temp1 = strDate2DateHMS("2015-2-10 23:50:01").getTime();
		long temp2 = getTimeWhole(temp1);
		String dateStr = getDateStr(temp2);
		System.out.println(dateStr);
	}

	public static String getDate(String pattern, String id) {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		sdf.setTimeZone(TimeZone.getTimeZone(id));
		String snow = sdf.format(now);
		return snow;
	}

	public static String getDateStr(long time) {
		Date date = new Date(time);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(date);
	}

	public static String getDateStr(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(date);
	}

	public static Date strDate2DateYMD(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = sdf.parse(date, pos);
		return strtodate;
	}

	public static Date strDate2DateHMS(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = sdf.parse(date, pos);
		return strtodate;
	}

	public static int getServiceDate(int moth, int cdTime, String serviceCreatDate) {
		long currentTime = System.currentTimeMillis();
		long serviceCreatTime = TimeUtil.strDate2DateYMD(serviceCreatDate).getTime();
		long dayNum = ((currentTime - serviceCreatTime) / (cdTime * MillisecondsPerSeconds)) % moth;
		return (int) dayNum;
	}

	public static int getServiceMoth(int moth, int cdTime, String serviceCreatDate) {
		long currentTime = System.currentTimeMillis();
		long serviceCreatTime = TimeUtil.strDate2DateYMD(serviceCreatDate).getTime();
		long dayNum = ((currentTime - serviceCreatTime) / (cdTime * MillisecondsPerSeconds)) / moth;
		return (int) dayNum;
	}

	public static int getVersionId(String serviceCreatDate, int week, long time) {
		int versionId = 1;
		long currentTime = System.currentTimeMillis();
		long serviceCreatTime = TimeUtil.strDate2DateYMD(serviceCreatDate).getTime();
		int weekDay = TimeUtil.getWeekOfDate(serviceCreatTime);
		int currentWeekDay = TimeUtil.getWeekOfDate(currentTime);
		int subDay = (week - weekDay) >= 0 ? (week - weekDay) : (week - weekDay + 7);
		long dayNum = ((currentTime - serviceCreatTime) / (86400 * MillisecondsPerSeconds));
		versionId = (dayNum - subDay) >= 0 ? (int) ((dayNum - subDay) / 7) + 2 : 1;
		long currentDayTime = currentTime - TimeUtil.getTodayStartTime();
		if (currentWeekDay == week) {
			if (currentDayTime < time) {
				versionId -= 1;
			}
		}
		return (int) versionId;
	}

	public static int getWeekOfDate(long dateTime) {
		Calendar cal = Calendar.getInstance();
		Date date = new Date(dateTime);
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK);
		return w;
	}

	public static long getTaskTime(String strs) {
		String[] taskTimes = strs.split(" ");
		long taskTime = 0;
		taskTime = (Integer.parseInt(taskTimes[0]) + Integer.parseInt(taskTimes[1]) * 60
				+ Integer.parseInt(taskTimes[0]) * 3600) * MillisecondsPerSeconds;
		return taskTime;
	}

	public static int getTaskWeek(String strs) {
		String[] taskTimes = strs.split(" ");
		return Integer.parseInt(taskTimes[5]);
	}

}
