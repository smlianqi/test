package com.elex.im.module.common.user.handler.proto;

import com.elex.common.component.player.IPlayer;
import com.elex.common.component.player.type.UserType;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.session.ISession;
import com.elex.im.message.proto.UserMessage.UserLoginDown;
import com.elex.im.module.common.user.IUserMService;
import com.elex.im.module.common.user.UserMessageHandler;

/**
 * 服务器返回登录状态
 * 
 * @author mausmars
 *
 */
public class UserLoginDownHandler extends UserMessageHandler<UserLoginDown> {
	public UserLoginDownHandler(IUserMService service) {
		super(service);
	}

	@Override
	public void unloginHandler(UserLoginDown message, ISession session, long userId) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> UserLoginDownHandler unloginHandler!!!!! userId=" + userId + " message=" + message);
		}
		UserType userType = UserType.valueOf(message.getUserType());
		if (userType == null) {
			logger.error("UserLoginUpHandler userType==null!!! userId=" + userId);
			return;
		}
		IPlayer player = service.createPlayer(session, userId, userType);
	}

	@Override
	public void loginedHandler(UserLoginDown message, IPlayer player) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> UserLoginDownHandler loginedHandler!!!!! userId=" + player.getUserId() + " message="
					+ message);
		}
	}

	public MessageConfig createMessageConfig() {
		MessageConfig messageConfig = new MessageConfig();
		// messageConfig.setCommonId(10002);
		messageConfig.setKey(UserLoginDown.MessageEnum.CommondId_VALUE);
		messageConfig.setMessage(UserLoginDown.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}
