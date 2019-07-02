package com.elex.common.service.module;

/**
 * 逻辑模块服务类型
 * 
 * @author mausmars
 *
 */
public enum ModuleServiceType {
	// ---大厅服---
	Account, // 账号

	// ---逻辑服---
	ObjOperate, // 对象操作
	EventCentre, // 事件中心

	Common, //
	User, // 用户
	Building, // 建筑
	Economy, // 经济
	Hero, // 英雄
	Pve, // pve
	Room, // 房间
	Equip, // 装备
	Skill, // 技能
	Item, // 道具
	Lord, // 领主
	Armyqueue, // 军队队列
	Attr, // 属性
	Soldier, // 兵种
	Citywall, Vip, // vip

	Clinic, //
	Smithy, // 铁匠铺
	Mission, // 任务
	Mail, // 邮件
	College, // 学院
	Union, // 联盟

	// ---聊天---
	Chat,
	// ---战场服---

	Sandboxmaplogic, // 世界地图
	// ---公共---
	Error, // 错误

	Inner,//
	;
}
