package com.elex.im.module.common.inner;

import com.elex.common.component.function.IFunctionService;
import com.elex.common.component.function.info.FService;
import com.elex.common.component.function.info.IFunctionInfo;
import com.elex.common.component.function.info.NettyForwardServiceInfo;
import com.elex.common.component.function.type.FServiceType;
import com.elex.common.component.net.server.INettyNetServerService;
import com.elex.common.component.player.IPlayer;
import com.elex.common.net.message.IMessage;
import com.elex.common.net.message.Message;
import com.elex.common.net.session.ISession;
import com.elex.common.net.session.ISessionManager;
import com.elex.common.service.GeneralConstant;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.module.AModuleService;
import com.elex.common.service.module.ModuleServiceType;
import com.elex.common.service.type.ServiceType;

import java.util.List;

/**
 * 内部逻辑服务
 * 
 * @author mausmars
 *
 */
public class InnerMService extends AModuleService implements IInnerMService {
	public InnerMService(IGlobalContext context) {
		super(context);
	}

	@Override
	public ModuleServiceType getModuleServiceType() {
		return ModuleServiceType.Inner;
	}

	@Override
	public void init() {
	}

	public boolean bindingSocket(String fid, int index, String token, ISession session) {
		IFunctionService functionService = context.getServiceManager().getService(ServiceType.function,
				GeneralConstant.DefaultFunctionServiceIdKey);

		IFunctionInfo serverFunctionInfo = functionService.getLocalFunctionInfo();
		FService serverFService = serverFunctionInfo.getFService(FServiceType.NettyForward);

		NettyForwardServiceInfo nettyForwardServiceInfo = serverFunctionInfo.getServiceInfo(FServiceType.NettyForward,
				FServiceType.NettyForward.name());
		if (!token.equals(nettyForwardServiceInfo.getToken())) {
			logger.error("BindingSocket Token check is fail!!! fid=" + fid);
			// 非法链接
			session.close();
			return false;
		}

		// if (logger.isDebugEnabled()) {
		// logger.debug(">>>>>>>>> " + fid);
		// }

		// 这个时候节点可能还没创建
		IFunctionInfo reverseFunctionInfo = functionService.getFunctionInfoAndCreatePrxNode(fid);

		FService reverseFService = reverseFunctionInfo.getFService(FServiceType.NettyForward);
		if (reverseFService == null) {
			reverseFService = serverFService.cloneObj();
			reverseFService = reverseFunctionInfo.putIfAbsentFService(reverseFService);
		}
		NettyForwardServiceInfo serviceInfo = reverseFService.getServiceInfo(FServiceType.NettyForward.name());
		// 绑定session
		serviceInfo.bindingSever(index, session);
		if (logger.isDebugEnabled()) {
			logger.debug("BindingSocket SessionPool binding success!!! fid=" + fid + ",index=" + index);
		}
		return true;
	}

	public void forwardWhole(int commandId, byte[] content, ISession session) {
		// ISessionManager sm = session.getAttach(SessionAttachType.SessionManager);
		INettyNetServerService c2s = context.getServiceManager().getService(ServiceType.netserver,
				GeneralConstant.NetClientServiceId_C2G);
		ISessionManager sm = c2s.getSessionManager();

		IMessage msg = Message.createBytesMessage(content, commandId);
		// 广播到全体用户
		sm.sendBroadcast2Players(msg);
	}

	public void forwardUsers(int commandId, byte[] content, List<String> uids, ISession session) {
		INettyNetServerService c2s = context.getServiceManager().getService(ServiceType.netserver,
				GeneralConstant.NetClientServiceId_C2G);
		ISessionManager sm = c2s.getSessionManager();

		// 这里不能从这个session取player，这个是
		// ISessionManager sm = session.getAttach(SessionAttachType.SessionManager);

		for (String uid : uids) {
			long userId = Long.parseLong(uid);
			IPlayer player = sm.getPlayer(userId);
			if (player == null) {
				if (logger.isDebugEnabled()) {
					logger.debug("BroadcastUpHandler player==null!!! userId=" + uid);
				}
				continue;
			}
			IMessage msg = Message.createBytesMessage(content, userId, commandId);
			player.send(msg);
		}
	}
}
