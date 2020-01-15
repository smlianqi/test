package com.elex.common.service.module;

import java.util.concurrent.ConcurrentHashMap;

import com.elex.common.component.player.IPlayer;
import com.elex.common.component.player.IPlayerInitOffline;

/**
 * 逻辑模块服务管理器
 * 
 * @author mausmars
 *
 */
public class ModuleServiceMgr implements IModuleServiceMgr {
	// {服务类型,{sid，service对象}}
	private ConcurrentHashMap<ModuleServiceType, IModuleService> serviceMap = new ConcurrentHashMap<>();

	public ModuleServiceMgr() {
	}

	@Override
	public void initMService() {
		for (IModuleService moduleService : serviceMap.values()) {
			moduleService.init();
		}
	}

	@Override
	public <T extends IModuleService> T getService(ModuleServiceType serviceType) {
		return (T) serviceMap.get(serviceType);
	}

	@Override
	public <T extends IModuleService> T insertService(IModuleService service) {
		// 只要一个不要被覆盖
		IModuleService moduleService = serviceMap.putIfAbsent(service.getModuleServiceType(), service);
		if (moduleService == null) {
			moduleService = service;
		}
		return (T) moduleService;
	}

	@Override
	public void registerInit(long userId) {
		for (IModuleService logicMService : serviceMap.values()) {
			if (logicMService instanceof IPlayerInitOffline) {
				((IPlayerInitOffline) logicMService).registerInit(userId);
			}
		}
	}

	@Override
	public void loginInit(IPlayer player) {
		for (IModuleService logicMService : serviceMap.values()) {
			if (logicMService instanceof IPlayerInitOffline) {
				((IPlayerInitOffline) logicMService).loginInit(player);
			}
		}
	}

	@Override
	public void offline(IPlayer player, long time) {
		for (IModuleService logicMService : serviceMap.values()) {
			if (logicMService instanceof IPlayerInitOffline) {
				((IPlayerInitOffline) logicMService).offline(player, time);
			}
		}
	}

	@Override
	public void uploadData(IPlayer player) {
		for (IModuleService logicMService : serviceMap.values()) {
			if (logicMService instanceof IPlayerInitOffline) {
				((IPlayerInitOffline) logicMService).uploadData(player);
			}
		}
	}
}
