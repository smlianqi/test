package com.elex.common.component.timeout;

import com.elex.common.net.IConnectCallBack;
import com.elex.common.net.session.ISession;
import com.elex.common.net.type.SessionAttachType;

/**
 * 网络连接回调处理
 * 
 * @author mausmars
 *
 */
public class NetConnectCallBack implements IConnectCallBack {
	private ITimeoutManager dataTimeoutTask;

	@Override
	public void connectExcute(ISession session) {
		// 将数据超时管理器放入session
		session.setAttach(SessionAttachType.DataTimeoutManager, dataTimeoutTask);

		// sessionId为key
		dataTimeoutTask.insertTimeoutCheck(String.valueOf(session.getSessionId()), session, new ITimeoutCallBlack() {
			@Override
			public void callblack(Object obj) {
				ISession s = (ISession) obj;
				// 如果session超时就
				s.close();
			}
		});
	}

	// ------------------------------------------------
	public void setDataTimeoutTask(ITimeoutManager dataTimeoutTask) {
		this.dataTimeoutTask = dataTimeoutTask;
	}
}
