package com.elex.common.util.log;

public class XLogUtil {
//	 private static ILogger logger = new LogSlf4jger();
	private static ILogger logger = new Log4j2ger();

	public static ILogger logger() {
		return logger;
	}
}
