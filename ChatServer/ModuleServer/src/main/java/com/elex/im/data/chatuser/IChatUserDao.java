package com.elex.im.data.chatuser;

import com.elex.common.component.data.IDao;

public interface IChatUserDao extends IDao<ChatUser> {
	ChatUser selectByUid(String uid);
}
