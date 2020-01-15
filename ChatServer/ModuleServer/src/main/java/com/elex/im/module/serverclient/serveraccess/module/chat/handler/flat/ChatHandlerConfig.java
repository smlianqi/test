package com.elex.im.module.serverclient.serveraccess.module.chat.handler.flat;

import java.util.ArrayList;
import java.util.List;

import com.elex.common.net.message.MessageConfig;
import com.elex.im.module.serverchat.module.chat.ChatMService;
import com.elex.im.module.serverchat.module.chat.handler.flat.CreateChatRoomUpHandler;
import com.elex.im.module.serverchat.module.chat.handler.flat.GainChatMessageUpHandler;
import com.elex.im.module.serverchat.module.chat.handler.flat.ManagerChatRoomMemberUpHandler;
import com.elex.im.module.serverchat.module.chat.handler.flat.SendChatMessageUpHandler;
import com.elex.im.module.serverchat.module.chat.handler.flat.TranslationMessageUpHandler;
import com.elex.im.module.serverclient.serveraccess.service.IReqCallBack;

/**
 * 
 */
public class ChatHandlerConfig {
	public List<MessageConfig> createHandler(ChatMService service, IReqCallBack callBack) {
		// 消息配置
		List<MessageConfig> messageConfigs = new ArrayList<MessageConfig>();

		// 下行处理
		{
			CreateChatRoomDownHandler downHandler = new CreateChatRoomDownHandler(callBack);
			messageConfigs.add(downHandler.createMessageConfig());
		}
		{
			ManagerChatRoomMemberDownHandler downHandler = new ManagerChatRoomMemberDownHandler(callBack);
			messageConfigs.add(downHandler.createMessageConfig());
		}
		{
			GainChatMessageDownHandler downHandler = new GainChatMessageDownHandler(callBack);
			messageConfigs.add(downHandler.createMessageConfig());
		}
		{
			SendChatMessageDownHandler downHandler = new SendChatMessageDownHandler(callBack);
			messageConfigs.add(downHandler.createMessageConfig());
		}
		{
			TranslationMessageDownHandler downHandler = new TranslationMessageDownHandler(callBack);
			messageConfigs.add(downHandler.createMessageConfig());
		}
		// 上行处理（只是为了注册消息）
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
		return messageConfigs;
	}
}
