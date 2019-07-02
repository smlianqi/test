/**
 * 
 */
package com.elex.im.module.common;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.player.IPlayer;
import com.elex.common.net.handler.IMessageInHandler;
import com.elex.common.net.service.netty.session.SessionBox;
import com.elex.common.net.session.ISession;
import com.elex.common.net.type.SessionAttachType;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

/**
 * @author mausmars
 * 
 */
public abstract class IGameHandler<T> implements IMessageInHandler<T> {
	protected static final ILogger logger = XLogUtil.logger();

	@Override
	public void inhandle(T message, ISession session, Object attr) {
		long userId = (Long) attr;
		try {
			SessionBox sessionBox = session.getAttach(SessionAttachType.SessionBox);
			IPlayer player = sessionBox.getPlayerFactory().getPlayerMgrService().select(userId);
			if (player == null) {// 用户注册或者登陆
				if (logger.isDebugEnabled()) {
					logger.debug(">>> MsgHandler unloginHandler! " + message.getClass().getSimpleName() + "| userId="
							+ userId);
				}
				unloginHandler(message, session, userId);
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug(">>> MsgHandler loginedHandler! " + message.getClass().getSimpleName() + "| userId="
							+ userId);
				}
				// 每次都判断session不同时的替换
				sessionBox.replaceSession(player, session);
				loginedHandler(message, player);
			}
		} catch (Exception e) {
			logger.error("MsgHandler error! target=" + userId + ",message=" + message, e);
		} finally {
		}
	}

	public void unloginHandler(T message, ISession session, long userId) {
		logger.error("Error user state!  login!" + message.getClass());
	}

	public void loginedHandler(T message, IPlayer player) {
		logger.error("Error user state! no login!" + message.getClass());
	}

	public FunctionType getFunctionType() {
		return FunctionType.test;
	}
}