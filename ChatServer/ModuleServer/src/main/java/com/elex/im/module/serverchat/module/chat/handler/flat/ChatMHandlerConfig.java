package com.elex.im.module.serverchat.module.chat.handler.flat;

import java.util.LinkedList;
import java.util.List;

import com.elex.common.net.message.MessageConfig;
import com.elex.im.module.serverchat.module.chat.ChatMService;
import com.elex.im.module.serverclient.imitateclient.module.chat.handler.flat.CreateChatRoomDownHandler;
import com.elex.im.module.serverclient.imitateclient.module.chat.handler.flat.GainChatMessageDownHandler;
import com.elex.im.module.serverclient.imitateclient.module.chat.handler.flat.ManagerChatRoomMemberDownHandler;
import com.elex.im.module.serverclient.imitateclient.module.chat.handler.flat.SendChatMessageDownHandler;
import com.elex.im.module.serverclient.imitateclient.module.chat.handler.flat.TranslationMessageDownHandler;

/**
 * 
 */
public class ChatMHandlerConfig {
	public List<MessageConfig> createHandler(ChatMService service) {
		// 消息配置
		List<MessageConfig> messageConfigs = new LinkedList<>();

		// 上行处理
		{
			CreateChatRoomUpHandler upHandler = new CreateChatRoomUpHandler(service);
			messageConfigs.add(upHandler.createMessageConfig());
		}
		{
			ManagerChatRoomMemberUpHandler upHandler = new ManagerChatRoomMemberUpHandler(service);
			messageConfigs.add(upHandler.createMessageConfig());
		}
		{
			GainChatMessageUpHandler upHandler = new GainChatMessageUpHandler(service);
			messageConfigs.add(upHandler.createMessageConfig());
		}
		{
			SendChatMessageUpHandler upHandler = new SendChatMessageUpHandler(service);
			messageConfigs.add(upHandler.createMessageConfig());
		}
		{
			TranslationMessageUpHandler upHandler = new TranslationMessageUpHandler(service);
			messageConfigs.add(upHandler.createMessageConfig());
		}
		// 下行处理（只是为了注册消息）
		{
			CreateChatRoomDownHandler downHandler = new CreateChatRoomDownHandler();
			messageConfigs.add(downHandler.createMessageConfig());
		}
		{
			ManagerChatRoomMemberDownHandler downHandler = new ManagerChatRoomMemberDownHandler();
			messageConfigs.add(downHandler.createMessageConfig());
		}
		{
			GainChatMessageDownHandler downHandler = new GainChatMessageDownHandler();
			messageConfigs.add(downHandler.createMessageConfig());
		}
		{
			SendChatMessageDownHandler downHandler = new SendChatMessageDownHandler();
			messageConfigs.add(downHandler.createMessageConfig());
		}
		{
			TranslationMessageDownHandler downHandler = new TranslationMessageDownHandler();
			messageConfigs.add(downHandler.createMessageConfig());
		}
		return messageConfigs;
	}
}
