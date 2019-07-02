package com.elex.im.module.common.user.handler.proto;

import java.util.ArrayList;
import java.util.List;

import com.elex.common.net.message.MessageConfig;
import com.elex.im.module.common.user.IUserMService;

/**
 * 
 */
public class UserHandlerConfig {
	public List<MessageConfig> createHandler(IUserMService service) {
		// 消息配置
		List<MessageConfig> messageConfigs = new ArrayList<MessageConfig>();

		// 上行处理
		{
			UserLoginUpHandler upHandler = new UserLoginUpHandler(service);
			messageConfigs.add(upHandler.createMessageConfig());
		}
		{
			UserLogoutUpHandler upHandler = new UserLogoutUpHandler(service);
			messageConfigs.add(upHandler.createMessageConfig());
		}
		{
			ModifyLanguageUpHandler upHandler = new ModifyLanguageUpHandler(service);
			messageConfigs.add(upHandler.createMessageConfig());
		}
		// 下行处理（只是为了注册消息）
		{
			UserLoginDownHandler downHandler = new UserLoginDownHandler(service);
			messageConfigs.add(downHandler.createMessageConfig());
		}
		return messageConfigs;
	}
}
