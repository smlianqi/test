package com.elex.im.module.serverclient.serveraccess.module.user.handler.proto;

import java.util.ArrayList;
import java.util.List;

import com.elex.common.net.message.MessageConfig;
import com.elex.im.module.common.user.UserMService;
import com.elex.im.module.common.user.handler.proto.UserLoginUpHandler;
import com.elex.im.module.common.user.handler.proto.UserLogoutUpHandler;
import com.elex.im.module.serverclient.serveraccess.service.IReqCallBack;

/**
 * 
 */
public class UserHandlerConfig {
	public List<MessageConfig> createHandler(UserMService userService, IReqCallBack callBack) {
		// 消息配置
		List<MessageConfig> messageConfigs = new ArrayList<MessageConfig>();

		// 下行处理
		UserLoginDownHandler userLoginDownHandler = new UserLoginDownHandler(callBack);
		userLoginDownHandler.setService(userService);
		messageConfigs.add(userLoginDownHandler.createMessageConfig());

		// 上行处理（只是为了注册消息）
		UserLoginUpHandler userLoginUpHandler = new UserLoginUpHandler(userService);
		messageConfigs.add(userLoginUpHandler.createMessageConfig());

		UserLogoutUpHandler userLogoutUpHandler = new UserLogoutUpHandler(userService);
		messageConfigs.add(userLogoutUpHandler.createMessageConfig());

		return messageConfigs;
	}
}
