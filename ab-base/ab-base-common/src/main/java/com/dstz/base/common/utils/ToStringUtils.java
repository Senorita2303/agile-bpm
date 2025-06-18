package com.dstz.base.common.utils;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <pre>
 * 描述：类转字符串工具，字符串格式如下：
 * 类名[字段a:a,字段b:b,...]
 * 作者:aschs
 * 邮箱:aschs@agilebpm.cn
 * 日期:2020年3月8日 上午9:37:48
 * 版权: 深圳市大世同舟信息科技有限公司
 * </pre>
 *
 * @author aschs
 */
public class ToStringUtils {
	private ToStringUtils() {

	}

	/**
	 * 对象字段信息字符串化
	 *
	 * @param object
	 *            对象
	 * @return 对象信息字符串
	 */
	public static String toString(Object object) {
		return ToStringBuilder.reflectionToString(object, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
