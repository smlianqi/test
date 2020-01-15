package com.elex.im.data.roomuseronline;

import java.util.List;

import com.elex.common.component.data.IDao;

public interface IRoomUserOnlineDao extends IDao<RoomUserOnline> {
	RoomUserOnline selectByRoomIdAndUid(String roomId, String uid);

	List<RoomUserOnline> selectByUid(String uid);

	List<RoomUserOnline> selectByRoomId(String roomId);
}
