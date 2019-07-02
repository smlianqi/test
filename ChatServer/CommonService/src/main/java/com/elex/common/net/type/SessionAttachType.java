package com.elex.common.net.type;

/**
 * DefaultPlayer 附件类型
 * 
 * @author mausmars
 *
 */
public enum SessionAttachType {
	ISession_Key, // ISession的key
	SocketAddress_Key, // 地址

	SessionBox, //
	// ----------------------------
	ClientServiceId, // client服务id

	TimeoutManager, //
	DataTimeoutManager, //
	TargetSocketAddress, //
	// ----------------------------
	UserTestConfig,//
	;
}
