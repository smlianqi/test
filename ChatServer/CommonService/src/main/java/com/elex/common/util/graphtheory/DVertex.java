package com.elex.common.util.graphtheory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.elex.common.util.graphtheory.IEdge.Direction;

/**
 * 端点（链接边）
 * 
 * @author mausmars
 * 
 */
public class DVertex implements IVertex {
	// 链接的端点
	protected Set<IVertex> linkVertexs;

	// 链接的边
	protected Map<IEdge, IEdge> edges;

	// key
	protected int key;

	public DVertex(int key) {
		this.key = key;
		edges = new HashMap<IEdge, IEdge>();
		linkVertexs = new HashSet<IVertex>();
	}

	@Override
	public int key() {
		return key;
	}

	/**
	 * 结点通过边链接
	 * 
	 * @param edge
	 */
	boolean addEdges(IEdge edge) {
		if (edges.containsKey(edge)) {
			IEdge e = edges.get(edge);
			if (e.direction() == Direction.BOTHWAY || e.direction() == edge.direction()) {
				return false;
			}
		}
		edges.put(edge, edge);
		return true;
	}

	/**
	 * 结点直接链接
	 * 
	 * @param edge
	 */
	boolean addLinkVertex(DVertex v1, Direction direction) {
		if (this == v1) {
			return false;
		}
		if ((direction.value() & Direction.V0TOV1.value()) == Direction.V0TOV1.value()) {
			this.linkVertexs.add(v1);
		}
		if ((direction.value() & Direction.V1TOV0.value()) == Direction.V1TOV0.value()) {
			v1.linkVertexs.add(this);
		}
		return true;
	}

	boolean addLinkVertex(DVertex v1) {
		return addLinkVertex(v1, Direction.BOTHWAY);
	}

	@Override
	public boolean link(IVertex v) {
		if (linkVertexs.contains(v)) {
			return true;
		}
		for (IEdge edge : edges.values()) {
			IVertex tv = edge.link(this);
			if (tv != null && tv == v) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "DVertex [key=" + key + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + key;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DVertex other = (DVertex) obj;
		if (key != other.key)
			return false;
		return true;
	}
}
