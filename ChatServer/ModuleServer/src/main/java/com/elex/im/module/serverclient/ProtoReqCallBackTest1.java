package com.elex.im.module.serverclient;

import java.util.LinkedList;
import java.util.List;

import com.elex.common.component.function.type.FunctionType;
import com.elex.common.component.player.IPlayer;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;
import com.elex.im.message.proto.ChatMessage.ChatRoomInfo;
import com.elex.im.message.proto.ChatMessage.ChatRoomMessageInfo;
import com.elex.im.message.proto.ChatMessage.CreateChatRoomDown;
import com.elex.im.message.proto.ChatMessage.GainChatMessageDown;
import com.elex.im.message.proto.ChatMessage.ManagerChatRoomMemberDown;
import com.elex.im.message.proto.ChatMessage.SendChatMessageDown;
import com.elex.im.message.proto.ChatMessage.TranslationMessageDown;
import com.elex.im.message.proto.ErrorMessage.ErrorMessageDown;
import com.elex.im.message.proto.ErrorMessage.SuccessMessageDown;
import com.elex.im.message.proto.UserMessage.UserLoginDown;
import com.elex.im.module.serverchat.module.chat.content.TextContent;
import com.elex.im.module.serverchat.module.chat.type.MemberModifyType;
import com.elex.im.module.serverchat.module.chat.type.RoomType;
import com.elex.im.module.serverclient.request.SendMultiChatReq;
import com.elex.im.module.serverclient.request.SendSingleChatReq;
import com.elex.im.module.serverclient.request.SendWorldChatReq;
import com.elex.im.module.serverclient.serveraccess.service.IChatClientAccessService;
import com.elex.im.module.serverclient.serveraccess.service.IProtoReqCallBack;

public class ProtoReqCallBackTest1 implements IProtoReqCallBack {
	protected static final ILogger logger = XLogUtil.logger();

	private IChatClientAccessService chatClientAccessService;

	public ProtoReqCallBackTest1(IChatClientAccessService chatClientAccessService) {
		this.chatClientAccessService = chatClientAccessService;
	}

