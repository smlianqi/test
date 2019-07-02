package com.elex.im.module.serverlogic;

/**
 * 启动战场服务，临时测试服务
 * 
 * @author mausmars
 *
 */
public class LogicServerStart {
	public static void main(String[] args) {
		LogicServer server = new LogicServer();
		server.init();
		server.start();
	}
}
