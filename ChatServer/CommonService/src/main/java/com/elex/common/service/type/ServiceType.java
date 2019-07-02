package com.elex.common.service.type;

/**
 * 服务类型
 * 
 * @author mausmars
 *
 */
public enum ServiceType {
	// 基础服务
	cache, // 缓存服务
	zookeeper, // zk服务
	database, // 数据库服务
	objectpool, // 对象池服务
	timeout, // 超时服务
	// -----------------------
	netclient, // 网络客户端，netty，httpclient
	netserver, // 网络服务器，netty，jetty
	rpc, // rpc服务 ice，Hessian（http）,netty（自定义）
	rpcprx, // rpc服务 ice，Hessian（http）,netty（自定义）
	// -----------------------
	function, // 功能服务
	// -----------------------
	data, // 数据服务
	prototype, // 静态数据客户端
	// -----------------------
	member, // 成员位置管理
	// -----------------------
	lock, // 锁服务（分布式锁）
	// -----------------------
	threadbox, // 线程工具箱
	threadpool, // 线程池服务
	// -----------------------
	task, // 定时任务服务
	// -----------------------
	script, // 脚本服务
	// -----------------------
	ignite, //
	// -----------------------
	event,//
	;
}
