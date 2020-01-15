package com.elex.common.component.netdelaycheck;

/**
 * 检查阶段
 * 
 * @author mausmars
 *
 */
public enum CheckStepType {
	CS_RequestServerTime(1), // 请求服务器时间
	CS_ReplyClientDelay(2), // 返回客户端延迟
	CS_ReplyServerTime(3), // 回复服务器时间
	;

	private int value;

	CheckStepType(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}
}
