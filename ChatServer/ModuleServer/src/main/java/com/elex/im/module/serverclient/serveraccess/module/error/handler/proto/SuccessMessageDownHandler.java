package com.elex.im.module.serverclient.serveraccess.module.error.handler.proto;

import com.elex.common.component.player.IPlayer;
import com.elex.common.net.message.MessageConfig;
import com.elex.im.message.proto.ErrorMessage.SuccessMessageDown;
import com.elex.im.module.serverclient.serveraccess.IChatClientGameHandler;
import com.elex.im.module.serverclient.serveraccess.service.IProtoReqCallBack;
import com.elex.im.module.serverclient.serveraccess.service.IReqCallBack;

public class SuccessMessageDownHandler extends IChatClientGameHandler<SuccessMessageDown> {
	public SuccessMessageDownHandler(IReqCallBack callBack) {
		super(callBack);
	}

	@Override
	public void loginedHandler(SuccessMessageDown message, IPlayer player) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> SuccessMessageDownHandler!!! ");
		}
		if (callBack != null) {
			IProtoReqCallBack cb = (IProtoReqCallBack) callBack;
			cb.successMessageClassBack(player, message);
		}
	}

	public MessageConfig createMessageConfig() {
		MessageConfig messageConfig = new MessageConfig();
		messageConfig.setKey(SuccessMessageDown.MessageEnum.CommondId_VALUE);
		messageConfig.setMessage(SuccessMessageDown.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}
