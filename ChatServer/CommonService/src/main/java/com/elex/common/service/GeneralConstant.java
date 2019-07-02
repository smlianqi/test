package com.elex.common.service;

/**
 * 常量
 * 
 * @author mausmars
 *
 */
public class GeneralConstant {
	/** 游戏上线时间 */
	public static final String OnlineTime = "2018-01-01";

	/** ------ 功能服务相关 ------ */
	/** 默认功能服务id的key */
	public static final String DefaultFunctionServiceIdKey = "common";
	/** 过滤所有组 */
	public static final String ALL = "*";
	/** leader路径 */
	public static final String ZK_SM_Leader_Path = "leader"; //

	/** ------ ingite相关 ------ */
	public static final String DefaultIngiteServiceIdKey = "common";
	/** ------ member相关 ------ */
	public static final String DefaultMemberServiceIdKey = "common";
	/** ------ event相关 ------ */
	public static final String DefaultEventServiceIdKey = "common";

	/** ------ 网络相关 ------ */
	public static final String NetClientServiceId_C2G = "client2gate";
	public static final String ChannelOptionKey = "channeloption";

	public static final String NetServerServiceId_S2S = "s2s";
	public static final String NetClientServiceId_S2S = "s2s";

}
