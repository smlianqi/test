package com.elex.common.component.netdelaycheck;

import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

/**
 * server端网络延时
 * 
 * @author mausmars
 *
 */
public class NetDelayCheckServer implements INetDelayCheckServer {
	protected static final ILogger logger = XLogUtil.logger();

	private int version = 0;// 计数器

	private DelayInfo bean;
	private DelayInfo beanFinished;// 完成一次检查的

	public NetDelayCheckServer() {
		beanFinished = new DelayInfo();
		beanFinished.setStep(CheckStepType.CS_ReplyClientDelay.value());
		beanFinished.setDelay(0);
	}

	@Override
	public DelayInfo requestServerTime(long ctime) {
		this.version++;

		DelayInfo bean = new DelayInfo();
		bean.setStime(System.currentTimeMillis());
		bean.setVersion(version);
		bean.setCtime(ctime);
		bean.setStep(CheckStepType.CS_ReplyServerTime.value());
		this.bean = bean;
		return bean;
	}

	@Override
	public void replyClientDelay(int version, long ctime) {
		if (bean.getVersion() == version && bean.getStep() == CheckStepType.CS_ReplyServerTime.value()) {
			bean.setStep(CheckStepType.CS_ReplyClientDelay.value());

			int delay = (int) (((System.currentTimeMillis() - bean.getStime()) + (ctime - bean.getCtime())) / 2);
			bean.setDelay(delay);

			beanFinished = bean;// 替换检查完成的
		} else {
			logger.warn("ReplyClientDelay version or step is error!!!");
		}
	}

	@Override
	public int getDelayTime() {
		if (beanFinished.getStep() == CheckStepType.CS_ReplyClientDelay.value()) {
			return beanFinished.getDelay();
		} else {
			logger.warn("NetDelayTime isn't accurate!!!");
			return 0;
		}
	}
}
