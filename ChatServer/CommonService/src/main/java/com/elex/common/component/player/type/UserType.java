package com.elex.common.component.player.type;

/**
 * 0-client,1-server,2-npc,3-gm
 * 
 * @author mausmars
 *
 */
public enum UserType {
	/** 0 普通用户 */
	Client, //
	/** 1 服务器 */
	Server, //
	/** 2 模拟机器人 */
	Npc, //
	/** 3 gm管理员 */
	Gm, //
	;

	public static UserType valueOf(int v) {
		if (v < 0 || v > 3) {
			return null;
		}
		return UserType.values()[v];
	}
}
