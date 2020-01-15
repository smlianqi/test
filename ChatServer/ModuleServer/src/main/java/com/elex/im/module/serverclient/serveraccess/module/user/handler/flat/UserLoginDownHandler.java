package com.elex.im.module.serverclient.serveraccess.module.user.handler.flat;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.player.IPlayer;
import com.elex.common.component.player.type.UserType;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.session.ISession;
import com.elex.im.message.flat.CommondId_User;
import com.elex.im.message.flat.UserLoginDown;
import com.elex.im.module.common.user.UserMService;
import com.elex.im.module.serverclient.serveraccess.IChatClientGameHandler;
import com.elex.im.module.serverclient.serveraccess.service.IFlatReqCallBack;
import com.elex.im.module.serverclient.serveraccess.service.IReqCallBack;

/**
 * 服务器返回登录状态
 * 
 * @author mausmars
 *
 */
public class UserLoginDownHandler extends IChatClientGameHandler<UserLoginDown> {
	public UserLoginDownHandler(IReqCallBack callBack) {
		super(callBack);
	}

	private UserMService service;

	@Override
	public void unloginHandler(UserLoginDown message, ISession session, long userId) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> UserLoginDownHandler unloginHandler!!!!! userId=" + userId + " message=" + message);
		}
		UserType userType = UserType.valueOf((int) message.userType());
		if (userType == null) {
			logger.error("UserLoginUpHandler userType==null!!! userId=" + userId);
			return;
		}
		IPlayer player = service.createPlayer(session, userId, userType);

		// 登录回调
		if (callBack != null) {
			IFlatReqCallBack cb = (IFlatReqCallBack) callBack;
			cb.userLoginClassBack(player, message);
		}
	}

	@Override
	public void loginedHandler(UserLoginDown message, IPlayer player) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> UserLoginDownHandler loginedHandler!!!!! userId=" + player.getUserId() + " message="
					+ message);
		}
		// 登录回调
		if (callBack != null) {
			IFlatReqCallBack cb = (IFlatReqCallBack) callBack;
			cb.userLoginClassBack(player, message);
		}
	}

	@Override
	public FunctionType getFunctionType() {
		return FunctionType.gate;
	}

	public MessageConfig createMessageConfig() {
		MessageConfig messageConfig = new MessageConfig();
		// messageConfig.setCommonId(10002);
		messageConfig.setKey(CommondId_User.UserLoginDown);
		messageConfig.setMessage(UserLoginDown.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}

	// ---------------------------------------
	public void setService(UserMService service) {
		this.service = service;
	}
}
