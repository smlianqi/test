package com.elex.im.module.common.error;

import com.elex.common.component.player.IPlayer;
import com.elex.common.net.session.ISession;
import com.elex.common.service.module.IModuleService;

public interface IErrorMService extends IModuleService {
	/**
	 * 发送错误消息
	 * 
	 * @param errorType
	 * @param errorDetail
	 * @param commandId
	 * @param player
	 */
	void sendErrorMessage(ErrorType errorType, int errorDetail, int commandId, IPlayer player);

	void sendErrorMessage(ErrorType errorType, int errorDetail, int commandId, ISession session);

	/**
	 * 统一回复成功指令
	 * 
	 * @param errorType
	 * @param errorDetail
	 * @param commandId
	 * @param player
	 */
	void sendSuccessMessage(int commandId, IPlayer player);

	void sendSuccessMessage(int commandId, ISession session);
}
