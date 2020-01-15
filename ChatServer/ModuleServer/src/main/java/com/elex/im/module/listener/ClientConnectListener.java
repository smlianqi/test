package com.elex.im.module.listener;

import com.elex.common.component.function.IFunctionService;
import com.elex.common.component.function.info.IFunctionInfo;
import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.member.IMemberService;
import com.elex.common.component.member.data.MemberOnline;
import com.elex.common.component.member.type.MemberType;
import com.elex.common.net.event.ReconnectEvent;
import com.elex.common.net.message.IMessage;
import com.elex.common.net.service.netty.session.ForwardSubSessionBox;
import com.elex.common.net.service.netty.session.SessionBox;
import com.elex.common.net.session.ISession;
import com.elex.common.net.session.ISessionPool;
import com.elex.common.net.type.SessionAttachType;
import com.elex.common.net.type.SessionType;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.config.IFunctionServiceConfig;
import com.elex.common.service.type.ServiceType;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;
import com.elex.im.message.AModuleMessageCreater;
import com.google.common.eventbus.Subscribe;

import java.util.List;

/**
 * 客户端连接重连监听
 * 
 * @author mausmars
 *
 */
public class ClientConnectListener implements IClientConnectListener {
	protected static final ILogger logger = XLogUtil.logger();
	private IGlobalContext context;

	public ClientConnectListener(IGlobalContext context) {
		this.context = context;
	}

	@Override
	@Subscribe
	public void listen(ReconnectEvent event) {
		if (logger.isDebugEnabled()) {
			logger.debug("EventListener ReconnectEvent event=" + event);
		}
		try {
			ISession session = event.getSession();

			SessionBox sessionBox = session.getAttach(SessionAttachType.SessionBox);

			SessionType sessionType = sessionBox.getSessionType();
			switch (sessionType) {
			case Forward:
				ForwardSubSessionBox forwardSessionBox = sessionBox.getSubSessionBox();
				IMessage msg = sessionBox.getMessageCreater().createBindingSocketUpMessage(forwardSessionBox.getSid(),
						forwardSessionBox.getToken(), forwardSessionBox.getIndex());
				// 重新绑定
				session.send(msg);

				IMemberService memberService = context.getServiceManager().getService(ServiceType.member, "common");
				IFunctionServiceConfig functionServiceConfig = memberService.getFunctionServiceConfig();
				if (functionServiceConfig.getFunctionType() == FunctionType.gate) {
					IFunctionService functionService = context.getServiceManager().getService(ServiceType.function,
							"common");
					IFunctionInfo functionInfo = functionService.getLocalFunctionInfo();
					String sid = functionInfo.getFunctionId().getFid(true);
					List<MemberOnline> memberOnlines = memberService.getMemberOnlineBySidAndMemberTypeAndFunctionType(
							sid, MemberType.user.name(), FunctionType.gate.name());

					for (MemberOnline memberOnline : memberOnlines) {
						ISessionPool sessionPool = forwardSessionBox.getSessionPool();
						int index = sessionPool.getIndex(memberOnline.getMemberId());
						if (index == forwardSessionBox.getIndex()) {
							// 重新登录
							String extend = memberOnline.getExtend();
							String[] strs = extend.split(":");

							AModuleMessageCreater moduleMessageCreater = sessionBox.getModuleMessageCreater();
							// TODO 根据extend解析出 语言和用户类型
							IMessage loginMsg = moduleMessageCreater.createUserLoginUp(
									Long.parseLong(memberOnline.getMemberId()), Integer.parseInt(strs[1]), strs[0]);
							session.send(loginMsg);
						}
					}
				}
				break;

			default:
				break;
			}
		} catch (Exception e) {
			logger.error("ClientConnectListener error!!!", e);
		}
	}
}
