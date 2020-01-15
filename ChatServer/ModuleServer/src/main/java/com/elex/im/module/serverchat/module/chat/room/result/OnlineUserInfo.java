package com.elex.im.module.serverchat.module.chat.room.result;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.elex.common.component.member.data.MemberOnline;
import com.elex.im.data.chatuser.ChatUser;
import com.elex.im.module.translation.type.LanguageType;

/**
 * 房间在线用户信息
 * 
 * @author mausmars
 *
 */
public class OnlineUserInfo {
	// {sid:用户ids}
	private Map<String, List<String>> onlineUserMap = new HashMap<>();
	private Set<LanguageType> languageTypes;

	public OnlineUserInfo(boolean isTranslation) {
		if (isTranslation) {
			languageTypes = new HashSet<>();
		}
	}

	public void insertUserOnline(MemberOnline userOnline, ChatUser chatUser) {
		List<String> uids = onlineUserMap.get(userOnline.getSid());
		if (uids == null) {
			uids = new LinkedList<>();
			onlineUserMap.put(userOnline.getSid(), uids);
		}
		uids.add(userOnline.getMemberId());

		if (languageTypes != null) {
			// 插入语言类型
			LanguageType languageType = LanguageType.getLanguageType(chatUser.getLanguageType());
			languageTypes.add(languageType);
		}
	}

	public Map<String, List<String>> getOnlineUserMap() {
		return onlineUserMap;
	}

	public Set<LanguageType> getLanguageTypes() {
		return languageTypes;
	}
}
