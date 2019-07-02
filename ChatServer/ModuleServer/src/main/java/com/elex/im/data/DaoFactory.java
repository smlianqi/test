package com.elex.im.data;

import com.elex.common.component.ignite.IIgniteService;
import com.elex.common.component.proxy.CglibMonitorInterceptor;
import com.elex.common.component.proxy.CglibProxyFactory;
import com.elex.im.data.chatmessage.ChatMessageDao;
import com.elex.im.data.chatmessage.IChatMessageDao;
import com.elex.im.data.chatroom.ChatRoomDao;
import com.elex.im.data.chatroom.IChatRoomDao;
import com.elex.im.data.chatroommember.ChatRoomMemberDao;
import com.elex.im.data.chatroommember.IChatRoomMemberDao;
import com.elex.im.data.chatuser.ChatUserDao;
import com.elex.im.data.chatuser.IChatUserDao;
import com.elex.im.data.roomuseronline.IRoomUserOnlineDao;
import com.elex.im.data.roomuseronline.RoomUserOnlineDao;

public class DaoFactory {
	private CglibProxyFactory cglibProxyFactory;

	public DaoFactory() {
		CglibMonitorInterceptor interceptor = new CglibMonitorInterceptor();
		cglibProxyFactory = new CglibProxyFactory(interceptor);
	}

	public IChatMessageDao createChatMessageDao(IIgniteService igniteService) {
		// ChatMessageDao dao = new ChatMessageDao(igniteService);
		IChatMessageDao dao = cglibProxyFactory.getInterface(ChatMessageDao.class, new Class[] { IIgniteService.class },
				new Object[] { igniteService });
		return dao;
	}

	public IChatRoomDao createChatRoomDao(IIgniteService igniteService) {
		// ChatRoomDao dao = new ChatRoomDao(igniteService);
		IChatRoomDao dao = cglibProxyFactory.getInterface(ChatRoomDao.class, new Class[] { IIgniteService.class },
				new Object[] { igniteService });
		return dao;
	}

	public IChatRoomMemberDao createChatRoomMemberDao(IIgniteService igniteService) {
		// ChatRoomMemberDao dao = new ChatRoomMemberDao(igniteService);
		IChatRoomMemberDao dao = cglibProxyFactory.getInterface(ChatRoomMemberDao.class,
				new Class[] { IIgniteService.class }, new Object[] { igniteService });
		return dao;
	}

	public IRoomUserOnlineDao createRoomUserOnlineDao(IIgniteService igniteService) {
		// RoomUserOnlineDao dao = new RoomUserOnlineDao(igniteService);
		IRoomUserOnlineDao dao = cglibProxyFactory.getInterface(RoomUserOnlineDao.class,
				new Class[] { IIgniteService.class }, new Object[] { igniteService });
		return dao;
	}

	public IChatUserDao createChatUserDao(IIgniteService igniteService) {
		// ChatUserDao dao = new ChatUserDao(igniteService);
		IChatUserDao dao = cglibProxyFactory.getInterface(ChatUserDao.class, new Class[] { IIgniteService.class },
				new Object[] { igniteService });
		return dao;
	}

}
