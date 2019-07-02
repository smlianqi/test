package com.elex.common.component.net.handlerconfig;

import java.util.List;

import com.elex.common.component.player.IPlayerFactory;
import com.elex.common.net.handler.IClosedSessionHandler;
import com.elex.common.net.handler.ICreateSessionHandler;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.rounter.IMessageRounter;
import com.elex.common.net.type.MegProtocolType;

/**
 * 网络handler配置接口
 * 
 * @author Administrator
 *
 */
public interface INetHandlerConfig {
	/**
	 * 获取消息配置
	 * 
	 * @param c
	 * @return
	 */
	List<MessageConfig> getMessageConfigs(MegProtocolType megProtocolType);

	IMessageRounter getMessageRounter();

	IPlayerFactory getPlayerFactory();

	Object getListener();

	/**
	 * 获取session创建处理器
	 * 
	 * @return
	 */
	List<ICreateSessionHandler> getCreateSessionHandlers();

	/**
	 * 获取session关闭处理器
	 * 
	 * @return
	 */
	List<IClosedSessionHandler> getCloseSessionHandlers();
}
