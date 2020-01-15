package com.elex.common.util.graph;

import java.awt.Point;
import java.util.List;

/**
 * 图形学工具
 * 
 * @author mausmars
 *
 */
public class GraphUtil {
	private static float getCross(Point p, Point p1, Point p2) {
		return (p2.x - p1.x) * (p.y - p1.y) - (p.x - p1.x) * (p2.y - p1.y);
	}

	/**
	 * 判断p点是否在矩形ps内部
	 * 
	 * @param p
	 *            要验证的点
	 * @param ps
	 *            矩形对角线
	 * @return
	 */
	public static boolean isPointInMatrix(Point p, Point[] ps) {
		Point p1 = ps[0];
		Point p2 = ps[1];
		Point p3 = ps[2];
		Point p4 = ps[3];
		return getCross(p, p1, p2) * getCross(p, p3, p4) >= 0 && getCross(p, p2, p3) * getCross(p, p4, p1) >= 0;
	}

	public static boolean isPointInMatrix(List<Integer> pl, Point[] ps) {
		Point p = new Point(pl.get(0), pl.get(1));
		return isPointInMatrix(p, ps);
	}

	public static Point[] changePoint(List<List<Integer>> pls) {
		Point p1 = new Point(pls.get(0).get(0), pls.get(0).get(1));
		Point p2 = new Point(pls.get(1).get(0), pls.get(1).get(1));
		Point p3 = new Point(pls.get(2).get(0), pls.get(2).get(1));
		Point p4 = new Point(pls.get(3).get(0), pls.get(3).get(1));

		Point[] ps = new Point[] { p1, p2, p3, p4 };
		return ps;
	}

}
