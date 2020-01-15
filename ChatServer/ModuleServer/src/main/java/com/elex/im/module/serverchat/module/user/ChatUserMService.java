package com.elex.im.module.serverchat.module.user;

import com.elex.common.component.ignite.IIgniteService;
import com.elex.common.component.player.IPlayer;
import com.elex.common.component.player.type.PlayerAttachType;
import com.elex.common.component.player.type.UserType;
import com.elex.common.service.GeneralConstant;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.type.ServiceType;
import com.elex.im.data.chatuser.ChatUser;
import com.elex.im.data.chatuser.IChatUserDao;
import com.elex.im.module.common.user.UserMService;
import com.elex.im.data.DaoFactory;
import com.elex.im.module.translation.type.LanguageType;

public class ChatUserMService extends UserMService implements IChatUserMService {
	private IChatUserDao chatUserDao;
	private DaoFactory daoFactory = new DaoFactory();

	public ChatUserMService(IGlobalContext context) {
		super(context);
	}

	@Override
	public void init() {
		super.init();

		IIgniteService igniteService = context.getServiceManager().getService(ServiceType.ignite,
				GeneralConstant.DefaultIngiteServiceIdKey);

		chatUserDao = daoFactory.createChatUserDao(igniteService);
	}

	@Override
	public void loginInit(IPlayer player) {
		String uid = String.valueOf(player.getUserId());
		ChatUser chatUser = getChatUser(uid);
		if (chatUser == null) {
			LanguageType languageType = player.getAttach(PlayerAttachType.LanguageType);
			chatUser = createChatUser(uid, player.getUserType(), languageType);
			// 插入
			insertChatUser(chatUser);
		}
	}

	@Override
	public void modifyLanguage(IPlayer player, LanguageType languageType) {
		super.modifyLanguage(player, languageType);

		String uid = String.valueOf(player.getUserId());
		ChatUser chatUser = getChatUser(uid);
		if (chatUser != null) {
			chatUser.setLanguageType(languageType.getIsoCode());
			// 更新
			updateChatUser(chatUser);
		}
	}

	// ------ 数据操作方法 ------
	@Override
	public ChatUser getChatUser(String uid) {
		return chatUserDao.selectByUid(uid);
	}

	public void insertChatUser(ChatUser pojo) {
		chatUserDao.insert(pojo, null);
	}

	public void updateChatUser(ChatUser pojo) {
		chatUserDao.update(pojo, null);
	}

	public ChatUser createChatUser(String uid, UserType userType, LanguageType languageType) {
		ChatUser entity = new ChatUser();
		entity.setUid(uid);
		entity.setUserType(userType.ordinal());
		entity.setLanguageType(languageType.getIsoCode());
		return entity;
	}
}
