package com.elex.common.component.task.quartz;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskUtil {
	public static long dataAndTimeChange(String data) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = sdf.parse(data, pos);
		return strtodate.getTime();
	}
}
