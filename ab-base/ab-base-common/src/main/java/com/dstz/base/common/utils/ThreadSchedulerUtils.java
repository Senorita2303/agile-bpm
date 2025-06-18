package com.dstz.base.common.utils;

import cn.hutool.core.thread.ThreadUtil;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 线程调度器工具类
 *
 * @author wacxhs
 */
public class ThreadSchedulerUtils {

	private static final class Singleton {
		static final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(2, ThreadUtil.newNamedThreadFactory("ab-scheduler-", true));
	}

	/**
	 * 获取调度执行器
	 *
	 * @return 调度执行器
	 */
	public static ScheduledExecutorService getScheduledExecutorService() {
		return Singleton.scheduledThreadPoolExecutor;
	}

}
