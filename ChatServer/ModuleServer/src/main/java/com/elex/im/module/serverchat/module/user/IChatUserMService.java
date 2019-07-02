package com.elex.im.module.serverchat.module.user;

import com.elex.im.data.chatuser.ChatUser;
import com.elex.im.module.common.user.IUserMService;

/**
 * 用户服务
 * 
 * @author mausmars
 *
 */
public interface IChatUserMService extends IUserMService {
	ChatUser getChatUser(String uid);
}
