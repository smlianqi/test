package com.elex.im.module.common.user;

import com.elex.common.net.message.MessageConfig;
import com.elex.common.service.module.ModuleServiceType;
import com.elex.im.module.common.IGameHandler;
import com.elex.im.module.common.error.IErrorMService;

public abstract class UserMessageHandler<T> extends IGameHandler<T> {
	protected IUserMService service;

	public UserMessageHandler(IUserMService service) {
		this.service = service;
	}

	public abstract MessageConfig createMessageConfig();

	public IErrorMService getErrorMService() {
		return service.getModuleService(ModuleServiceType.Error);
	}
}
