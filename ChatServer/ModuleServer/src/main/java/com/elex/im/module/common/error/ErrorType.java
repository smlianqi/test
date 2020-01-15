package com.elex.im.module.common.error;

/**
 * 错误类型
 * 
 * @author mausmars
 *
 */
public enum ErrorType {
	ParamError(1), // 参数错误，这里的错误可能会很细
	DataError(2), // 数据错误，这个错误客户端重新获取模块数据
	SystemError(3), // 系统错误，暂时就模块服务器不可用
	;

	int value;

	ErrorType(int value) {
		this.value = value;
	}

	public int getType() {
		return value;
	}
}
