package com.elex.im.module.serverclient.imitateclient.module.usertestconfig;

import java.util.Collection;

public interface IUserTestConfigManager {
	UserTestConfig getUserTestConfig(String name);

	UserTestConfig getUserTestConfig(long userId);

	void insert(UserTestConfig userTestConfig);

	Collection<UserTestConfig> getUserTestConfigs();

	void binding(long userId, String name);
}
