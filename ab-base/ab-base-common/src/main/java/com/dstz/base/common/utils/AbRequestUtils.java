package com.dstz.base.common.utils;

import cn.hutool.extra.servlet.ServletUtil;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * Request Tool
 *
 */
public class AbRequestUtils {

	private AbRequestUtils() throws IllegalAccessException {
		throw new IllegalAccessException();
	}

	/**
	 * Get HttpServletRequest
	 *
	 * @return HttpServletRequest
	 */
	public static HttpServletRequest getHttpServletRequest() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes == null) {
			return null;
		}
		return ((ServletRequestAttributes) requestAttributes).getRequest();
	}

	/**
	 * Get HttpServletResponse
	 *
	 * @return HttpServletResponse
	 */
	public static HttpServletResponse getHttpServletResponse() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes == null) {
			return null;
		}
		return ((ServletRequestAttributes) requestAttributes).getResponse();
	}

	/**
	 * Get the current IP
	 * @return ip
	 */
	public static String getRequestIp() {
		String clientIp = ServletUtil.getClientIP(getHttpServletRequest());
		return "0:0:0:0:0:0:0:1".equals(clientIp) ? "127.0.0.1" : clientIp;
	}

}
