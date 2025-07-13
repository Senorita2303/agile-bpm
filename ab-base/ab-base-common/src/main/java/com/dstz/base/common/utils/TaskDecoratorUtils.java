package com.dstz.base.common.utils;

import org.springframework.core.task.TaskDecorator;

/**
 * task decorator utils
 *
 */
public class TaskDecoratorUtils {

	/**
	 * decorate runnable
	 *
	 * @param target         target
	 * @param taskDecorators taskDecorators
	 * @return decorate runnable
	 */
	public static Runnable decorate(Runnable target, TaskDecorator... taskDecorators) {
		Runnable runnable = target;
		for (int i = taskDecorators.length - 1; i >= 0; i--) {
			runnable = taskDecorators[i].decorate(runnable);
		}
		return runnable;
	}

	/**
	 * build chain task decorator
	 *
	 * @param taskDecorators taskDecorators
	 * @return chain taskDecorators
	 */
	public static TaskDecorator buildChainTaskDecorator(TaskDecorator... taskDecorators) {
		return target -> decorate(target, taskDecorators);
	}
}
