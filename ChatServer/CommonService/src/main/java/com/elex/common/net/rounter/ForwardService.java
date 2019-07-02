package com.elex.common.net.rounter;

import com.elex.common.component.function.IFunctionService;
import com.elex.common.component.function.info.FunctionId;
import com.elex.common.component.function.info.IFunctionInfo;
import com.elex.common.component.function.type.FServiceType;
import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.member.IMemberService;
import com.elex.common.component.net.server.INettyNetServerService;
import com.elex.common.net.service.netty.filter.tcp.NettyMessage;
import com.elex.common.net.service.netty.session.SessionBox;
import com.elex.common.net.session.ISession;
import com.elex.common.net.session.ISessionPool;
import com.elex.common.net.type.SessionAttachType;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

public class ForwardService implements IForwardService {
	protected static final ILogger logger = XLogUtil.logger();

	private IFunctionService functionService;// 为了查询s->s
	private IMessageRounter messageRounter;// 消息定位
	private INettyNetServerService client2server;// c->s

	private IMemberService memberService;

	// 转发消息
	public void forwardMsg(NettyMessage message, ISession session) {
		// 这里转发或处理
		int commandId = message.getCommandId();

		// 转发的功能服务上
		if (messageRounter == null) {
			if (logger.isDebugEnabled()) {
				printLog(null, null, message, "messageRounter==null");
			}
			return;
		}
		if (functionService == null) {
			if (logger.isDebugEnabled()) {
				printLog(null, null, message, "functionService==null");
			}
			return;
		}
		// 指令映射要转发的功能类型
		FunctionType functionType = messageRounter.forward(String.valueOf(commandId));
		if (functionType == null) {
			//一直在这里出去了
			if (logger.isDebugEnabled()) {
				printLog(null, null, message, "commandId no forward ,commandId=" + commandId);
			}
			return;
		}
		IFunctionInfo localFunctionInfo = functionService.getLocalFunctionInfo();
		if (localFunctionInfo == null) {
			if (logger.isDebugEnabled()) {
				printLog(null, null, message, "localFunctionInfo==null");
			}
			return;
		}
		if (localFunctionInfo.getFunctionId().getFunctionType() == functionType) {
			if (logger.isDebugEnabled()) {
				FunctionId functionId = localFunctionInfo.getFunctionId();
				// 当前服是类型就是目标类型
				printLog(functionId, functionType, message, "local_functionType==functionType");
			}
			return;
		}
		if (functionType == FunctionType.client) {
			// 转发给客户端
			if (client2server != null) {
				if (logger.isDebugEnabled()) {
					FunctionId functionId = localFunctionInfo.getFunctionId();
					// 打印日志
					printLog(functionId, functionType, message, null);
				}
				ISession s = client2server.getSessionManager().getSessionByUserId(message.getExtend());
				if (s != null) {
					s.send(message);
				} else {
					logger.error(">>> client2server NoForward! session==null! extend=" + message.getExtend()
							+ " message=" + message);
				}
			}
		} else {
			IFunctionInfo reverseFunctionInfo = null;
			long extendId = message.getExtend();

			if (extendId > 0) {
				// 定位用户功能服务
				reverseFunctionInfo = memberService.getFunctionInfoByMemberId(String.valueOf(extendId), functionType);
			}
			if (reverseFunctionInfo == null) {
				if (logger.isDebugEnabled()) {
					logger.debug(
							"定位失败 extendId=" + extendId + " functionType=" + functionType + " commandId=" + commandId);
				}
				// 如果为null，随机一个服务
				reverseFunctionInfo = functionService.getRandomFunctionInfo(functionType);
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug(
							"定位成功 extendId=" + extendId + " functionType=" + functionType + " commandId=" + commandId);
				}
			}
			if (reverseFunctionInfo == null) {
				// TODO 这里返回服务不可用
				if (logger.isDebugEnabled()) {
					// 打印日志
					printLog(null, functionType, message, "reverseFunctionInfo==null");
				}
				return;
			}
			// 转发到指定业务的服务器
			if (logger.isDebugEnabled()) {
				// 打印日志
				printLog(reverseFunctionInfo.getFunctionId(), functionType, message, null);
			}
			SessionBox sessionBox = session.getAttach(SessionAttachType.SessionBox);
			Long uid = sessionBox.getUserId();
			if (uid != null) {
				message.setExtend(uid);
			}
			ISessionPool sessionPool = reverseFunctionInfo.getService(FServiceType.NettyForward,
					FServiceType.NettyForward.name());
			if (message.getExtend() > 0) {
				// 按规则散列发送
				sessionPool.send(String.valueOf(message.getExtend()), message);
			} else {
				sessionBox = session.getAttach(SessionAttachType.SessionBox);
				Integer index = sessionBox.getSessionIndex();
				if (index == null) {
					// 这里如果没有用户信息没法散列发送
					sessionPool.send(message);
				} else {
					// 按规则散列发送
					sessionPool.send(index, message);
				}
			}
		}
	}

	private void printLog(FunctionId functionId, FunctionType functionType, NettyMessage message, String errorReason) {
		StringBuilder sb = new StringBuilder();
		sb.append("Forward message log!");
		if (errorReason != null) {
			sb.append(" NoForward! " + errorReason);
		}
		if (functionId != null && functionType != null) {
			sb.append(" [");
			sb.append(functionId.getFunctionType());
			sb.append("->");
			sb.append(functionType);
			sb.append("] ");
			sb.append(functionId.getGroupId());
			sb.append("_");
			sb.append(functionId.getRegionId());
		}
		sb.append(" commandId=");
		sb.append(message.getCommandId());
		if (logger.isDebugEnabled()) {
			logger.debug(sb.toString());
		}
	}

	// -------------------------------------------------
	public void setFunctionService(IFunctionService functionService) {
		this.functionService = functionService;
	}

	public void setMessageRounter(IMessageRounter messageRounter) {
		this.messageRounter = messageRounter;
	}

	public void setClient2server(INettyNetServerService client2server) {
		this.client2server = client2server;
	}

	public void setMemberService(IMemberService memberService) {
		this.memberService = memberService;
	}
}
