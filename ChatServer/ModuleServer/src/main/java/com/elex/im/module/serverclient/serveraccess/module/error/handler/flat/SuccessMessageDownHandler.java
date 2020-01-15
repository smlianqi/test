package com.elex.im.module.serverclient.serveraccess.module.error.handler.flat;

import com.elex.common.component.player.IPlayer;
import com.elex.common.net.message.MessageConfig;
import com.elex.im.message.flat.CommondId_Error;
import com.elex.im.message.flat.SuccessMessageDown;
import com.elex.im.module.serverclient.serveraccess.IChatClientGameHandler;
import com.elex.im.module.serverclient.serveraccess.service.IFlatReqCallBack;
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
			IFlatReqCallBack cb = (IFlatReqCallBack) callBack;
			cb.successMessageClassBack(player, message);
		}
	}

	public MessageConfig createMessageConfig() {
		MessageConfig messageConfig = new MessageConfig();
		// messageConfig.setCommonId(10002);
		messageConfig.setKey(CommondId_Error.SuccessMessageDown);
		messageConfig.setMessage(SuccessMessageDown.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}
