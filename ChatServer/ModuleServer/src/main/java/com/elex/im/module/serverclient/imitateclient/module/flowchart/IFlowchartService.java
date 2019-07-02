package com.elex.im.module.serverclient.imitateclient.module.flowchart;

import com.elex.common.component.player.service.IPlayerMService;

/**
 * 指令流程服务
 * 
 * @author mausmars
 *
 */
public interface IFlowchartService extends IPlayerMService {
	/**
	 * 发送指令
	 */
	void sendCommand();

	boolean isContinue();
}
