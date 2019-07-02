package com.elex.common.component.threadpool.task;

/**
 * 任务状态
 * 
 * @author mausmars
 *
 */
public enum TaskStateType {
	Wait, // 在队列中等待
	Runing, // 运行中
	Finished, // 完成
	Cancel, // 取消任务
	;
}
