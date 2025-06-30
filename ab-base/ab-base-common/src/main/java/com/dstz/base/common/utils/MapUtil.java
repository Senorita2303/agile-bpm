package com.dstz.base.common.utils;

import java.util.Map;

public class MapUtil extends cn.hutool.core.map.MapUtil {
	/**
	 * <pre>
	 * 根据路径获取map
	 * eg:
	 * map= {a:{b:1}}
	 * path= a.b
	 * 结果：1
	 * </pre>	
	 * 
	 * @param map
	 * @param pathStr
	 * @return
	 */
	public static Object getValByPath(Map<String, Object> map, String path) {
		String[] strs = path.split("\\.");

		Map<String, Object> currentMap = map;
		for (int i = 0; i < strs.length; i++) {
			Object value = currentMap.get(strs[i]);
			if (value instanceof Map && i < strs.length - 1) {
				currentMap = (Map<String, Object>) value;
			} else {
				return value;
			}
		}
		return null;
	}
}
