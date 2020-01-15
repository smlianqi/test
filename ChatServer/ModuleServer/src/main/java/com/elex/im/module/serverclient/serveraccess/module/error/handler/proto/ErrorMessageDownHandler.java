package com.elex.im.module.serverclient.serveraccess.module.error.handler.proto;

import com.elex.common.component.player.IPlayer;
import com.elex.common.net.message.MessageConfig;
import com.elex.im.message.proto.ErrorMessage.ErrorMessageDown;
import com.elex.im.module.serverclient.serveraccess.IChatClientGameHandler;
import com.elex.im.module.serverclient.serveraccess.service.IProtoReqCallBack;
import com.elex.im.module.serverclient.serveraccess.service.IReqCallBack;

public class ErrorMessageDownHandler extends IChatClientGameHandler<ErrorMessageDown> {
	public ErrorMessageDownHandler(IReqCallBack callBack) {
		super(callBack);
	}

	@Override
	public void loginedHandler(ErrorMessageDown message, IPlayer player) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> ErrorMessageDownHandler!!! ");
		}
		if (callBack != null) {
			IProtoReqCallBack cb = (IProtoReqCallBack) callBack;
			cb.errorMessageClassBack(player, message);
		}
	}

	public MessageConfig createMessageConfig() {
		MessageConfig messageConfig = new MessageConfig();
		messageConfig.setKey(ErrorMessageDown.MessageEnum.CommondId_VALUE);
		messageConfig.setMessage(ErrorMessageDown.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}
