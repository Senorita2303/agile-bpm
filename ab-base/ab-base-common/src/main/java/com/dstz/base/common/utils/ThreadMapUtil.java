package com.dstz.base.common.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * <pre>
 * 描述：线程map的工具类
 * 让开发员随时放自己想要的东西到线程变量中
 * 作者:aschs
 * 邮箱:aschs@agilebpm.cn
 * 版权: 深圳市大世同舟信息科技有限公司
 * </pre>
 */
public class ThreadMapUtil {
	private final static ThreadLocal<Map<String, Object>> threadLocalMap = new ThreadLocal<>();

	static {
		ContextCleanUtils.register(threadLocalMap::remove, ContextCleanUtils.Phase.REQUEST_COMPLETE, ContextCleanUtils.Phase.THREAD);
	}
	
	private ThreadMapUtil() {

	}

	private static Map<String, Object> map() {
		Map<String, Object> map = threadLocalMap.get();
		if (map == null) {
			threadLocalMap.set(new ConcurrentHashMap<String, Object>());
			map = threadLocalMap.get();
		}
		return map;
	}

	public static void put(String key, Object value) {
		map().put(key, value);
	}

	public static Object get(String key) {
		if (threadLocalMap.get() == null) {
			return null;
		}

		return map().get(key);
	}

	public static Object remove(String key) {
		Object obj = get(key);
		map().remove(key);
		if (map().isEmpty()) {
			threadLocalMap.remove();
		}
		return obj;
	}

	public static Object removeDefault(String key, Object defaultValue) {
		Object obj = get(key);
		if (obj == null) {
			obj = defaultValue;
		}
		map().remove(key);
		if (map().isEmpty()) {
			threadLocalMap.remove();
		}
		return obj;
	}

	public static Map getMap() {
		return threadLocalMap.get();
	}

	/**
	 * <pre>
	 * 清除线程变量
	 * </pre>
	 */
	public static void remove() {
		threadLocalMap.remove();
	}

	public static Object getOrDefault(String key, Object defaultValue) {
		return map().getOrDefault(key, defaultValue);
	}

	/**
	 * <pre>
	 * 获取某个值，为空时创建mappingFunction
	 * </pre>
	 * 
	 * @param key
	 * @param mappingFunction
	 * @return
	 */
	public static Object computeIfAbsent(String key, Function<? super String, ? extends Object> mappingFunction) {
		return map().computeIfAbsent(key, mappingFunction);
	}
}
