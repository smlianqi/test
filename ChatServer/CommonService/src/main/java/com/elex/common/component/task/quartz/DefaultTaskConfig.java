package com.elex.common.component.task.quartz;

/**
 * 默认配置
 * 
 * @author mausmars
 *
 */
public class DefaultTaskConfig implements ITaskConfig {
	public static DefaultTaskConfig DefaultTaskConfig = new DefaultTaskConfig();

	@Override
	public boolean isExceptionCancle() {
		return false;
	}

	@Override
	public boolean isInterruptCancle() {
		return false;
	}

	@Override
	public boolean isRequestRecovery() {
		return false;
	}

	@Override
	public int getPriority() {
		return ITaskConfig.NoPriority;
	}
}
