package com.elex.common.util.graphtheory;

/**
 * 边（线段的边）
 * 
 * @author mausmars
 * 
 */
public class DEdge implements IEdge {
	// 端点v0,v1
	protected DVertex[] vertexs;
	// 方向
	protected Direction direction;
	// 权重
	protected int weight = 0;

	public DEdge(DVertex v0, DVertex v1) {
		this.direction = Direction.BOTHWAY;
		init(v0, v1);
	}

	public DEdge(DVertex v0, DVertex v1, Direction direction) {
		this.direction = direction;
		init(v0, v1);
	}

	private void init(DVertex v0, DVertex v1) {
		vertexs = new DVertex[2];
		vertexs[0] = v0;
		vertexs[1] = v1;
	}

	/**
	 * 关联端点和边。这个方法必须调用
	 */
	public void associations() {
		if (!(vertexs[0] instanceof DVertex)) {
			throw new GrapException("Vertex key=" + vertexs[0].key() + " type isn't VertexCEdge!");
		}
		if (!(vertexs[1] instanceof DVertex)) {
			throw new GrapException("Vertex key=" + vertexs[1].key() + " type isn't VertexCEdge!");
		}
		// 节点加入到边
		vertexs[0].addEdges(this);
		vertexs[1].addEdges(this);
	}

	@Override
	public IVertex[] vertexs() {
		return vertexs;
	}

	@Override
	public int weight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	@Override
	public Direction direction() {
		return direction;
	}

	@Override
	public IVertex next(IVertex v) {
		if (vertexs[0].key() == v.key()) {
			return vertexs[1];
		} else if (vertexs[1].key() == v.key()) {
			return vertexs[0];
		}
		return null;
	}

	@Override
	public IVertex link(IVertex v) throws GrapException {
		if (vertexs[0] == v) {
			if (direction == Direction.BOTHWAY || direction == Direction.V0TOV1) {
				return vertexs[1];
			} else {
				return null;
			}
		} else if (vertexs[1] == v) {
			if (direction == Direction.BOTHWAY || direction == Direction.V1TOV0) {
				return vertexs[0];
			} else {
				return null;
			}
		}
		throw new GrapException("Vertex key=" + v.key() + " is on the edge!");
	}

	@Override
	public String toString() {
		return "DEdge [v0=" + vertexs[0].key() + ",v1=" + vertexs[1].key() + ", direction=" + direction + ", weight=" + weight + "]";
	}

	@Override
	public int hashCode() {
		String key = "";
		if (vertexs[0].key() >= vertexs[1].key()) {
			key = vertexs[1].key() + "_" + vertexs[0].key();
		} else {
			key = vertexs[0].key() + "_" + vertexs[1].key();
		}
		final int prime = 31;
		int result = 1;
		result = prime * result + key.hashCode();
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
		DEdge other = (DEdge) obj;

		if (this.direction == Direction.BOTHWAY && other.direction == Direction.BOTHWAY) {
			// 如果是双联通，顺序和倒序都不同的，就不是一条线
			if ((this.vertexs[0].key() != other.vertexs[0].key() || this.vertexs[1].key() != other.vertexs[1].key())
					&& (this.vertexs[0].key() != other.vertexs[1].key() || this.vertexs[1].key() != other.vertexs[0].key()))
				return false;
		} else {
			if (this.direction == other.direction) {
				// 如果方向相同，只要相同顺序的值不同，就不是一条线
				if (this.vertexs[0].key() != other.vertexs[0].key() || this.vertexs[1].key() != other.vertexs[1].key())
					return false;
			}
		}
		return true;
	}

}
