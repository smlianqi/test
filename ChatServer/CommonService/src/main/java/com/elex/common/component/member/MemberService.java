package com.elex.common.component.member;

import java.util.List;

import com.elex.common.component.function.IFunctionCluster;
import com.elex.common.component.function.IFunctionService;
import com.elex.common.component.function.info.IFunctionInfo;
import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.ignite.IIgniteService;
import com.elex.common.component.member.config.ScMember;
import com.elex.common.component.member.data.IMemberOnlineDao;
import com.elex.common.component.member.data.MemberOnline;
import com.elex.common.component.member.type.MemberType;
import com.elex.common.service.AbstractService;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.config.IFunctionServiceConfig;
import com.elex.common.service.config.IServiceConfig;
import com.elex.common.service.type.ServiceType;

/**
 * 成员职位服务
 * 
 * @author mausmars
 *
 */
public class MemberService extends AbstractService<ScMember> implements IMemberService {
	private IMemberOnlineDao memberOnlineDao;

	private IFunctionService functionService;
	private IIgniteService igniteService;

	private DaoFactory daoFactory = new DaoFactory();

	public MemberService(IServiceConfig serviceConfig, IGlobalContext context) {
		super(serviceConfig, context);
	}

	@Override
	public void initService() throws Exception {
		ScMember c = getSConfig();

		// 依赖服务
		{
			List<String> sids = c.getDependIdsMap().get(ServiceType.ignite.name());
			igniteService = getServiceManager().getService(ServiceType.ignite, sids.get(0));
		}
		{
			List<String> sids = c.getDependIdsMap().get(ServiceType.function.name());
			functionService = getServiceManager().getService(ServiceType.function, sids.get(0));
		}
	}

	@Override
	public void startupService() {
		igniteService.startup();
		functionService.startup();

		this.memberOnlineDao = daoFactory.createMemberOnlineDao(igniteService);
	}

	@Override
	public void shutdownService() {
	}

	@Override
	public MemberOnline loginLocalServer(String memberId, MemberType memberType, String extend) {
		IFunctionServiceConfig fsc = functionService.getFunctionServiceConfig();
		String sid = functionService.getCurrentServerFid();

		MemberOnline entity = new MemberOnline();
		entity.setMemberId(memberId);
		entity.setFunctionType(fsc.getFunctionType().name());
		entity.setMemberType(memberType.name());
		entity.setSid(sid);
		entity.setLoginTime(System.currentTimeMillis());
		entity.setExtend(extend);
		// 插入
		memberOnlineDao.insert(entity, null);

		if (logger.isDebugEnabled()) {
			logger.debug("定位绑定 信息=" + entity);
		}
		return entity;
	}

	@Override
	public void logoutLocalServer(String memberId) {
		IFunctionServiceConfig fsc = functionService.getFunctionServiceConfig();

		MemberOnline entity = getMemberOnlineByMemberIdAndFunctionType(memberId, fsc.getFunctionType().name());
		memberOnlineDao.remove(entity, null);
	}

	@Override
	public IFunctionInfo getFunctionInfoByMemberId(String memberId, FunctionType functionType) {
		MemberOnline userOnline = memberOnlineDao.selectByMemberIdAndFunctionType(memberId, functionType.name());
		if (userOnline == null) {
			// 定位失败，没有成员定位信息
			logger.error("MemberOnline == null! memberId=" + memberId + " | functionType=" + functionType);
			return null;
		}
		IFunctionServiceConfig fsc = functionService.getFunctionServiceConfig();
		IFunctionCluster functionCluster = functionService.getFunctionCluster(fsc.getGroupId(), fsc.getRegionId(),
				functionType);
		// 定位用户位置发送消息
		IFunctionInfo functionInfo = functionCluster.getFunctionInfo(userOnline.getSid());
		return functionInfo;
	}

	@Override
	public MemberOnline getMemberOnlineByMemberIdAndFunctionType(String memberId, String functionType) {
		return memberOnlineDao.selectByMemberIdAndFunctionType(memberId, functionType);
	}

	// @Override
	// public List<MemberOnline> getMemberOnlineByMemberId(String memberId) {
	// return memberOnlineDao.selectByMemberId(memberId);
	// }

	@Override
	public List<MemberOnline> getMemberOnlineBySidAndMemberTypeAndFunctionType(String sid, String memberType, String functionType) {
		return memberOnlineDao.selectBySidAndMemberTypeAndFunctionType(sid, memberType, functionType);
	}

	// @Override
	// public List<MemberOnline> getMemberOnlineBySid(String sid) {
	// return memberOnlineDao.selectBySid(sid);
	// }
}
