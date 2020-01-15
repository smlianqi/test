package com.elex.common.component.function.type;

/**
 * 功能类型
 * 
 * @author mausmars
 *
 */
public enum FunctionType {
	client, // 模拟客户端测试
	test, // 游戏test（测试）系统
	// ------------------------------
	config, // config（配置）系统
	gate, // 网关
	logic, // logic（逻辑）系统
	battle, // battle（战场）系统
	room, // room（房间）系统
	proto, // proto（静态数据）系统

	foyer, // foyer（大厅）系统
	account, // account（账号）系统
	gm, // gm系统;
	pay, // pay（付费）系统
	legion, // legion（ 军团 ）系统
	union, // union（ 联盟 ）系统
	map, // map（ 地图 ）系统
	chat,//chat（聊天）系统
	
	// MsgRouter, // MsgRouter（消息路由）系统
	;

}
