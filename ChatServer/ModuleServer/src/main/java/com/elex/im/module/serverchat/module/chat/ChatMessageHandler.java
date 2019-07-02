package com.elex.im.module.serverchat.module.chat;

import com.elex.common.component.player.IPlayer;
import com.elex.common.component.player.type.PlayerAttachType;
import com.elex.common.net.message.IMessage;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.service.httpclient.ARequestCallback;
import com.elex.common.net.service.netty.session.SessionBox;
import com.elex.common.net.type.SessionAttachType;
import com.elex.common.service.module.ModuleServiceType;
import com.elex.common.util.json.JsonUtil;
import com.elex.im.data.chatmessage.ChatMessage;
import com.elex.im.message.AModuleMessageCreater;
import com.elex.im.message.proto.ChatMessage.*;
import com.elex.im.module.HandleErrorType;
import com.elex.im.module.common.IGameHandler;
import com.elex.im.module.common.error.ErrorType;
import com.elex.im.module.common.error.IErrorMService;
import com.elex.im.module.common.user.IUserMService;
import com.elex.im.module.serverchat.module.HandeResult;
import com.elex.im.module.serverchat.module.chat.content.IContent;
import com.elex.im.module.serverchat.module.chat.room.request.*;
import com.elex.im.module.serverchat.module.chat.room.result.*;
import com.elex.im.module.serverchat.module.chat.type.ContentType;
import com.elex.im.module.serverchat.module.chat.type.ResultStateType;
import com.elex.im.module.serverchat.module.chat.type.RoomType;
import com.elex.im.module.translation.type.LanguageType;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract class ChatMessageHandler<T> extends IGameHandler<T> {
	protected ChatMService service;

	public ChatMessageHandler(ChatMService service) {
		this.service = service;
	}

	public abstract MessageConfig createMessageConfig();

	// -----------------------------------------
	protected void lastMessageOrderInChatUpHandler(IPlayer player, LastMessageOrderInChatUpContext context) {
		//TODO: huangyuanqiang
		if(!player.getUserId().equals(Long.valueOf(context.getUserId()))){
			//需要设置的用户id与当前登录的用户id不一致，不能修改lastOrder
			sendErrorMessage(player, HandeResult.errorResult(HandleErrorType.ParameterError),
					LastMessageOrderInChatUp.MessageEnum.CommondId_VALUE);
		}
		HandeResult handeResult = service.setMessageLastOrder(context);
		// 发送
		switch (handeResult.getStateType()) {
			case Success: {
				lastMessageOrderInChatSuccessResponse(handeResult, player);
				break;
			}
			case Fail:
				logger.error("lastMessageOrderInChatUpHandler Fail!!!");

				// 发送错误消息
				sendErrorMessage(player, handeResult, LastMessageOrderInChatUp.MessageEnum.CommondId_VALUE);
				break;
			default:
				break;
		}
	}
	protected void sendChatMessageUpHandler(IPlayer player, SendMessageReqContext context) {
		HandeResult handeResult = null;
		switch (context.getRoomType()) {
		// 单聊
		case Single: {
			handeResult = service.sendMessage(context);
			if (handeResult.getStateType() == ResultStateType.Success) {
				// 成功的操作
				singleSuccessResponse(handeResult, player, context.getTargetUid());
			}
			break;
		}
		// 组聊
		case Region: {
			handeResult = service.sendMessage(context);
			if (handeResult.getStateType() == ResultStateType.Success) {
				// 区联盟的处理返回
				unionOrRegionSuccessResponse(handeResult, player);
			}
			break;
		}
		case Union: {
			handeResult = service.sendMessage(context);
			if (handeResult.getStateType() == ResultStateType.Success) {
				// 区联盟的处理返回
				unionOrRegionSuccessResponse(handeResult, player);
			}
			break;
		}
		case Group: {
			handeResult = service.sendMessage(context);
			if (handeResult.getStateType() == ResultStateType.Success) {
				// 成功的操作
				groupSuccessResponse(handeResult, player);
			}
			break;
		}
		// 世界聊天
		case World: {
			handeResult = service.sendMessage(context);
			if (handeResult.getStateType() == ResultStateType.Success) {
				// 成功的操作
				worldSuccessResponse(handeResult, player);
			}
			break;
		}
		default:
			break;
		}
		if (handeResult.getStateType() == ResultStateType.Fail) {
			// 发送错误消息
			sendErrorMessage(player, handeResult, SendChatMessageUp.MessageEnum.CommondId_VALUE);
		}
	}

	protected void gainChatMessageUpHandler(IPlayer player, GainChatMessageReqContext context) {
		HandeResult handeResult = service.gainChatMessage(context);
		// 发送
		switch (handeResult.getStateType()) {
		case Success: {
			gainChatMessageSuccessResponse(handeResult, context.getClientSendedTime(), player);
			break;
		}
		case Fail:
			logger.error("GainChatMessageUpHandler Fail!!!");

			// 发送错误消息
			sendErrorMessage(player, handeResult, GainChatMessageUp.MessageEnum.CommondId_VALUE);
			break;
		default:
			break;
		}
	} 

	protected void createChatRoomUpHandler(IPlayer player, CreateChatRoomReqContext context) {
		if (context.getRoomType() == RoomType.Union || context.getRoomType() == RoomType.Region) {
			// 联盟，区 不创建房间
			return;
		}
		HandeResult handeResult = service.createRoom(context);

		// 发送消息
		switch (handeResult.getStateType()) {
		case Success: {
			createChatRoomSuccessResponse(handeResult, player);
			break;
		}
		case Fail:
			logger.error("CreateChatRoomUpHandler Fail!!!");
			// 发送错误消息
			sendErrorMessage(player, handeResult, CreateChatRoomUp.MessageEnum.CommondId_VALUE);
			break;
		default:
			break;
		}
	}

	protected void managerChatRoomMemberUpHandler(IPlayer player, ManagerChatRoomMemberReqContext context) {
		HandeResult handeResult = service.modifyMember(context);
		// 发送消息

		switch (handeResult.getStateType()) {
		case Success: {
			managerChatRoomMemberSuccessResponse(handeResult, player);
			break;
		}
		case Fail:
			logger.error("ManagerChatRoomMemberUpHandler Fail!!!");
			// 发送错误消息
			sendErrorMessage(player, handeResult, ManagerChatRoomMemberUp.MessageEnum.CommondId_VALUE);

			break;
		default:
			break;
		}
	}

	protected void translationMessageUpHandler(IPlayer player, TranslationMessageReqContext context) {
		HandeResult handeResult = service.translationMessage(context);

		switch (handeResult.getStateType()) {
		case Success: {
			// 成功的操作
			translationMessageSuccessResponse(handeResult, player);
			break;
		}
		case Fail:
			logger.error("TranslationMessageUpHandler Fail!!!");

			// 发送错误消息
			sendErrorMessage(player, handeResult, TranslationMessageUp.MessageEnum.CommondId_VALUE);
			break;
		default:
			break;
		}
	}

	private void translationMessageSuccessResponse(HandeResult handeResult, IPlayer player) {
		SessionBox sessionBox = player.getAttachFromSession(SessionAttachType.SessionBox);
		AModuleMessageCreater moduleMessageCreater = sessionBox.getModuleMessageCreater();

		TranslationMessageResult result = handeResult.getAttach();

		LanguageType toLang = player.getAttach(PlayerAttachType.LanguageType);

		ARequestCallback callback = new ARequestCallback() {
			@Override
			public void completed(String translateResult) {
				// 异步调用方法
				translationMessage(player, result, translateResult, moduleMessageCreater);
			}

			@Override
			public void failed(Exception ex) {
				if (logger.isErrorEnabled()) {
					logger.error("", ex);
				}
				// 异步调用方法
				translationMessage(player, result, null, moduleMessageCreater);
			}

			@Override
			public void cancelled() {
				logger.warn("cancelled");
			}
		};
		// 翻译
		service.translate(null, result.getChatMessage().getContent(), toLang, callback);
		String translateResult = null;
		try {
			translateResult = callback.getFuture().get();
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("", e);
			}
		}
		// 同步调用
		translationMessage(player, result, translateResult, moduleMessageCreater);
	}

	private void translationMessage(IPlayer player, TranslationMessageResult result, String translateResult,
			AModuleMessageCreater moduleMessageCreater) {
		Map<String, String> translate = translateStr2Map(translateResult);

		IMessage msg = moduleMessageCreater.createTranslationMessageDown(player.getUserId(), result.getChatMessage(),
				translate, result.getRoomType(), true);
		player.send(msg);
	}

	private void createChatRoomSuccessResponse(HandeResult handeResult, IPlayer player) {
		CreateRoomResult result = handeResult.getAttach();

		SessionBox sessionBox = player.getAttachFromSession(SessionAttachType.SessionBox);
		AModuleMessageCreater moduleMessageCreater = sessionBox.getModuleMessageCreater();

		// 通知用户
		if (result.getOnlineUserInfo() != null) {
			for (Entry<String, List<String>> entry : result.getOnlineUserInfo().getOnlineUserMap().entrySet()) {
				byte[] data = moduleMessageCreater.createCreateChatRoomDownBytes(player.getUserId(), result);
				getUserMService().forwardGroup(CreateChatRoomDown.MessageEnum.CommondId_VALUE, data, entry.getValue(),
						entry.getKey());
			}
		} else {
			// 发给自己
			IMessage msg = moduleMessageCreater.createCreateChatRoomDown(player.getUserId(), result);
			player.send(msg);
		}
	}

	private void managerChatRoomMemberSuccessResponse(HandeResult handeResult, IPlayer player) {
		ModifyMemberResult result = handeResult.getAttach();

		SessionBox sessionBox = player.getAttachFromSession(SessionAttachType.SessionBox);
		AModuleMessageCreater moduleMessageCreater = sessionBox.getModuleMessageCreater();

		// 通知用户
		if (result.getOnlineUserInfo() != null) {
			byte[] data = moduleMessageCreater.createManagerChatRoomMemberDownBytes(result);
			for (Entry<String, List<String>> entry : result.getOnlineUserInfo().getOnlineUserMap().entrySet()) {
				getUserMService().forwardGroup(ManagerChatRoomMemberDown.MessageEnum.CommondId_VALUE, data,
						entry.getValue(), entry.getKey());
			}
		} else {
			// 发给自己
			IMessage msg = moduleMessageCreater.createManagerChatRoomMemberDown(player.getUserId(), result);
			player.send(msg);
		}
	}

	private void lastMessageOrderInChatSuccessResponse(HandeResult handeResult, IPlayer player) {
		Map<String,String> mapping = handeResult.getAttach();
		SessionBox sessionBox = player.getAttachFromSession(SessionAttachType.SessionBox);
		AModuleMessageCreater moduleMessageCreater = sessionBox.getModuleMessageCreater();
		// 发送
		IMessage msg = moduleMessageCreater.createLastMessageOrderInChatDown(player.getUserId(),mapping);
		player.send(msg);
	}

	private void gainChatMessageSuccessResponse(HandeResult handeResult, long clientSendedTime, IPlayer player) {
		List<GainMessageResult> resultChatRoomInfos = handeResult.getAttach();

		SessionBox sessionBox = player.getAttachFromSession(SessionAttachType.SessionBox);
		AModuleMessageCreater moduleMessageCreater = sessionBox.getModuleMessageCreater();

		// 发送
		IMessage msg = moduleMessageCreater.createGainChatMessageDown(player.getUserId(), clientSendedTime,
				resultChatRoomInfos);
		player.send(msg);
	}

	// -----------------------------------------
	// 消息发送逻辑，为了proto，flat handler复用
	private void singleSuccessResponse(HandeResult handeResult, IPlayer player, String targetUid) {
		SessionBox sessionBox = player.getAttachFromSession(SessionAttachType.SessionBox);
		AModuleMessageCreater moduleMessageCreater = sessionBox.getModuleMessageCreater();

		SendMessageResult result = handeResult.getAttach();

		// 马上返回消息
		forwardSingleMessage(player, targetUid, result, null, moduleMessageCreater);

		IContent content = result.getContent();
		if (content.contentType() != ContentType.Text) {
			// Text 类型才翻译
			return;
		}

		// 翻译做异步处理
		Set<LanguageType> toLangs = result.getOnlineUserInfo().getLanguageTypes();
		if (toLangs != null && !toLangs.isEmpty()) {
			// 这里做翻译
			ARequestCallback callback = new ARequestCallback() {
				@Override
				public void completed(String translateResult) {
					// 异步调用方法
					// forwardSingleMessage(player, targetUid, result, translateResult,
					// moduleMessageCreater);
					forwardSingleTranslate(player, targetUid, result, translateResult, moduleMessageCreater);
				}

				@Override
				public void failed(Exception ex) {
					if (logger.isErrorEnabled()) {
						logger.error("", ex);
					}
					// 异步调用方法
					// forwardSingleMessage(player, targetUid, result, null, moduleMessageCreater);
					forwardSingleTranslate(player, targetUid, result, null, moduleMessageCreater);
				}

				@Override
				public void cancelled() {
					logger.warn("cancelled");
				}
			};
			// 翻译
			String translateResult = translate(result, toLangs, callback);

			// 同步调用方法
			// forwardSingleMessage(player, targetUid, result, translateResult,
			// moduleMessageCreater);
			forwardSingleTranslate(player, targetUid, result, translateResult, moduleMessageCreater);
		}
	}

	private void groupSuccessResponse(HandeResult handeResult, IPlayer player) {
		SessionBox sessionBox = player.getAttachFromSession(SessionAttachType.SessionBox);
		AModuleMessageCreater moduleMessageCreater = sessionBox.getModuleMessageCreater();

		SendMessageResult result = handeResult.getAttach();

		// 马上返回消息
		forwardGroupMessage(result, null, moduleMessageCreater);

		IContent content = result.getContent();
		if (content.contentType() != ContentType.Text) {
			// Text 类型才翻译
			return;
		}
		// 翻译做异步处理
		Set<LanguageType> toLangs = result.getOnlineUserInfo().getLanguageTypes();
		if (toLangs != null && !toLangs.isEmpty()) {
			// 这里做翻译
			ARequestCallback callback = new ARequestCallback() {
				@Override
				public void completed(String translateResult) {
					// 异步调用方法
					// forwardGroupMessage(result, translateResult, moduleMessageCreater);
					forwardGroupTranslate(result, translateResult, moduleMessageCreater);
				}

				@Override
				public void failed(Exception ex) {
					if (logger.isErrorEnabled()) {
						logger.error("", ex);
					}
					// 异步调用方法
					// forwardGroupMessage(result, null, moduleMessageCreater);
					forwardGroupTranslate(result, null, moduleMessageCreater);
				}

				@Override
				public void cancelled() {
					logger.warn("cancelled");
				}
			};
			// 翻译
			String translateResult = translate(result, toLangs, callback);

			// 同步调用方法
			// forwardGroupMessage(result, translateResult, moduleMessageCreater);
			forwardGroupTranslate(result, translateResult, moduleMessageCreater);
		}
	}

	private void worldSuccessResponse(HandeResult handeResult, IPlayer player) {
		SessionBox sessionBox = player.getAttachFromSession(SessionAttachType.SessionBox);
		AModuleMessageCreater moduleMessageCreater = sessionBox.getModuleMessageCreater();

		SendMessageResult result = handeResult.getAttach();

		// 马上返回消息
		forwardWorldMessage(result, null, moduleMessageCreater);

		IContent content = result.getContent();
		if (content.contentType() != ContentType.Text) {
			// Text 类型才翻译
			return;
		}
		// TODO 世界服要语言基本都包括了
		Set<LanguageType> toLangs = result.getOnlineUserInfo().getLanguageTypes();
		if (toLangs != null && !toLangs.isEmpty()) {
			// 这里做翻译
			ARequestCallback callback = new ARequestCallback() {
				@Override
				public void completed(String translateResult) {
					// 异步调用方法
					// forwardWorldMessage(result, translateResult, moduleMessageCreater);
					forwardWorldTranslate(result, translateResult, moduleMessageCreater);
				}

				@Override
				public void failed(Exception ex) {
					if (logger.isErrorEnabled()) {
						logger.error("", ex);
					}
					// 异步调用方法
					// forwardWorldMessage(result, null, moduleMessageCreater);
					forwardWorldTranslate(result, null, moduleMessageCreater);
				}

				@Override
				public void cancelled() {
					logger.warn("cancelled");
				}
			};
			// 翻译
			String translateResult = translate(result, toLangs, callback);

			// 同步调用方法
			// forwardWorldMessage(result, translateResult, moduleMessageCreater);
			forwardWorldTranslate(result, translateResult, moduleMessageCreater);
		}
	}

	// 联盟和区处理返回
	private void unionOrRegionSuccessResponse(HandeResult handeResult, IPlayer player) {
		SessionBox sessionBox = player.getAttachFromSession(SessionAttachType.SessionBox);
		AModuleMessageCreater moduleMessageCreater = sessionBox.getModuleMessageCreater();

		SendMessageResult result = handeResult.getAttach();

		// 马上返回消息。直接返回给当前用户，由逻辑服做转发处理
		IMessage msg = moduleMessageCreater.createSendChatMessageDown(player.getUserId(), null, result.getChatMessage(),
				null, result.getRoomType());
		player.send(msg);

		IContent content = result.getContent();
		if (content.contentType() != ContentType.Text) {
			// Text 类型才翻译
			return;
		}
		// 翻译做异步处理
		Set<LanguageType> toLangs = result.getOnlineUserInfo().getLanguageTypes();
		if (toLangs != null && !toLangs.isEmpty()) {
			// 这里做翻译
			ARequestCallback callback = new ARequestCallback() {
				@Override
				public void completed(String translateResult) {
					// 异步调用方法
					forwardUnionOrRegionTranslate(result, translateResult, moduleMessageCreater, player);
				}

				@Override
				public void failed(Exception ex) {
					if (logger.isErrorEnabled()) {
						logger.error("", ex);
					}
					// 异步调用方法
					forwardUnionOrRegionTranslate(result, null, moduleMessageCreater, player);
				}

				@Override
				public void cancelled() {
					logger.warn("cancelled");
				}
			};
			// 翻译
			String translateResult = translate(result, toLangs, callback);
			// 同步调用方法
			forwardUnionOrRegionTranslate(result, translateResult, moduleMessageCreater, player);
		}
	}

	// 转发单人翻译
	private void forwardSingleTranslate(IPlayer player, String targetUid, SendMessageResult result,
			String translateResult, AModuleMessageCreater moduleMessageCreater) {

		Map<String, String> translate = translateStr2Map(translateResult);
		ChatMessage chatMessage = result.getChatMessage();
		chatMessage.setTranslationStr(translateResult);
		this.service.insertChatMessage(chatMessage);
		IMessage msg = moduleMessageCreater.createTranslationMessageDown(player.getUserId(), chatMessage, translate,
				result.getRoomType().getValue(), false);

		long tUid = Long.parseLong(targetUid);
		if (player.getUserId() != tUid) {
			// 转发，TODO 如果可以同个账号同时不同设备登陆，可能有问题。暂时不考虑
			getUserMService().forwardSingle(msg, targetUid);
		}
		player.send(msg);
	}

	// 转发世界消息
	private void forwardWorldTranslate(SendMessageResult result, String translateResult,
			AModuleMessageCreater moduleMessageCreater) {
		Map<String, String> translate = translateStr2Map(translateResult);
		result.setTranslateResult(translate);
		result.getChatMessage().setTranslationStr(translateResult);
		this.service.insertChatMessage(result.getChatMessage());
		byte[] data = moduleMessageCreater.createTranslationMessageDownBytes(result.getChatMessage(), translate,
				result.getRoomType().getValue(), false);
		// 转发
		getUserMService().forwardWhole(TranslationMessageDown.MessageEnum.CommondId_VALUE, data);
	}

	// 转发组消息
	private void forwardGroupTranslate(SendMessageResult result, String translateResult,
			AModuleMessageCreater moduleMessageCreater) {
		Map<String, String> translate = translateStr2Map(translateResult);
		result.setTranslateResult(translate);
		result.getChatMessage().setTranslationStr(translateResult);
		this.service.insertChatMessage(result.getChatMessage());
		byte[] data = moduleMessageCreater.createTranslationMessageDownBytes(result.getChatMessage(), translate,
				result.getRoomType().getValue(), false);
		// 转发
		if (result.getOnlineUserInfo() != null) {
			for (Entry<String, List<String>> entry : result.getOnlineUserInfo().getOnlineUserMap().entrySet()) {
				getUserMService().forwardGroup(TranslationMessageDown.MessageEnum.CommondId_VALUE, data,
						entry.getValue(), entry.getKey());
			}
		}
	}

	private void forwardUnionOrRegionTranslate(SendMessageResult result, String translateResult,
			AModuleMessageCreater moduleMessageCreater, IPlayer player) {
		Map<String, String> translate = translateStr2Map(translateResult);
		result.setTranslateResult(translate);
		result.getChatMessage().setTranslationStr(translateResult);
		this.service.insertChatMessage(result.getChatMessage());
		IMessage msg = moduleMessageCreater.createTranslationMessageDown(player.getUserId(), result.getChatMessage(),
				translate, result.getRoomType().getValue(), false);
		player.send(msg);
	}

	// --------------------------------
	// 转发单人消息
	private void forwardSingleMessage(IPlayer player, String targetUid, SendMessageResult result,
			String translateResult, AModuleMessageCreater moduleMessageCreater) {
		Map<String, String> translate = translateStr2Map(translateResult);
		result.setTranslateResult(translate);

		// 创建消息
		long tUid = Long.parseLong(targetUid);
		IMessage msg = moduleMessageCreater.createSendChatMessageDown(tUid, result.getChatRoom(),
				result.getChatMessage(), translate, result.getRoomType());

		if (player.getUserId() != tUid) {
			// 转发，TODO 如果可以同个账号同时不同设备登陆，可能有问题。暂时不考虑
			getUserMService().forwardSingle(msg, targetUid);
		}
		// 发送
		player.send(msg);
	}

	// 转发世界消息
	private void forwardWorldMessage(SendMessageResult result, String translateResult,
			AModuleMessageCreater moduleMessageCreater) {
		Map<String, String> translate = translateStr2Map(translateResult);
		result.setTranslateResult(translate);

		byte[] data = moduleMessageCreater.createSendChatMessageDownBytes(result.getChatRoom(), result.getChatMessage(),
				translate, result.getRoomType());
		// 转发
		getUserMService().forwardWhole(SendChatMessageDown.MessageEnum.CommondId_VALUE, data);
	}

	// 转发组消息
	private void forwardGroupMessage(SendMessageResult result, String translateResult,
			AModuleMessageCreater moduleMessageCreater) {
		Map<String, String> translate = translateStr2Map(translateResult);
		result.setTranslateResult(translate);

		byte[] data = moduleMessageCreater.createSendChatMessageDownBytes(result.getChatRoom(), result.getChatMessage(),
				translate, result.getRoomType());
		// 转发
		if (result.getOnlineUserInfo() != null) {
			for (Entry<String, List<String>> entry : result.getOnlineUserInfo().getOnlineUserMap().entrySet()) {
				getUserMService().forwardGroup(SendChatMessageDown.MessageEnum.CommondId_VALUE, data, entry.getValue(),
						entry.getKey());
			}
		}
	}

	private Map<String, String> translateStr2Map(String translateResult) {
		Map<String, String> translate = null;
		if (translateResult != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("###### TranslateResult = " + translateResult);
			}
			translate = JsonUtil.gsonString2Obj(translateResult, Map.class);

			// 现在翻译返回的语言的iso是谷歌的，需要转成标准的
			List<LanguageType> changeLT = new LinkedList<>();
			for (Entry<String, String> entry : translate.entrySet()) {
				LanguageType languageType = LanguageType.getLanguageTypeByGoogle(entry.getKey());
				if (languageType != null && !entry.getKey().equals(languageType.getIsoCode())) {
					changeLT.add(languageType);
				}
			}
			for (LanguageType lt : changeLT) {
				// 替换翻译的key
				String v = translate.remove(lt.getGoogleCode());
				translate.put(lt.getIsoCode(), v);
			}
		}
		return translate;
	}

	// 调用翻译服务
	private String translate(SendMessageResult result, Set<LanguageType> toLangs, ARequestCallback callback) {
		String msg = result.getContent().getMsg();
		if (msg == null || msg.isEmpty()) {
			return null;
		}
		msg = msg.replaceAll("\"", "\'");// 这里为了后边能json解析，替换"符号
		service.translate(null, msg, toLangs, callback);

		String translateResult = null;
		try {
			translateResult = callback.getFuture().get();
		} catch (Exception e) {
			logger.error("", e);
		}
		return translateResult;
	}

	private void sendErrorMessage(IPlayer player, HandeResult handeResult, int commandId) {
		HandleErrorType handleErrorType = handeResult.getErrorType();
		// 发送错误消息
		getErrorMService().sendErrorMessage(ErrorType.ParamError, handleErrorType.getValue(), commandId, player);
	}

	protected IUserMService getUserMService() {
		return service.getModuleService(ModuleServiceType.User);
	}

	protected IErrorMService getErrorMService() {
		return service.getModuleService(ModuleServiceType.Error);
	}
}
