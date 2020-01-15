package com.elex.common.net.message;

/**
 * 消息配置管理器
 * 
 * @author mausmars
 *
 */
public interface IMessageConfigMgr {
	/**
	 * 通过message类型获取消息key
	 * 
	 * @param msgClass
	 * @return
	 */
	String getKey(Class<?> msgClass);

	/**
	 * 得道指令cls
	 * 
	 * @param commondId
	 * @return
	 */
	MessageConfig getMessageConfig(String key);
}
