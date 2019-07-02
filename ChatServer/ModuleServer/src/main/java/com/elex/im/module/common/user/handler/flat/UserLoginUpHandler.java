package com.elex.im.module.common.user.handler.flat;

import com.elex.common.component.member.data.MemberOnline;
import com.elex.common.component.player.IPlayer;
import com.elex.common.component.player.type.UserType;
import com.elex.common.net.message.IMessage;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.service.netty.session.SessionBox;
import com.elex.common.net.session.ISession;
import com.elex.common.net.type.SessionAttachType;
import com.elex.im.message.AModuleMessageCreater;
import com.elex.im.message.flat.CommondId_User;
import com.elex.im.message.flat.UserLoginUp;
import com.elex.im.module.HandleErrorType;
import com.elex.im.module.common.error.ErrorType;
import com.elex.im.module.common.user.IUserMService;
import com.elex.im.module.common.user.UserMessageHandler;

/**
 * 用户登录
 * 
 * @author mausmars
 *
 */
public class UserLoginUpHandler extends UserMessageHandler<UserLoginUp> {
	public UserLoginUpHandler(IUserMService service) {
		super(service);
	}

	@Override
	public void loginedHandler(UserLoginUp message, IPlayer player) {
		// 处理重复登录问题
		if (logger.isDebugEnabled()) {
			logger.debug("Chat UserLoginUpHandler loginedHandler!!!!! userId=" + player.getUserId());
		}
		String languageType = message.languageType();
		int userType = (int) message.userType();
		String extend = languageType + ":" + userType;
		// 登录
		MemberOnline memberOnline = service.bindMember(player, extend);

		// 回复消息
		send(player, memberOnline);
	}

	@Override
	public void unloginHandler(UserLoginUp message, ISession session, long userId) {
		if (logger.isDebugEnabled()) {
			logger.debug("Chat UserLoginUpHandler unloginHandler!!!!! userId=" + userId);
		}
		UserType userType = UserType.valueOf((int) message.userType());
		if (userType == null) {
			getErrorMService().sendErrorMessage(ErrorType.ParamError, HandleErrorType.ParameterError.getValue(), 0,
					session);
			return;
		}
		IPlayer player = service.createPlayer(session, userId, userType);

		// 登录
		String languageType = message.languageType();
		String extend = languageType + ":" + userType.ordinal();
		// 登录
		MemberOnline memberOnline = service.bindMember(player, extend);

		// 回复消息
		send(player, memberOnline);
	}

	private void send(IPlayer player, MemberOnline memberOnline) {
		SessionBox sessionBox = player.getAttachFromSession(SessionAttachType.SessionBox);
		AModuleMessageCreater moduleMessageCreater = sessionBox.getModuleMessageCreater();
		IMessage msg = moduleMessageCreater.createUserLoginDown(player.getUserId(), player.getUserType().ordinal(),
				memberOnline.getFunctionType(), memberOnline.getSid());
		player.send(msg);
	}

	public MessageConfig createMessageConfig() {
		MessageConfig messageConfig = new MessageConfig();
		// messageConfig.setCommonId(10001);
		messageConfig.setKey(CommondId_User.UserLoginUp);
		messageConfig.setMessage(UserLoginUp.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}
