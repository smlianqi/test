package com.elex.common.util.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 树节点
 * 
 * @author mausmars
 * 
 */
public class TreeNode {
	protected Object key; // key
	protected volatile int level; // 层

	protected volatile TreeNode parent;// 父节点
	protected ConcurrentMap<Object, TreeNode> children = new ConcurrentHashMap<>();// 子节点

	public TreeNode(Object key) {
		this.key = key;
	}

	/**
	 * 插入子节点
	 */
	public <T extends TreeNode> T insertChild(TreeNode node) {
		synchronized (this) {
			node.level = this.level + 1;
			node.parent = this;
		}
		TreeNode n = children.putIfAbsent(node.key, node);
		if (n == null) {
			n = node;
		}
		return (T) n;
	}

	/**
	 * 得到根节点
	 * 
	 * @return
	 */
	public TreeNode getRoot() {
		if (this.parent != null) {
			return this.parent.getRoot();
		} else {
			return this;
		}
	}

	/**
	 * 得到节点全路径
	 * 
	 * @return
	 */
	public String getFullPath(String separator) {
		StringBuilder sb = new StringBuilder();
		if (parent == null) {
			sb.append(separator);
			sb.append(key);
		} else {
			sb.append(parent.getFullPath(separator));
			sb.append(separator);
			sb.append(key);
		}
		return sb.toString();
	}

	/**
	 * 得到节点全路径
	 * 
	 * @return
	 */
	public List<String> getFullPath(int level) {
		List<String> paths = new ArrayList<String>(level);
		setPath(paths);
		return paths;
	}

	private void setPath(List<String> paths) {
		if (parent != null) {
			parent.setPath(paths);
		} else {
			paths.add("/");
		}
		paths.add(String.valueOf(key));
	}

	/**
	 * 移除节点本身
	 */
	public void remove() {
		if (parent == null) {
			return;
		}
		parent.children.remove(this.key);
	}

	/**
	 * 移除子节点
	 * 
	 * @param key
	 * @return
	 */
	public TreeNode removeChild(Object key) {
		return children.remove(key);
	}

	/**
	 * 查询子节点
	 * 
	 * @param key
	 * @return
	 */
	public TreeNode selectChild(Object key) {
		return children.get(key);
	}

	public boolean containsChild(Object key) {
		return children.containsKey(key);
	}

	/**
	 * 得到全部子节点
	 * 
	 * @return
	 */
	public Collection<TreeNode> getAllChildren() {
		return children.values();
	}

	/**
	 * 得到父节点
	 * 
	 * @return
	 */
	public TreeNode getParent() {
		return parent;
	}

	/**
	 * 得到节点key
	 * 
	 * @return
	 */
	public Object getKey() {
		return key;
	}

	/**
	 * 得到节点level
	 * 
	 * @return
	 */
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public String toString() {
		return "FileNode [key=" + key + ", level=" + level + ", parent=" + parent + ", children=" + children + "]";
	}
}
