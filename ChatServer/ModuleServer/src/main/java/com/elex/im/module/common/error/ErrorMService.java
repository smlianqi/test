package com.elex.im.module.common.error;

import com.elex.common.component.player.IPlayer;
import com.elex.common.net.message.IMessage;
import com.elex.common.net.service.netty.session.SessionBox;
import com.elex.common.net.session.ISession;
import com.elex.common.net.type.SessionAttachType;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.module.AModuleService;
import com.elex.common.service.module.ModuleServiceType;
import com.elex.im.message.AModuleMessageCreater;

/**
 * 内部逻辑服务
 * 
 * @author mausmars
 *
 */
public class ErrorMService extends AModuleService implements IErrorMService {
	public ErrorMService(IGlobalContext context) {
		super(context);
	}

	@Override
	public ModuleServiceType getModuleServiceType() {
		return ModuleServiceType.Error;
	}

	@Override
	public void init() {
	}

	@Override
	public void sendErrorMessage(ErrorType errorType, int errorDetail, int commandId, IPlayer player) {
		SessionBox sessionBox = player.getAttachFromSession(SessionAttachType.SessionBox);
		AModuleMessageCreater moduleMessageCreater = sessionBox.getModuleMessageCreater();

		IMessage msg = moduleMessageCreater.createErrorMessageDown(player.getUserId(), commandId, errorDetail,
				errorType.getType());
		player.send(msg);
	}

	@Override
	public void sendErrorMessage(ErrorType errorType, int errorDetail, int commandId, ISession session) {
		SessionBox sessionBox = session.getAttach(SessionAttachType.SessionBox);
		AModuleMessageCreater moduleMessageCreater = sessionBox.getModuleMessageCreater();

		IMessage msg = moduleMessageCreater.createErrorMessageDown(null, commandId, errorDetail, errorType.getType());
		session.send(msg);
	}

	@Override
	public void sendSuccessMessage(int commandId, IPlayer player) {
		SessionBox sessionBox = player.getAttachFromSession(SessionAttachType.SessionBox);
		AModuleMessageCreater moduleMessageCreater = sessionBox.getModuleMessageCreater();

		IMessage msg = moduleMessageCreater.createSuccessMessageDown(player.getUserId(), commandId);
		player.send(msg);
	}

	@Override
	public void sendSuccessMessage(int commandId, ISession session) {
		SessionBox sessionBox = session.getAttach(SessionAttachType.SessionBox);
		AModuleMessageCreater moduleMessageCreater = sessionBox.getModuleMessageCreater();

		IMessage msg = moduleMessageCreater.createSuccessMessageDown(null, commandId);
		session.send(msg);
	}
}