	@Override
	public void userLoginClassBack(final IPlayer player, UserLoginDown message) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> userLogin ClassBack userId=" + player.getUserId());
		}

		FunctionType functionType = FunctionType.valueOf(message.getFunctionType());
		if (functionType != FunctionType.chat) {
			return;
		}

		long targetUid = ChatTestManger.userId2;
		if (player.getUserId() == ChatTestManger.userId2) {
			targetUid = ChatTestManger.userId1;
		}
		// test1(player, targetUid);

		// if (player.getUserId() == userId2) {
		// // 创建聊天室
		// createChatRoom(player);
		// // 获取聊天室信息
		// gainChatMessage(player);
		// }
		// if (player.getUserId() == ChatTestManger.userId1) {
		// sendMessage2WorldRoom(player);
		// // 创建聊天室
		// createChatRoom(player);
		// // 获取聊天室信息
		// gainChatMessage(player);
		// }

		// 测试获取

		// 测试发送消息

		// 测试创建聊天室

		// 测试成员管理

		if (player.getUserId() == ChatTestManger.userId1) {
			// 创建组聊天室
			// createChatRoom(player);

			// 发送世界聊天
			// sendMessage2WorldRoom(player);
			// 发送联盟聊天
			// sendMessage2UnionRoom(player);
			// 发送区聊天
			// sendMessage2RegionRoom(player);

			// 获取全部最新消息
			// sendGainAllNewestChatMessage(player, ChatTestManger.RegionRoomId,
			// ChatTestManger.UnionRoomId);

			sendGainRoomNewestChatMessage(player, ChatTestManger.RegionRoomId, RoomType.Region);
			sendGainRoomNewestChatMessage(player, ChatTestManger.UnionRoomId, RoomType.Union);
			sendGainRoomNewestChatMessage(player, "e979ed1254eb4849a636e0d65c7f1ebb", RoomType.Group);
			
			// sendGainRoomNewestChatMessage(player, ChatTestManger.UnionRoomId);

			// sendGainRoomNewestChatMessage(player, ChatTestManger.UnionRoomId);

			// sendPageGainChatMessage(player, ChatTestManger.UnionRoomId, 7, 5);
		}

		if (player.getUserId() == ChatTestManger.userId2) {
			// 发送世界聊天
			// sendMessage2WorldRoom(player);
			// 发送联盟聊天
			// sendMessage2UnionRoom(player);
			// 发送区聊天
			// sendMessage2RegionRoom(player);
			// 创建主聊天室
			// 发送区聊天
			// sendMessage2UnionRoom(player);
		}
	}

	// 往组聊天 发送消息
	private void sendMessage2GroupRoom(IPlayer player, String roomId) {
		for (int i = 0; i < 1; i++) {
			String text = "GroupRoom message {100,100} [100,100] playerId=" + player.getUserId() + " test! i=" + i;
			TextContent content = new TextContent(text);

			SendMultiChatReq req = new SendMultiChatReq();
			req.setUid(player.getUserId());
			req.setContentType(content.contentType());
			req.setContent(content.content2Json());
			req.setSendedTime(System.currentTimeMillis());
			req.setClientExt("");
			req.setRoomId(roomId);
			req.setRoomType(RoomType.Group);
			chatClientAccessService.sendMultiChatMessage(player.getUserId(), req);
		}
	}

	// 往联盟聊天 发送消息
	private void sendMessage2UnionRoom(IPlayer player) {
		for (int i = 0; i < 1; i++) {
			String text = "UnionRoom message {100,100} [100,100] playerId=" + player.getUserId() + " test! i=" + i;
			TextContent content = new TextContent(text);

			SendMultiChatReq req = new SendMultiChatReq();
			req.setUid(player.getUserId());
			req.setContentType(content.contentType());
			req.setContent(content.content2Json());
			req.setSendedTime(System.currentTimeMillis());
			req.setClientExt("");
			req.setRoomId(ChatTestManger.UnionRoomId);
			req.setRoomType(RoomType.Union);
			chatClientAccessService.sendMultiChatMessage(player.getUserId(), req);
		}
	}

	// 往区聊天 发送消息
	private void sendMessage2RegionRoom(IPlayer player) {
		for (int i = 0; i < 1; i++) {
			String text = "RegionRoom message {100,100} [100,100] playerId=" + player.getUserId() + " test! i=" + i;
			TextContent content = new TextContent(text);

			SendMultiChatReq req = new SendMultiChatReq();
			req.setUid(player.getUserId());
			req.setContentType(content.contentType());
			req.setContent(content.content2Json());
			req.setSendedTime(System.currentTimeMillis());
			req.setClientExt("");
			req.setRoomId(ChatTestManger.RegionRoomId);
			req.setRoomType(RoomType.Region);
			chatClientAccessService.sendMultiChatMessage(player.getUserId(), req);
		}
	}

	// 往世界聊天 发送消息
	private void sendMessage2WorldRoom(IPlayer player) {
		for (int i = 0; i < 1; i++) {
			String text = "WorldRoom message {100,100} [100,100] playerId=" + player.getUserId() + " test! i=" + i;
			TextContent content = new TextContent(text);

			SendWorldChatReq req = new SendWorldChatReq();
			req.setUid(player.getUserId());
			req.setContentType(content.contentType());
			req.setContent(content.content2Json());
			req.setSendedTime(System.currentTimeMillis());
			req.setClientExt("");
			req.setRoomId(ChatTestManger.WorldRoomId);
			chatClientAccessService.sendWorldChatMessage(player.getUserId(), req);
		}
	}

	private void sendGainRoomNewestChatMessage(IPlayer player, String roomId, RoomType roomType) {
		chatClientAccessService.sendGainRoomNewestChatMessage(player.getUserId(), 0, roomId, roomType);
	}

	private void sendGainRoomPageChatMessage(IPlayer player, String roomId, RoomType roomType, int orders, int count) {
		chatClientAccessService.sendGainRoomPageChatMessage(player.getUserId(), 0, roomId, roomType, orders, count);
	}

	// 获取聊天信息
	private void sendGainAllNewestChatMessage(IPlayer player, String regionRoomId, String unionRoomId) {
		chatClientAccessService.sendGainAllNewestChatMessage(player.getUserId(), 0, regionRoomId, unionRoomId);
	}

	// 创建组
	private void createChatRoom(IPlayer player) {
		{
			List<Long> members = new LinkedList<>();
			members.add(ChatTestManger.userId1);
			members.add(ChatTestManger.userId2);

			chatClientAccessService.sendCreateChatRoom(player.getUserId(), RoomType.Group, "", "", members);
		}
	}

	// 测试断网重连继续发消息
	private void singleChatMessage(IPlayer player, long targetUid) {
		Thread thread = new Thread() {
			public void run() {
				for (;;) {
					SendSingleChatReq req = new SendSingleChatReq();
					req.setTargetUid(targetUid);
					req.setContent("Hello World!!!!!!");
					req.setSendedTime(System.currentTimeMillis());
					req.setTargetUid(targetUid);
					req.setClientExt("");
					req.setUid(player.getUserId());
					chatClientAccessService.sendSingleChatMessage(player.getUserId(), req);
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		thread.start();
	}

	@Override
	public void gainChatMessageClassBack(IPlayer player, GainChatMessageDown message) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> gainChatMessage ClassBack userId=" + player.getUserId() + " message=" + message);
		}

		List<ChatRoomMessageInfo> chatRoomMessageInfos = message.getChatRoomMessageList();

		for (ChatRoomMessageInfo chatRoomMessageInfo : chatRoomMessageInfos) {
			RoomType roomType = RoomType.valueOf(chatRoomMessageInfo.getRoomType());
			switch (roomType) {
			case Group: {
				ChatRoomInfo chatRoomInfo = chatRoomMessageInfo.getChatRoom();

				TextContent content = new TextContent("Test!!! roomType=" + roomType.name());

				SendMultiChatReq req = new SendMultiChatReq();
				req.setUid(player.getUserId());
				req.setContentType(content.contentType());
				req.setContent(content.content2Json());
				req.setSendedTime(System.currentTimeMillis());
				req.setClientExt("");
				req.setRoomId(chatRoomInfo.getRoomId());
				req.setRoomType(roomType);
				req.setAtUids(new LinkedList<>());
				chatClientAccessService.sendMultiChatMessage(player.getUserId(), req);
				break;
			}
			// case Union:
			// msg =
			// PrototModuleMessageCreater.createSendMultiChatMessageUp(player.getUserId(),
			// roomType,
			// "Test!!! roomType=" + roomType, chatRoomInfo.getRoomId(), new
			// LinkedList<>());
			// break;
			// case Region:
			// msg =
			// PrototModuleMessageCreater.createSendMultiChatMessageUp(player.getUserId(),
			// roomType,
			// "Test!!! roomType=" + roomType, chatRoomInfo.getRoomId(), new
			// LinkedList<>());
			// break;
			// case Single:
			// long targetUid = userId2;
			// if (player.getUserId() == userId2) {
			// targetUid = userId1;
			// }
			// msg =
			// PrototModuleMessageCreater.createSendSingleChatMessageUp(player.getUserId(),
			// "Single test!!! roomType=" + roomType, targetUid);
			// break;
			// case World:
			// msg =
			// PrototModuleMessageCreater.createSendWorldChatMessageUp(player.getUserId(),
			// "World test!!! roomType=" + roomType, "");
			// break;
			default:
				break;
			}
		}
	}

	@Override
	public void createChatRoomClassBack(IPlayer player, CreateChatRoomDown message) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> createChatRoom ClassBack userId=" + player.getUserId() + ", rid=" + message.getRoomId());
		}
		List<Long> members = new LinkedList<>();
		members.add(ChatTestManger.userId3);

		chatClientAccessService.sendManagerChatRoomMember(player.getUserId(), message.getRoomId(),
				MemberModifyType.Insert, members);
	}

	@Override
	public void managerChatRoomMemberClassBack(IPlayer player, ManagerChatRoomMemberDown message) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> managerChatRoomMember ClassBack userId=" + player.getUserId());
		}
	}

	@Override
	public void sendChatMessageClassBack(IPlayer player, SendChatMessageDown message) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> sendChatMessage ClassBack userId=" + player.getUserId() + " message=" + message);
		}
	}

	@Override
	public void errorMessageClassBack(IPlayer player, ErrorMessageDown message) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> errorMessage ClassBack userId=" + player.getUserId() + " message=" + message);
		}
	}

	@Override
	public void successMessageClassBack(IPlayer player, SuccessMessageDown message) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> successMessage ClassBack userId=" + player.getUserId() + " message=" + message);
		}
	}

	@Override
	public void translationMessageClassBack(IPlayer player, TranslationMessageDown message) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> translationMessage ClassBack userId=" + player.getUserId() + " message=" + message);
		}
	}
}
