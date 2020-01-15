package com.elex.common.component.task.quartz;

/**
 * 任务配置
 * 
 * @author mausmars
 * 
 */
public interface ITaskConfig {
	public static final int NoPriority = -1;

	/**
	 * 是否异常取消
	 * 
	 * @return
	 */
	boolean isExceptionCancle();

	/**
	 * 是否中断取消
	 * 
	 * @return
	 */
	boolean isInterruptCancle();

	/**
	 * 是否执行中应用发生故障，需要重新执行
	 * 
	 * @return
	 */
	boolean isRequestRecovery();

	/**
	 * 得到优先级
	 * 
	 * @return
	 */
	int getPriority();
}
