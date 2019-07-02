package com.elex.im.module.serverclient.imitateclient.module.flowchart;

import java.util.Map;

import com.google.protobuf.GeneratedMessage;

/**
 * 消息创建者
 * 
 * @author mausmars
 *
 */
public interface IMessageCreater {
	GeneratedMessage create(Map<String, Object> params);
}
