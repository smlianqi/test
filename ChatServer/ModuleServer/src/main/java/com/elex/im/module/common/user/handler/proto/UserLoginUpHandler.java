package com.elex.im.module.common.user.handler.proto;

import com.elex.common.component.member.data.MemberOnline;
import com.elex.common.component.player.IPlayer;
import com.elex.common.component.player.type.PlayerAttachType;
import com.elex.common.component.player.type.UserType;
import com.elex.common.net.message.IMessage;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.service.netty.session.SessionBox;
import com.elex.common.net.session.ISession;
import com.elex.common.net.type.SessionAttachType;
import com.elex.im.message.AModuleMessageCreater;
import com.elex.im.message.proto.UserMessage.UserLoginUp;
import com.elex.im.module.HandleErrorType;
import com.elex.im.module.common.error.ErrorType;
import com.elex.im.module.common.user.IUserMService;
import com.elex.im.module.common.user.UserMessageHandler;
import com.elex.im.module.translation.type.LanguageType;

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

		// 登录
		String languageType = message.getLanguageType();
		int userType = message.getUserType();

		String extend = languageType + ":" + userType;
		service.loginInit(player);
		MemberOnline memberOnline = service.bindMember(player, extend);

		// 回复消息
		send(player, memberOnline);
	}

	@Override
	public void unloginHandler(UserLoginUp message, ISession session, long userId) {
		if (logger.isDebugEnabled()) {
			logger.debug("Chat UserLoginUpHandler unloginHandler!!!!! userId=" + userId);
		}
		String lt = message.getLanguageType();
		int ut = message.getUserType();

		UserType userType = UserType.valueOf(ut);
		if (userType == null) {
			// TODO 返回错误信息
			getErrorMService().sendErrorMessage(ErrorType.ParamError, HandleErrorType.ParameterError.getValue(), 0,
					session);
			return;
		}

		LanguageType languageType = LanguageType.getLanguageType(lt);
		if (languageType == null) {
			// TODO 返回错误信息
			getErrorMService().sendErrorMessage(ErrorType.ParamError, HandleErrorType.ParameterError.getValue(), 0,
					session);
			return;
		}
		IPlayer player = service.createPlayer(session, userId, userType);
		// 设置语言类型
		player.setAttach(PlayerAttachType.LanguageType, languageType);

		// 登录
		service.loginInit(player);
		String extend = languageType.getIsoCode() + ":" + userType.ordinal();
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
		messageConfig.setKey(UserLoginUp.MessageEnum.CommondId_VALUE);
		messageConfig.setMessage(UserLoginUp.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}
