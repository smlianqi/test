package com.elex.common.component.function.filter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.elex.common.service.GeneralConstant;

/**
 * 节点过滤器
 * 
 * @author mausmars
 * 
 * 
 */
public class NodeFilter implements INodeFilter {
	// 所需要的组节点列表
	private Set<String> groupFilters = new HashSet<String>();
	// 所需要的区节点列表
	private Set<String> regionFilters = new HashSet<String>();
	// 所需要的功能节点列表
	private Set<String> functionFilters = new HashSet<String>();

	public boolean isGroupFilterNode(String node) {
		if (groupFilters.contains(GeneralConstant.ALL)) {
			return true;
		}
		return groupFilters.contains(node);
	}

	@Override
	public boolean isRegionFilterNode(String node) {
		if (regionFilters.contains(GeneralConstant.ALL)) {
			return true;
		}
		return regionFilters.contains(node);
	}

	public boolean isFunctionFilterNode(String node) {
		if (functionFilters.contains(GeneralConstant.ALL)) {
			return true;
		}
		return functionFilters.contains(node);
	}

	// ------------------------------------
	public void setGroupFilterStr(List<String> groupIds) {
		groupFilters.addAll(groupIds);
	}

	public void setFunctionFilterStr(List<String> functionTypes) {
		functionFilters.addAll(functionTypes);
	}

	public void setRegionFilterStr(List<String> regionIds) {
		regionFilters.addAll(regionIds);
	}
}
