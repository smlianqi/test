package com.elex.im.module.serverclient.imitateclient;

/**
 * 启动战场服务，临时测试服务
 * 
 * @author mausmars
 *
 */
public class ClientServerStart {
	public static void main(String[] args) {
		ClientServer server = new ClientServer();
		server.init();
		server.start();
	}
}
