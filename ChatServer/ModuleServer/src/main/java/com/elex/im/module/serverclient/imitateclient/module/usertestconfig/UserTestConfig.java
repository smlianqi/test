package com.elex.im.module.serverclient.imitateclient.module.usertestconfig;

import java.util.HashMap;
import java.util.Map;

import com.elex.im.module.serverclient.imitateclient.module.flowchart.FlowChartType;

/**
 * 用户测试配置
 * 
 * @author mausmars
 *
 */
public class UserTestConfig {
	private long userId;
	private String name;// 名字
	private String rid;// 区id
	private FlowChartType flowChartType;// 流程类型

	private Map<String, String> attach = new HashMap<>();

	public String getAttach(String key) {
		return attach.get(key);
	}

	public void putAttach(String key, String value) {
		attach.put(key, value);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public FlowChartType getFlowChartType() {
		return flowChartType;
	}

	public void setFlowChartType(FlowChartType flowChartType) {
		this.flowChartType = flowChartType;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}
