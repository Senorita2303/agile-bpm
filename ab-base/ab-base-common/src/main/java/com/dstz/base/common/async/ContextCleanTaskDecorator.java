package com.dstz.base.common.async;

import com.dstz.base.common.utils.ContextCleanUtils;
import org.springframework.core.task.TaskDecorator;

/**
 * Context cleanup decorator
 *
 */
public class ContextCleanTaskDecorator implements TaskDecorator {

	public static final ContextCleanTaskDecorator INSTANCE = new ContextCleanTaskDecorator();

	@Override
	public Runnable decorate(Runnable runnable) {
		return () -> {
			ContextCleanUtils.executeAll();
			try {
				// Copy Object Fill
				runnable.run();
			} finally {
				ContextCleanUtils.executeAll();
			}
		};
	}
}
