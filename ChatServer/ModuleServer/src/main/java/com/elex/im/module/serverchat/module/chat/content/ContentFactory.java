package com.elex.im.module.serverchat.module.chat.content;

import com.elex.common.util.json.JsonUtil;
import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;
import com.elex.im.module.serverchat.module.chat.type.ContentType;

public class ContentFactory {
	protected static final ILogger logger = XLogUtil.logger();

	public IContent createContent(ContentType contentType, String c) {
		IContent content = null;
		try {
			switch (contentType) {
			case Text: {
				TextContent tcontent = JsonUtil.gsonString2Obj(c, TextContent.class);
				tcontent.setContent(c);
				content = tcontent;
				break;
			}
			case Battle: {
				BattleContent tcontent = JsonUtil.gsonString2Obj(c, BattleContent.class);
				tcontent.setContent(c);
				content = tcontent;
				break;
			}
			case AllianceOperation: {
				AllianceOperationContent tcontent = JsonUtil.gsonString2Obj(c, AllianceOperationContent.class);
				tcontent.setContent(c);
				content = tcontent;
			}
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return content;
	}
}
