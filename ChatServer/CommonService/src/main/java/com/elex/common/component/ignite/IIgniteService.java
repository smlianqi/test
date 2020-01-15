package com.elex.common.component.ignite;

import org.apache.ignite.Ignite;

import com.elex.common.service.IService;

public interface IIgniteService extends IService {
	Ignite getIgnite();
}
