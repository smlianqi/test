package com.elex.im.module.common.user;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.member.data.MemberOnline;
import com.elex.common.component.player.IPlayer;
import com.elex.common.component.player.IPlayerInitOffline;
import com.elex.common.component.player.type.UserType;
import com.elex.common.net.session.ISession;
import com.elex.common.service.module.IModuleService;
import com.elex.im.module.translation.type.LanguageType;

import java.util.List;

/**
 * 用户服务
 * 
 * @author mausmars
 *
 */
public interface IUserMService extends IModuleService, IPlayerInitOffline {
	/**
	 * 用户是否在线
	 * 
	 * @param uid
	 * @return
	 */
	MemberOnline getUserOnline(String uid, FunctionType functionType);

	MemberOnline getUserOnlineInGate(String uid);

	// -------------------------------------
	MemberOnline bindMember(IPlayer player, String extend);

	void modifyLanguage(IPlayer player, LanguageType languageType);

	IPlayer createPlayer(ISession session, long userId, UserType userType);

	// -------------------------------------
	/**
	 * 转发
	 * 
	 * @param message
	 * @param userId
	 */
	void forwardSingle(Object message, String userId);

	void forwardGroup(int commandId, byte[] content, List<String> userIds, String sid);

	void forwardWhole(int commandId, byte[] content);
}
