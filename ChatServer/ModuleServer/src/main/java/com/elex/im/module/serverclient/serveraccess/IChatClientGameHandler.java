/**
 * 
 */
package com.elex.im.module.serverclient.serveraccess;

import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;
import com.elex.im.module.common.IGameHandler;
import com.elex.im.module.serverclient.serveraccess.service.IReqCallBack;

/**
 * @author mausmars
 * 
 */
public abstract class IChatClientGameHandler<T> extends IGameHandler<T> {
	protected static final ILogger logger = XLogUtil.logger();

	protected IReqCallBack callBack;

	public IChatClientGameHandler(IReqCallBack callBack) {
		this.callBack = callBack;
	}
}