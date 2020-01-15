package com.elex.im.test.classanalysiser;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Food {
	// 基础
	private byte type100;
	private short type101;
	private int type102;
	private long type103;
	private float type104;
	private double type105;
	private boolean type106;

	// 对象基础
	private Byte type200;
	private Short type201;
	private Integer type202;
	private Long type203;
	private Float type204;
	private Double type205;
	private Boolean type206;

	// 枚举类型
	private PrimitiveType type;

	// 对象
	private Object type0;

	// 字符串
	private String type7;
	private Character type91;

	// 数组
	private byte[] type20;
	private short[] type21;
	private int[] type22;
	private long[] type23;
	private float[] type24;
	private double[] type25;

	private Integer[] type26;
	private Object[] type27;

	private List<Integer>[] type66;

	private int[][][] type28;

	// 集合
	private List<Integer> type16;
	private Set<Integer> type17;
	private Map<Integer, Integer> type18;

}
