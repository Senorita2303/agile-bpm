package com.dstz.base.common.utils;

import cn.hutool.core.map.MapUtil;
import com.dstz.base.common.requestlog.AbRequestLog;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

/**
 * 审计日志变量工具
 *
 * @author wacxhs
 */
public class AuditLogVariableUtil {


	private static Map<String, Object> getVariableMap(HttpServletRequest request) {
		if (request == null) {
			return null;
		}
		// 从线程中获取
		AbRequestLog abRequestLog = (AbRequestLog) request.getAttribute(AbRequestLog.class.getName());
		if (abRequestLog == null) {
			return null;
		}
		Map<String, Object> variableMap = CastUtils.cast(abRequestLog.getAttribute(AuditLogVariableUtil.class));
		if (variableMap == null) {
			abRequestLog.bindAttribute(AuditLogVariableUtil.class, variableMap = MapUtil.newHashMap());
		}
		return variableMap;
	}

	/**
	 * 从请求日志中获取到变量Map
	 *
	 * @param abRequestLog 请求日志
	 * @return 变量Map
	 */
	public static Map<String, Object> getVariableMap(AbRequestLog abRequestLog) {
		if (abRequestLog == null) {
			return Collections.emptyMap();
		}
		Object attribute = abRequestLog.getAttribute(AuditLogVariableUtil.class);
		if (attribute == null) {
			return Collections.emptyMap();
		}
		return CastUtils.cast(attribute);
	}

	/**
	 * 设置变量
	 *
	 * @param name  变量名
	 * @param value 变量值
	 */
	public static void setVariable(String name, Object value) {
		Map<String, Object> variableMap = getVariableMap(AbRequestUtils.getHttpServletRequest());
		if (variableMap != null) {
			variableMap.put(name, value);
		}
	}
}
