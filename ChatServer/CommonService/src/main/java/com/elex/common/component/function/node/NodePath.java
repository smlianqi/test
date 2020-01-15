package com.elex.common.component.function.node;

import com.elex.common.component.function.info.FunctionId;
import com.elex.common.component.function.type.NodeLevelType;
import com.elex.common.util.string.StringUtil;

/**
 * 节点路径
 * 
 * @author mausmars
 * 
 */
public class NodePath {
	/**
	 * [sms,gid,rid,ftype,id]
	 */
	private String[] paths;

	public NodePath(String path) {
		path = path.substring(1, path.length());
		this.paths = path.split(StringUtil.SeparatorSlash);
	}

	public NodePath(String[] paths) {
		this.paths = paths;
	}

	// ------------------------------
	public String getKey(int level) {
		if (level < 0 || level > paths.length - 1) {
			return null;
		}
		return paths[level];
	}

	public String getParentKey(int level) {
		return getKey(level - 1);
	}

	public String getChildKey(int level) {
		return getKey(level + 1);
	}

	public int getLevel() {
		return paths.length - 1;
	}

	public String getLastKey() {
		return paths[getLevel()];
	}

	public String[] getPaths() {
		return paths;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("NodePath [paths=");
		for (String path : paths) {
			sb.append(path);
			sb.append(" ");
		}
		sb.append("]");
		return sb.toString();
	}

	public void setId(FunctionId fid) {
		paths[NodeLevelType.Id.value()] = fid.getFid(true);
	}

	/**
	 * 根据root，fid生成路径数组
	 * 
	 * @param root
	 * @param fid
	 * @return
	 */
	public static NodePath createNodePath(Object module, FunctionId fid, boolean isContainAotuId) {
		String[] paths = new String[NodeLevelType.values().length];
		paths[NodeLevelType.Module.value()] = String.valueOf(module);
		paths[NodeLevelType.Group.value()] = fid.getGroupId();
		paths[NodeLevelType.Region.value()] = fid.getRegionId();
		paths[NodeLevelType.Type.value()] = fid.getFunctionType().name();
		paths[NodeLevelType.Id.value()] = fid.getFid(isContainAotuId);
		NodePath nodePath = new NodePath(paths);
		return nodePath;
	}

	/**
	 * 得到fkey一级的全路径
	 * 
	 * @param fid
	 * @return
	 */
	public static String getFKeyFullPath(String module, FunctionId fid, boolean isContainAotuId) {
		StringBuilder sb = new StringBuilder();
		sb.append(module);
		sb.append(StringUtil.SeparatorSlash);
		sb.append(fid.getGroupId());
		sb.append(StringUtil.SeparatorSlash);
		sb.append(fid.getRegionId());
		sb.append(StringUtil.SeparatorSlash);
		sb.append(fid.getFunctionType().name());
		sb.append(StringUtil.SeparatorSlash);
		sb.append(fid.getFid(isContainAotuId));
		return sb.toString();
	}

	/**
	 * 得到ftype一级的全路径
	 * 
	 * @param fid
	 * @return
	 */
	public static String getFTypeFullPath(String module, FunctionId fid) {
		StringBuilder sb = new StringBuilder();
		sb.append(module);
		sb.append(StringUtil.SeparatorSlash);
		sb.append(fid.getGroupId());
		sb.append(StringUtil.SeparatorSlash);
		sb.append(fid.getRegionId());
		sb.append(StringUtil.SeparatorSlash);
		sb.append(fid.getFunctionType().name());
		return sb.toString();
	}

}
