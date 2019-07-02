package com.elex.common.util.graphtheory;

import com.elex.common.util.graphtheory.IEdge.Direction;

/**
 * 图的工具类
 * 
 * @author mausmars
 * 
 */
public class GraphUtil {
	/**
	 * 通过边链接v0，v1；有方向
	 * 
	 * @param v0
	 * @param v1
	 * @param direction
	 */
	public static void linkEdge(DVertex v0, DVertex v1, Direction direction) {
		DEdge edge = new DEdge(v0, v1, direction);
		edge.associations();
	}

	/**
	 * 通过边链接v0，v1；默认双向
	 * 
	 * @param v0
	 * @param v1
	 * @param direction
	 */
	public static void linkEdge(DVertex v0, DVertex v1) {
		DEdge edge = new DEdge(v0, v1);
		edge.associations();
	}

	/**
	 * 直接链接节点；
	 * 
	 * @param v0
	 * @param v1
	 * @param direction
	 */
	public static void linkVertex(DVertex v0, DVertex v1, Direction direction) {
		v0.addLinkVertex(v1, direction);
	}

	/**
	 * 直接链接节点；
	 * 
	 * @param v0
	 * @param v1
	 * @param direction
	 */
	public static void linkVertex(DVertex v0, DVertex v1) {
		v0.addLinkVertex(v1);
	}

	public static boolean link(DVertex v0, DVertex v1) {
		return v0.link(v1);
	}

	public static void main(String args[]) {
		test1();
		test2();
		test3();
		test4();
	}

	private static void test1() {
		System.out.println("===test1===");
		DVertex v1 = new DVertex(1);
		DVertex v2 = new DVertex(2);
		DVertex v3 = new DVertex(3);
		DVertex v4 = new DVertex(4);
		DVertex v5 = new DVertex(5);
		DVertex v6 = new DVertex(6);
		DVertex v7 = new DVertex(7);

		GraphUtil.linkEdge(v1, v2);
		GraphUtil.linkEdge(v1, v4);
		GraphUtil.linkEdge(v1, v5, Direction.V1TOV0);

		GraphUtil.linkEdge(v2, v3);

		GraphUtil.linkEdge(v3, v5, Direction.V0TOV1);
		GraphUtil.linkEdge(v3, v6, Direction.V0TOV1);
		GraphUtil.linkEdge(v3, v7);

		GraphUtil.linkEdge(v4, v6);

		System.out.println(GraphUtil.link(v1, v5));
		System.out.println(GraphUtil.link(v5, v1));
	}

	private static void test2() {
		System.out.println("===test2===");
		DVertex v1 = new DVertex(1);
		DVertex v2 = new DVertex(2);
		DVertex v3 = new DVertex(3);
		DVertex v4 = new DVertex(4);
		DVertex v5 = new DVertex(5);
		DVertex v6 = new DVertex(6);
		DVertex v7 = new DVertex(7);

		GraphUtil.linkVertex(v1, v2);
		GraphUtil.linkVertex(v1, v4);
		GraphUtil.linkVertex(v1, v5, Direction.V1TOV0);

		GraphUtil.linkVertex(v2, v3);

		GraphUtil.linkVertex(v3, v5, Direction.V0TOV1);
		GraphUtil.linkVertex(v3, v6, Direction.V0TOV1);
		GraphUtil.linkVertex(v3, v7);

		GraphUtil.linkVertex(v4, v6);

		System.out.println(GraphUtil.link(v1, v5));
		System.out.println(GraphUtil.link(v5, v1));
	}

	public static void test3() {
		System.out.println("===test3===");
		DVertex v1 = new DVertex(1);
		DVertex v2 = new DVertex(2);

		DEdge edge4 = new DEdge(v2, v1, Direction.BOTHWAY);
		DEdge edge3 = new DEdge(v1, v2, Direction.V0TOV1);
		System.out.println(edge3.equals(edge4));

		DEdge edge1 = new DEdge(v1, v2, Direction.BOTHWAY);
		DEdge edge2 = new DEdge(v2, v1, Direction.BOTHWAY);
		System.out.println(edge1.equals(edge2));

		DEdge edge5 = new DEdge(v1, v2, Direction.V0TOV1);
		DEdge edge6 = new DEdge(v2, v1, Direction.V1TOV0);
		System.out.println(edge5.equals(edge6));
	}

	public static void test4() {
		System.out.println("===test4===");
		String a1 = new String("1111aaaa");
		String a2 = new String("1111aaaa");
		System.out.println(a1.hashCode());
		System.out.println(a2.hashCode());
	}
}
