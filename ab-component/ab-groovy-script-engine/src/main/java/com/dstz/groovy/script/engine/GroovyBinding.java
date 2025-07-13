package com.dstz.groovy.script.engine;

import groovy.lang.Binding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Groovy Binding
 * 
 */
public class GroovyBinding extends Binding {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private  static final ThreadLocal<Map<String, Object>> LOCAL_VARS = new ThreadLocal<>();

	private static final Map<String, Object> PROPERTY_MAP = new HashMap<>();

	public void setThreadVariables(Map<String, Object> variables) {
		LOCAL_VARS.remove();
		LOCAL_VARS.set(variables);
	}

	@Override
	public Object getVariable(String name) {
		Map<String, Object> localVars = LOCAL_VARS.get();
		Object result = null;
		if (localVars != null) {
			result = localVars.get(name);
		}
		if (result == null) {
			result = PROPERTY_MAP.get(name);
		}
		if (result == null) {
			logger.warn("When executing a Groovy statement, the Context is missing Variable ：{}", name);
		}
		return result;
	}

	@Override
	public void setVariable(String name, Object value) {
		if (LOCAL_VARS.get() == null) {
			Map<String, Object> vars = new LinkedHashMap<>();
			vars.put(name, value);
			LOCAL_VARS.set(vars);
		} else {
			LOCAL_VARS.get().put(name, value);
		}
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Map<?, ?> getVariables() {
		if (LOCAL_VARS.get() == null) {
			return new LinkedHashMap();
		}
		return LOCAL_VARS.get();
	}

	public void clearVariables() {
		LOCAL_VARS.remove();
	}

	@Override
	public Object getProperty(String property) {
		return PROPERTY_MAP.get(property);
	}

	@Override
	public void setProperty(String property, Object newValue) {
		PROPERTY_MAP.put(property, newValue);
	}
}
