package com.dstz.base.common.utils;

import com.fasterxml.jackson.databind.JsonNode;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * 常量的工具类
 *
 * @author aschs
 */
public class ConstantUtil {
    private ConstantUtil() {

    }

    /**
     * <pre>
     * 获取指定类下的某个静态常量
     * ps:那个key 一定是final static 的才能被获取
     * </pre>
     *
     * @param classPath
     * @param key
     * @return 返回json:{key:"字段名",value:"字段值",type:"字段的类型"}
     */
    public static JsonNode get(String classPath, String key) {
        JsonNode json = get(classPath).get(key);
        if (json != null) {
            return json;
        }
        throw new RuntimeException("类中" + classPath + "获取不到常量[" + key + "]");
    }

    /**
     * <pre>
     * 获取指定类下的某个静态常量下所有final static的常量
     * </pre>
     *
     * @param classPath
     * @return 返回map:{key:{key:"字段名",value:"字段值",type:"字段的类型"},key1:{key:"字段名1",value:"字段值1",type:"字段的类型1"}}
     */
    public static Map<String, JsonNode> get(String classPath) {
        try {
            Map<String, JsonNode> map = new HashMap<>();
            Class<?> clazz = Class.forName(classPath);
            Field[] fileds = clazz.getFields();
            for (Field f : fileds) {
                if (Modifier.isPublic(f.getModifiers()) && Modifier.isFinal(f.getModifiers()) && Modifier.isStatic(f.getModifiers())) {
                    Map<String,Object> json=new HashMap<>();
                    json.put("key", f.getName());
                    json.put("value", f.get(clazz));
                    json.put("type", f.getType());
                    map.put(f.getName(), JsonUtils.toJSONNode(json));
                }
            }
            return map;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

