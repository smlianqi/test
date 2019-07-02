package com.elex.im.module.serverclient.imitateclient.module.usertestconfig;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserTestConfigManager implements IUserTestConfigManager {
	private ConcurrentHashMap<String, UserTestConfig> userTestConfigMap = new ConcurrentHashMap<>();
	private Map<Long, String> nameMap = new ConcurrentHashMap<>();

	@Override
	public void insert(UserTestConfig userTestConfig) {
		userTestConfigMap.put(userTestConfig.getName(), userTestConfig);
	}

	@Override
	public UserTestConfig getUserTestConfig(String name) {
		return userTestConfigMap.get(name);
	}

	@Override
	public void binding(long userId, String name) {
		nameMap.put(userId, name);
	}

	@Override
	public Collection<UserTestConfig> getUserTestConfigs() {
		return userTestConfigMap.values();
	}

	@Override
	public UserTestConfig getUserTestConfig(long userId) {
		String name = nameMap.get(userId);
		if (name == null) {
			return null;
		}
		return userTestConfigMap.get(name);
	}
}
