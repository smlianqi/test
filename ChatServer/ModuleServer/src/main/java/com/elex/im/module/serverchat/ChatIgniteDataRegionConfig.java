package com.elex.im.module.serverchat;

import java.util.LinkedList;
import java.util.List;

import org.apache.ignite.configuration.DataRegionConfiguration;

import com.elex.common.component.ignite.config.IDataRegionConfig;
import com.elex.common.component.ignite.config.ScIgnite;
import com.elex.im.data.chatmessage.ChatMessageDao;
import com.elex.im.data.chatroom.ChatRoomDao;
import com.elex.im.data.chatroommember.ChatRoomMemberDao;
import com.elex.im.data.chatuser.ChatUserDao;

public class ChatIgniteDataRegionConfig implements IDataRegionConfig {
	@Override
	public DataRegionConfiguration[] createDataRegionConfigurations(ScIgnite c) {
		List<DataRegionConfiguration> list = new LinkedList<>();

		String swapDirectory = c.getSwapDirectory();
		DataRegionConfiguration chatMessage = ChatMessageDao.createDataRegionConfiguration(swapDirectory);
		list.add(chatMessage);

		DataRegionConfiguration chatRoom = ChatRoomDao.createDataRegionConfiguration(swapDirectory);
		list.add(chatRoom);

		DataRegionConfiguration chatRoomMember = ChatRoomMemberDao.createDataRegionConfiguration(swapDirectory);
		list.add(chatRoomMember);

		DataRegionConfiguration chatUser = ChatUserDao.createDataRegionConfiguration(swapDirectory);
		list.add(chatUser);

		return list.toArray(new DataRegionConfiguration[list.size()]);
	}

}
