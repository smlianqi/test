package com.elex.im.module.servergate;

/**
 * 启动战场服务，临时测试服务
 * 
 * @author mausmars
 *
 */
public class GateServerStart {
	public static void main(String[] args) {
		GateServer server = new GateServer();
		server.init();
		server.start();
	}
}
