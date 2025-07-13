package com.dstz.base.common.utils;

import cn.hutool.core.map.MapUtil;
import com.dstz.base.common.requestlog.AbRequestLog;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

/**
 * Audit log variable tool
 *
 */
public class AuditLogVariableUtil {


	private static Map<String, Object> getVariableMap(HttpServletRequest request) {
		if (request == null) {
			return null;
		}
		// Get from thread
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
	 * Get the variable map from the request log
	 *
	 * @param abRequestLog Request log
	 * @return Variable Map
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
	 * Set variables
	 *
	 * @param name  Variable name
	 * @param value Variable value
	 */
	public static void setVariable(String name, Object value) {
		Map<String, Object> variableMap = getVariableMap(AbRequestUtils.getHttpServletRequest());
		if (variableMap != null) {
			variableMap.put(name, value);
		}
	}
}
