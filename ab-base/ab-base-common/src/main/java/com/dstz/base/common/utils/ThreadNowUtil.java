package com.dstz.base.common.utils;

import java.util.Date;

/**
 * <pre>
 * Reserved entrance
 * </pre>
 * 
 */
public class ThreadNowUtil {
	private ThreadNowUtil() {
		
	}

	public static Date getNow() {
		Date now = new Date();
		return now;
	}
}
