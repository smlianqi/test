package com.elex.common.util.graphtheory;

/**
 * 边（线段的边）
 * 
 * @author mausmars
 * 
 */
public interface IEdge {
	public enum Direction {
		V0TOV1(1), // 0->1
		V1TOV0(2), // 1->0
		BOTHWAY(3), // 0<->1
		;
		int value;

		Direction(int value) {
			this.value = value;
		}

		public int value() {
			return value;
		}
	}

	/**
	 * 获得边的2个节点
	 * 
	 * @return
	 */
	IVertex[] vertexs();

	/**
	 * 权重
	 * 
	 * @return
	 */
	int weight();

	/**
	 * 方向
	 * 
	 * @return
	 */
	Direction direction();

	/**
	 * 端点对应的另一个端点
	 * 
	 * @return
	 */
	IVertex next(IVertex v);

	/**
	 * 端点到另一个端点
	 * 
	 * @return
	 */
	IVertex link(IVertex v) throws GrapException;
}
