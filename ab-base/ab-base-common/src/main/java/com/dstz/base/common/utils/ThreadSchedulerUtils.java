package com.dstz.base.common.utils;

import cn.hutool.core.thread.ThreadUtil;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Thread scheduler tool class
 *
 */
public class ThreadSchedulerUtils {

	private static final class Singleton {
		static final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(2, ThreadUtil.newNamedThreadFactory("ab-scheduler-", true));
	}

	/**
	 * Get the scheduler executor
	 *
	 * @return Scheduling executor
	 */
	public static ScheduledExecutorService getScheduledExecutorService() {
		return Singleton.scheduledThreadPoolExecutor;
	}

}
