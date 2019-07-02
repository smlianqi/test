package com.elex.im.module.common.user.handler.proto;

import com.elex.common.component.player.IPlayer;
import com.elex.common.net.message.MessageConfig;
import com.elex.im.message.proto.UserMessage.ModifyLanguageUp;
import com.elex.im.module.common.user.IUserMService;
import com.elex.im.module.common.user.UserMessageHandler;
import com.elex.im.module.translation.type.LanguageType;

public class ModifyLanguageUpHandler extends UserMessageHandler<ModifyLanguageUp> {
	public ModifyLanguageUpHandler(IUserMService service) {
		super(service);
	}

	public void loginedHandler(ModifyLanguageUp message, IPlayer player) {
		String lt = message.getLanguageType();
		LanguageType languageType = LanguageType.getLanguageType(lt);
		// 修改语言
		service.modifyLanguage(player, languageType);
	}

	public MessageConfig createMessageConfig() {
		MessageConfig messageConfig = new MessageConfig();
		messageConfig.setKey(ModifyLanguageUp.MessageEnum.CommondId_VALUE);
		messageConfig.setMessage(ModifyLanguageUp.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}
