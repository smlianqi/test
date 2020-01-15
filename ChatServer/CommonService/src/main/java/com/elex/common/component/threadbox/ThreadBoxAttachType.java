package com.elex.common.component.threadbox;

/**
 * ThreadBox 附件类型
 * 
 * @author mausmars
 *
 */
public enum ThreadBoxAttachType {
	SqlSession, // sql的session
	DaoOperateService, // dao操作服务
	PrototypeService, // 静态数据服务
	CurrentTimeMillis, // 当前时间
	CommandId, // 指令id
	ScriptMgr,// 脚本管理器
	;
}
