package com.elex.im.module.serverchat;

/**
 * 启动战场服务，临时测试服务
 * 
 * @author mausmars
 *
 */
public class ChatServerStart {
	public static void main(String[] args) {
		ChatServer server = new ChatServer();
		server.init();
		server.start();
	}
}
