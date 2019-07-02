package com.elex.im.test.net.handler;

import java.io.IOException;
import java.util.List;

import com.elex.common.net.handler.IMessageInHandler;
import com.elex.common.net.message.MessageConfig;
import com.elex.common.net.service.netty.filter.http.HttpResponseMessage;
import com.elex.common.net.session.ISession;
import com.elex.im.test.net.message.LoginMessage;

public class LoginUpdataHandler implements IMessageInHandler<LoginMessage> {
	@Override
	public void inhandle(LoginMessage message, ISession session, Object attr) {
		List<String> a = message.getAParamsValue();
		System.out.println(a);

		HttpResponseMessage responseMessage = new HttpResponseMessage();
		// responseMessage.writeUTF("<html>");
		// responseMessage.writeUTF("<head>");
		// responseMessage.writeUTF("<title>放置文章标题</title>");
		// responseMessage.writeUTF("<meta http-equiv=\"content-type\"
		// content=\"text/html;charset=gb2312\">");
		// responseMessage.writeUTF("</head>");
		// responseMessage.writeUTF("<body>");
		// responseMessage.writeUTF("这里就是正文内容 ");
		// responseMessage.writeUTF("</body>");
		// responseMessage.writeUTF("</html>");

		try {
			responseMessage.writeUTF("a=" + a);
		} catch (IOException e) {
			e.printStackTrace();
		}
		session.send(responseMessage);
	}

	public MessageConfig createMessageConfig() {
		MessageConfig messageConfig = new MessageConfig();
		messageConfig.setKey("/login");
		messageConfig.setMessage(LoginMessage.class);
		messageConfig.setMessageHandler(this);
		return messageConfig;
	}
}
