package com.dstz.base.common.utils;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <pre>
 * Description: Class to string tool, the string format is as follows:
 * Class name [field a: a, field b: b,...]
 * </pre>
 *
 */
public class ToStringUtils {
	private ToStringUtils() {

	}

	/**
	 * Stringify object field information
	 *
	 * @param object
	 *            Object
	 * @return Object information string
	 */
	public static String toString(Object object) {
		return ToStringBuilder.reflectionToString(object, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
