package com.elex.common.service.type;

/**
 * 服务状态类型
 * 
 * @author mausmars
 *
 */
public enum ServiceStateType {
	Initing, // 初始化中
	Inited, // 初始化结束
	Starting, // 启动中
	Started, // 运行结束运行中
	Stopping, // 停止中
	Stopped, // 已经停止
	;
}
