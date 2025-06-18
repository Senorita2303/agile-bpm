package com.dstz.base.common.aop.annotion;

import java.lang.annotation.*;

/**
 * ab Scheduling
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.METHOD)
public @interface AbScheduled {

	/**
	 * Scheduling task KEY, can also be used in other middleware
	 *
	 * @return Scheduling Task KEY
	 */
	String jobKey();
}
