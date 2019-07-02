package com.elex.common.component.function;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FunctionNode {
	protected String key; // key

	protected volatile FunctionNode parent;// 父节点
	protected ConcurrentMap<String, FunctionNode> children = new ConcurrentHashMap<>();// 子节点
	// ----------------------------------------

	public FunctionNode(String key) {
		this.key = key;
	}

	// ----------------------------------------
	/** 移除节点本身 */
	public void remove() {
		if (parent == null) {
			return;
		}
		this.parent.getChildren().remove(this.key);
	}

	/** 移除子节点 */
	public FunctionNode removeChild(String key) {
		return children.remove(key);
	}

	/** 插入子节点 */
	public FunctionNode insertChild(FunctionNode child) {
		FunctionNode n = children.putIfAbsent(child.getKey(), child);
		if (n == null) {
			n = child;
			// 添加父节点
			child.setParent(this);
		}
		return n;
	}

	/** 获取父节点 */
	public FunctionNode getParent() {
		return this.parent;
	}

	/** 查询子节点 */
	public FunctionNode selectChild(String key) {
		return children.get(key);
	}

	/** 得到根节点 */
	public FunctionNode getRoot() {
		FunctionNode n = this;
		for (;;) {
			if (n.getParent() == null) {
				break;
			}
			n = n.getParent();
		}
		return n;
	}

	/** 是否包含子节点 */
	public boolean containsChild(String key) {
		return children.containsKey(key);
	}

	/** 得到全部子节点 */
	public Collection<FunctionNode> getAllChildren() {
		return children.values();
	}

	/** 得到节点全路径 */
	public String getFullPath() {
		List<FunctionNode> nodes = getFullNode();

		StringBuilder sb = new StringBuilder();
		for (FunctionNode node : nodes) {
			sb.append("/");
			sb.append(node.getKey());
		}
		return sb.toString();
	}

	/** 得到节点全路径 */
	public List<FunctionNode> getFullNode() {
		LinkedList<FunctionNode> nodes = new LinkedList<>();
		FunctionNode n = this;
		for (;;) {
			nodes.addFirst(n);
			if (n.getParent() == null) {
				break;
			}
			n = n.getParent();
		}
		return nodes;
	}

	/** 在现有的节点上添加 */
	public FunctionNode createNewRoot(String fullPath) {
		String[] paths = fullPath.split("/");

		FunctionNode n = this.getRoot();
		boolean isMatchRoot = false;
		for (String path : paths) {
			if (path.isEmpty()) {
				continue;
			}
			if (!isMatchRoot) {
				if (!path.equals(n.getKey())) {
					return null;
				} else {
					isMatchRoot = true;
				}
			} else {
				FunctionNode node = new FunctionNode(path);
				n.insertChild(node);
				n = node;
			}
		}
		return n;
	}

	/** 创建新的目录结构 */
	public static FunctionNode createRoot(String fullPath) {
		String[] paths = fullPath.split("/");

		FunctionNode n = null;
		for (String path : paths) {
			if (path.isEmpty()) {
				continue;
			}
			if (n == null) {
				n = new FunctionNode(path);
			} else {
				FunctionNode node = new FunctionNode(path);
				n.insertChild(node);
				n = node;
			}
		}
		return n;
	}

	/** 获得层数 */
	public int getLevel() {
		int level = 1;
		FunctionNode n = this;
		for (;;) {
			if (n.getParent() == null) {
				break;
			}
			level++;
			n = n.getParent();
		}
		return level;
	}

	public String getKey() {
		return key;
	}

	void setParent(FunctionNode parent) {
		this.parent = parent;
	}

	public ConcurrentMap<String, FunctionNode> getChildren() {
		return children;
	}

	public void print() {
		System.out.println("=====================");
		StringBuilder sb = new StringBuilder();
		print(this, sb);
		System.out.println(sb.toString());
	}

	private void print(FunctionNode n, StringBuilder sb) {
		sb.append(n);
		sb.append("(");
		ConcurrentMap<String, FunctionNode> children = n.getChildren();
		for (FunctionNode child : children.values()) {
			sb.append(child);
			sb.append(",");
		}
		sb.append(")");
		sb.append("\r\n");

		for (FunctionNode child : children.values()) {
			print(child, sb);
		}
	}

	@Override
	public String toString() {
		return "FunctionNode [key=" + key + "]";
	}

	// ----------------------------------------
	public static void main(String[] args) {
		FunctionNode root = new FunctionNode("root");

		FunctionNode level1_1 = root.insertChild(new FunctionNode("level1_1"));
		FunctionNode level1_2 = root.insertChild(new FunctionNode("level1_2"));
		FunctionNode level1_3 = root.insertChild(new FunctionNode("level1_3"));

		FunctionNode level1_1_1 = level1_1.insertChild(new FunctionNode("level1_1_1"));
		FunctionNode level1_1_2 = level1_1.insertChild(new FunctionNode("level1_1_2"));
		FunctionNode level1_1_3 = level1_1.insertChild(new FunctionNode("level1_1_3"));

		FunctionNode level1_2_1 = level1_2.insertChild(new FunctionNode("level1_2_1"));
		FunctionNode level1_2_2 = level1_2.insertChild(new FunctionNode("level1_2_2"));
		FunctionNode level1_2_3 = level1_2.insertChild(new FunctionNode("level1_2_3"));
		root.print();

		// 获取根
		FunctionNode r = level1_2_1.getRoot();
		r.print();

		// 移除
		level1_1.remove();
		root.print();

		// 获取路径
		System.out.println(level1_2_3.getFullPath());

		// 创建目录
		FunctionNode n1 = FunctionNode.createRoot("/root/1/2/3");
		System.out.println(n1.getFullPath());

		FunctionNode n2 = n1.createNewRoot("/root/a/b/c");
		System.out.println(n2.getFullPath());
		System.out.println(n2.getLevel());
		FunctionNode r1 = n2.getRoot();
		r1.print();
	}
}
