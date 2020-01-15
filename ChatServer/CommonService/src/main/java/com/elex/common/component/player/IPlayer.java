package com.elex.common.component.player;

import com.elex.common.component.player.service.IPlayerMService;
import com.elex.common.component.player.type.PlayerAttachType;
import com.elex.common.component.player.type.PlayerMServiceType;
import com.elex.common.component.player.type.UserType;
import com.elex.common.net.session.ISession;
import com.elex.common.net.type.SessionAttachType;
import com.elex.common.service.IGlobalContext;

/**
 * player接口
 * 
 * @author mausmars
 *
 */
public interface IPlayer {
	/**
	 * player的userId
	 * 
	 * @return
	 */
	Long getUserId();

	/**
	 * 用户类型
	 * 
	 * @return
	 */
	UserType getUserType();

	/**
	 * player的session 如果session为null则是临时的数据
	 * 
	 * @return
	 */
	ISession getSession();

	/**
	 * 替换session
	 * 
	 * @return
	 */
	void replaceSessionAndCloseOld(ISession session);

	/**
	 * 直接替换
	 * 
	 * @param session
	 */
	void replaceSession(ISession session);

	/**
	 * 清除session
	 */
	void clearSession();

	/**
	 * 直接发送消息
	 * 
	 * @param key
	 * @return
	 */
	void send(Object msg);

	/**
	 * 获取上下文
	 * 
	 * @return
	 */
	<T extends IGlobalContext> T getContext();

	/**
	 * 获取player服务
	 * 
	 * @param type
	 * @return
	 */
	<T extends IPlayerMService> T getPlayerMService(PlayerMServiceType type);

	/**
	 * player自己的附件
	 * 
	 * @param key
	 * @return
	 */
	<T> T getAttach(PlayerAttachType key);

	/**
	 * player自己的附件
	 * 
	 * @param key
	 * @param value
	 */
	void setAttach(PlayerAttachType key, Object value);

	/**
	 * session中设置附加属性
	 * 
	 * @param key
	 * @param attachment
	 * @return
	 */
	void setAttachToSession(SessionAttachType key, Object attachment);

	/**
	 * 
	 * session中获取附加值
	 * 
	 * @param key
	 * @return
	 */
	<T> T getAttachFromSession(SessionAttachType key);
}
