package com.elex.common.component.player.type;

public enum PlayerMServiceType {
	PlayerMgr, // player管理服务
	State, // 状态机服务
	Event, // 事件中心服务

	// -----------------------
	// 用于logic服的
	Attr, // 属性管理服务
	DataChange, // 数据变更管理服务

	// -----------------------
	// 用于client服的
	Flowchart, // 指令控制流程
	;
}
