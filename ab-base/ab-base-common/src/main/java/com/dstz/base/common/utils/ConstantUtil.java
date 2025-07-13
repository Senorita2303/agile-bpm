package com.dstz.base.common.utils;

import com.fasterxml.jackson.databind.JsonNode;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Tool class for constants
 *
 */
public class ConstantUtil {
    private ConstantUtil() {

    }

    /**
     * <pre>
     * Get a static constant under the specified class
     * ps: That key must be final static to be obtained
     * </pre>
     *
     * @param classPath
     * @param key
     * @return Return json:{key:"field name",value:"field value",type:"field type"}
     */
    public static JsonNode get(String classPath, String key) {
        JsonNode json = get(classPath).get(key);
        if (json != null) {
            return json;
        }
        throw new RuntimeException("In the category " + classPath + " unable to obtain constants [" + key + "]");
    }

    /**
     * <pre>
     * Get all final static constants under a static constant in a specified class
     * </pre>
     *
     * @param classPath
     * @return Return map:{key:{key:"field name",value:"field value",type:"field type"},key1:{key:"field name 1",value:"field value 1",type:"field type 1"}}
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

