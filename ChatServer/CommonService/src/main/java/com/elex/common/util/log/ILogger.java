package com.elex.common.util.log;

public interface ILogger {
	LoggerType getLoggerType();

	<T> T getRawLogger();

	String getName();

	boolean isTraceEnabled();

	void trace(String msg);

	void trace(String msg, Throwable t);

	// ---------------------------
	boolean isDebugEnabled();

	void debug(String msg);

	void debug(String msg, Throwable t);

	void debug(String format, Object arg1, Object arg2);
	// ---------------------------
	public boolean isInfoEnabled();

	void info(String msg);

	void info(String msg, Throwable t);

	void info(String format, Object arg1, Object arg2);

	// ---------------------------
	public boolean isWarnEnabled();

	void warn(String msg);

	void warn(String msg, Throwable t);

	// ---------------------------
	public boolean isErrorEnabled();

	void error(String msg);

	void error(String msg, Throwable t);
}
