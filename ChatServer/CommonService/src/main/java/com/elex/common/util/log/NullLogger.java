package com.elex.common.util.log;

import org.apache.logging.log4j.Logger;

public class NullLogger implements ILogger {
	private Logger logger = null;

	@Override
	public LoggerType getLoggerType() {
		return LoggerType.Log4j2;
	}

	@Override
	public <T> T getRawLogger() {
		return (T) logger;
	}

	@Override
	public String getName() {
		return logger.getName();
	}

	@Override
	public boolean isTraceEnabled() {
		if (logger == null) {
			return false;
		}
		return logger.isTraceEnabled();
	}

	@Override
	public void trace(String msg) {
		if (logger == null) {
			return;
		}
		logger.trace(msg);
	}

	@Override
	public void trace(String msg, Throwable t) {
		if (logger == null) {
			return;
		}
		logger.trace(msg, t);
	}

	@Override
	public boolean isDebugEnabled() {
		if (logger == null) {
			return false;
		}
		return logger.isDebugEnabled();
	}

	@Override
	public void debug(String msg) {
		if (logger == null) {
			return;
		}
		logger.debug(msg);
	}

	@Override
	public void debug(String msg, Throwable t) {
		if (logger == null) {
			return;
		}
		logger.debug(msg, t);
	}

	@Override
	public void debug(String format, Object arg1, Object arg2) {
		if (logger == null) {
			return;
		}
		logger.debug(format, arg1, arg2);
	}

	@Override
	public boolean isInfoEnabled() {
		if (logger == null) {
			return false;
		}
		return logger.isInfoEnabled();
	}

	@Override
	public void info(String msg) {
		if (logger == null) {
			return;
		}
		logger.info(msg);
	}

	@Override
	public void info(String msg, Throwable t) {
		if (logger == null) {
			return;
		}
		logger.info(msg, t);
	}

	@Override
	public void info(String format, Object arg1, Object arg2) {
		if (logger == null) {
			return;
		}
		logger.info(format, arg1, arg2);
	}

	@Override
	public boolean isWarnEnabled() {
		if (logger == null) {
			return false;
		}
		return logger.isWarnEnabled();
	}

	@Override
	public void warn(String msg) {
		if (logger == null) {
			return;
		}
		logger.warn(msg);
	}

	@Override
	public void warn(String msg, Throwable t) {
		if (logger == null) {
			return;
		}
		logger.warn(msg, t);
	}

	@Override
	public boolean isErrorEnabled() {
		if (logger == null) {
			return false;
		}
		return logger.isErrorEnabled();
	}

	@Override
	public void error(String msg) {
		if (logger == null) {
			return;
		}
		logger.error(msg);
	}

	@Override
	public void error(String msg, Throwable t) {
		if (logger == null) {
			return;
		}
		logger.error(msg, t);
	}
}
