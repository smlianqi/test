package com.elex.im.module.serverclient;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.player.type.UserType;
import com.elex.common.util.uuid.UUIDUtil;
import com.elex.im.module.listener.ClientConnectListener;
import com.elex.im.module.serverclient.serveraccess.service.ProtoChatClientAccessService;
import com.elex.im.module.translation.type.LanguageType;

public class ChatClientAccessServiceTest2 {
	public static void main(String[] args) {
		String serverId = "slgx1#r1#" + FunctionType.client + "@" + UUIDUtil.getUUID();

		ProtoChatClientAccessService service = new ProtoChatClientAccessService(serverId);
		ProtoReqCallBackTest2 callBack = new ProtoReqCallBackTest2(service);
		// FlatChatClientAccessService service = new
		// FlatChatClientAccessService(serverId);
		// FlatReqCallBack1 callBack = new FlatReqCallBack1();

		service.setCallBack(callBack);

		service.setHost("10.1.14.244");
		service.setPort(14310);
		// service.setPort(15000);

		// service.setHost("10.1.17.223");
		// service.setPort(14310);

		service.setConnectCount(5);
		service.setToken("XXX");

		// 初始化
		service.startup();

		// 发送登录

		service.sendUserLogin(ChatTestManger.userId1, UserType.Client, LanguageType.en);

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		try {
			Thread.sleep(100000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// 关闭
		service.shutdown();
	}
}
