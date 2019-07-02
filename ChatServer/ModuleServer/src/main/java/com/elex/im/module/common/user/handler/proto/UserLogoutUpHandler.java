package com.elex.im.module.common.user.handler.proto;

import com.elex.common.component.player.IPlayer;
import com.elex.common.net.message.MessageConfig;
import com.elex.im.message.proto.UserMessage.UserLogoutUp;
import com.elex.im.module.common.user.IUserMService;
import com.elex.im.module.common.user.UserMessageHandler;

/**
 * 用户登出
 * 
 * @author mausmars
 *
 */
public class UserLogoutUpHandler extends UserMessageHandler<UserLogoutUp> {
	public UserLogoutUpHandler(IUserMService service) {
		super(service);
	}

	public void loginedHandler(UserLogoutUp message, IPlayer player) {
		// 离线
		service.offline(player, System.currentTimeMillis());
	}

	public MessageConfig createMessageConfig() {
		MessageConfig messageConfig = new MessageConfig();
		// messageConfig.setCommonId(10003);
		messageConfig.setKey(UserLogoutUp.MessageEnum.CommondId_VALUE);
		messageConfig.setMessage(UserLogoutUp.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}

}
