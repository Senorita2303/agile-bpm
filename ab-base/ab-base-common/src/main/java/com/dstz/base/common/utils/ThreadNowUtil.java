package com.dstz.base.common.utils;

import java.util.Date;

/**
 * <pre>
 * 预留入口
 * </pre>
 * 
 * @author aschs
 * @date 2022年3月30日
 * @owner 深圳市大世同舟信息科技有限公司
 */
public class ThreadNowUtil {
	private ThreadNowUtil() {
		
	}

	public static Date getNow() {
		Date now = new Date();
		return now;
	}
}
