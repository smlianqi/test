package com.elex.im.module.serverclient.imitateclient.module.flowchart;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.elex.common.component.player.IPlayer;
import com.elex.common.component.player.type.PlayerMServiceType;
import com.elex.common.util.json.JsonUtil;
import com.elex.common.util.string.StringUtil;
import com.elex.im.module.serverclient.imitateclient.IClientContext;
import com.elex.im.module.serverclient.imitateclient.module.usertestconfig.IUserTestConfigManager;
import com.elex.im.module.serverclient.imitateclient.module.usertestconfig.UserTestConfig;
import com.google.protobuf.GeneratedMessage;

/**
 * 指令流程服务
 * 
 * @author mausmars
 *
 */
public class FlowchartService implements IFlowchartService {
	private static final int InitFlowKeyId = 1;
	private static Map<Class<?>, Class<?>> classMap = new HashMap<>();
	private static Map<Class<?>, IMessageCreater> messageCreaterMap = new HashMap<>();

	static {
		classMap.put(Integer.class, int.class);
		classMap.put(Long.class, long.class);
		classMap.put(Double.class, double.class);
		classMap.put(Float.class, float.class);
	}

	private IPlayer player;
	private boolean isContinue;

	public FlowchartService(IPlayer player) {
		this.player = player;
		this.isContinue = true;
	}

	/**
	 * 发送指令
	 */
	public void sendCommand() {
		IClientContext context = player.getContext();
		IUserTestConfigManager userTestConfigManager = context.getUserTestConfigManager();
		UserTestConfig userTestConfig = userTestConfigManager.getUserTestConfig(player.getUserId());

		FlowChartType flowChartType = userTestConfig.getFlowChartType();
		switch (flowChartType) {
		case Single:
			isContinue = singleCommand(userTestConfig);
			break;
		default:
			break;
		}
	}

	private boolean singleCommand(UserTestConfig userTestConfig) {
		String className = userTestConfig.getAttach("class_name");
		String paramsStr = userTestConfig.getAttach("params");

		Map<String, Object> params = JsonUtil.transferJsonTOJavaBean(paramsStr, Map.class);
		Object message = createMessage(className, params);
		player.send(message);
		// 不再执行
		return false;
	}

	// 创建消息
	private Object createMessage(String classPath, Map<String, Object> params) {
		try {
			Class<?> clazz = Class.forName(classPath);
			if (messageCreaterMap.containsKey(clazz)) {
				IMessageCreater messageCreater = messageCreaterMap.get(clazz);
				return messageCreater.create(params);
			}
			Method newBuilderMethod = clazz.getDeclaredMethod("newBuilder", null);
			GeneratedMessage.Builder<?> builder = (GeneratedMessage.Builder<?>) newBuilderMethod.invoke(null, null);

			for (Entry<String, Object> entry : params.entrySet()) {
				String key = entry.getKey();
				String str = StringUtil.upperFirstString(key);
				Object obj = entry.getValue();
				if ("userId".equals(key)) {
					obj = String.valueOf(player.getUserId());
				}
				Class<?> paramsClass = obj.getClass();
				if (classMap.containsKey(obj.getClass())) {
					paramsClass = classMap.get(paramsClass);
				}
				Method setMethod = builder.getClass().getMethod("set" + str, paramsClass);
				setMethod.invoke(builder, obj);
			}
			Method buildMethod = builder.getClass().getMethod("build", null);
			return buildMethod.invoke(builder, null);
		} catch (Exception e) {
			throw new RuntimeException("CreateMessage is Error! classPath=" + classPath, e);
		}
	}

	@Override
	public PlayerMServiceType getType() {
		return PlayerMServiceType.Flowchart;
	}

	private IClientContext getContext() {
		return player.getContext();
	}

	@Override
	public boolean isContinue() {
		return isContinue;
	}
}
