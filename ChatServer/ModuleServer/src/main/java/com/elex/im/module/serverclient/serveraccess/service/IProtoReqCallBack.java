package com.elex.im.module.serverclient.serveraccess.service;

import com.elex.common.component.player.IPlayer;
import com.elex.im.message.proto.ChatMessage.CreateChatRoomDown;
import com.elex.im.message.proto.ChatMessage.GainChatMessageDown;
import com.elex.im.message.proto.ChatMessage.ManagerChatRoomMemberDown;
import com.elex.im.message.proto.ChatMessage.SendChatMessageDown;
import com.elex.im.message.proto.ChatMessage.TranslationMessageDown;
import com.elex.im.message.proto.ErrorMessage.ErrorMessageDown;
import com.elex.im.message.proto.ErrorMessage.SuccessMessageDown;
import com.elex.im.message.proto.UserMessage.UserLoginDown;

public interface IProtoReqCallBack extends IReqCallBack {
	/**
	 * 
	 * @param userId
	 * @param message
	 */
	void userLoginClassBack(IPlayer player, UserLoginDown message);

	/**
	 * 
	 * @param userId
	 * @param message
	 */
	void gainChatMessageClassBack(IPlayer player, GainChatMessageDown message);

	/**
	 * 
	 * @param userId
	 * @param message
	 */
	void createChatRoomClassBack(IPlayer player, CreateChatRoomDown message);

	/**
	 * 
	 * @param userId
	 * @param message
	 */
	void managerChatRoomMemberClassBack(IPlayer player, ManagerChatRoomMemberDown message);

	/**
	 * 
	 * @param userId
	 * @param message
	 */
	void sendChatMessageClassBack(IPlayer player, SendChatMessageDown message);

	/**
	 * 
	 * @param userId
	 * @param message
	 */
	void translationMessageClassBack(IPlayer player, TranslationMessageDown message);

	// -------------------------------------
	/**
	 * 
	 * @param userId
	 * @param message
	 */
	void errorMessageClassBack(IPlayer player, ErrorMessageDown message);

	/**
	 * 
	 * @param userId
	 * @param message
	 */
	void successMessageClassBack(IPlayer player, SuccessMessageDown message);
}
