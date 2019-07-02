package com.elex.im.data.chatroommember;

import java.util.List;

import com.elex.common.component.data.IDao;

public interface IChatRoomMemberDao extends IDao<ChatRoomMember> {
	ChatRoomMember selectByRoomIdAndUid(String roomId, String uid);

	List<ChatRoomMember> selectByUid(String uid);

	List<ChatRoomMember> selectByRoomId(String roomId);
}
