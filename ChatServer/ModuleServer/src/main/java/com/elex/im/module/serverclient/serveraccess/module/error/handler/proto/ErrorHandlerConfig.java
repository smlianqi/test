package com.elex.im.module.serverclient.serveraccess.module.error.handler.proto;

import java.util.ArrayList;
import java.util.List;

import com.elex.common.net.message.MessageConfig;
import com.elex.im.module.serverclient.serveraccess.service.IReqCallBack;

/**
 * 
 */
public class ErrorHandlerConfig {
	public List<MessageConfig> createHandler(IReqCallBack callBack) {
		// 消息配置
		List<MessageConfig> messageConfigs = new ArrayList<MessageConfig>();

		// 上行处理

		// 下行处理（只是为了注册消息）
		ErrorMessageDownHandler errorMessageDownHandler = new ErrorMessageDownHandler(callBack);
		messageConfigs.add(errorMessageDownHandler.createMessageConfig());

		SuccessMessageDownHandler successMessageDownHandler = new SuccessMessageDownHandler(callBack);
		messageConfigs.add(successMessageDownHandler.createMessageConfig());

		return messageConfigs;
	}
}
