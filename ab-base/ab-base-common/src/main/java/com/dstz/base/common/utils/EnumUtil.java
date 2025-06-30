package com.dstz.base.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.context.ApplicationContext;

import com.dstz.base.common.ext.EnumExtraData;
import com.dstz.base.common.ext.IEnumExtraDataLoader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import cn.hutool.extra.spring.SpringUtil;

/**
 * Enumerated tool class
 *
 */
public class EnumUtil {
    /**
     * Prevent from being instantiated
     */
    private EnumUtil() {

    }

    /**
     * <pre>
     * Convert an enumeration class to JSON, mainly to facilitate direct front-end calls (the following is the usage of jsp)
     * Front-end call example:
     * <%@page import="com.dstz.sys.persistence.enums.FieldControlType"%>
     * <%@page import="com.dstz.base.core.util.EnumUtil"%>
     * <script type="text/javascript">
     * var FieldControlType = <%=EnumUtil.toJSON(FieldControlType.class)%>;
     * </script>
     * The system's built-in asynchronous acquisition class: toolsControllerUtil.js
     * </pre>
     *
     * @param enumClass
     * @return
     */
    public static JsonNode toJSON(Class<? extends Enum<?>> enumClass) {
        try {
            Method method = enumClass.getMethod("values");
            Enum<?>[] enums = (Enum[]) method.invoke(enumClass, null);
            Map<String, Object> result = new HashMap<>();
            for (Enum<?> e : enums) {
                result.put(e.name(), toJSON(e));
            }
            loadEnumExtraData(enumClass, enumExtraData -> {
                result.put(enumExtraData.getName(), JsonUtils.toJSONNode(enumExtraData));
            });
            return JsonUtils.toJSONNode(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayNode toJSONArray(Class<? extends Enum<?>> enumClass) {
        try {
            Method method = enumClass.getMethod("values");
            Enum<?>[] enums = (Enum[]) method.invoke(enumClass, null);
            ArrayNode result = JsonNodeFactory.instance.arrayNode();
            for (Enum<?> e : enums) {
                result.add(toJSON(e));
            }
            loadEnumExtraData(enumClass, enumExtraData -> {
                result.add(JsonUtils.toJSONNode(enumExtraData));
            });
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <pre>
     * Convert an enumeration class path into a json array
     * NOTE! ! If the enumeration is in the middle of a class, the path is as follows: com.dstz.base.db.model.Column$TYPE
     * </pre>
     *
     * @param enumClassPath Enumeration path
     * @return
     * @throws Exception
     */
    public static ArrayNode toJSONArray(String enumClassPath) {
        try {
            return toJSONArray((Class<? extends Enum<?>>) Class.forName(enumClassPath));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonNode toJSON(String enumClassPath) {
        try {
            return toJSON((Class<? extends Enum<?>>) Class.forName(enumClassPath));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <pre>
     * Convert an enumeration instance to JSON, mainly to facilitate direct front-end calls.
     * </pre>
     *
     * @param e
     * @return
     * @throws Exception
     */
    private static JsonNode toJSON(Enum<?> e) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Field[] fields = e.getClass().getDeclaredFields();
        for (Field field : fields) {
            // Make private variables accessible
            field.setAccessible(true);
            // Filter out the enumeration itself (I can't figure out why enumeration instances are also considered class fields) and the ENUM$VALUES field of all the variables that the enumeration class must have
            if ("ENUM$VALUES".equals(field.getName()) || field.getType().equals(e.getClass())) {
                continue;
            }
            Object obj = field.get(e);
            if (obj instanceof Enum) {// If the type is Enum, then just continue to explain it one more time
                result.put(field.getName(), toJSON((Enum<?>) field.get(e)));
            } else {
                result.put(field.getName(), field.get(e));
            }

        }
        return JsonUtils.toJSONNode(result);
    }

    /**
     * Load enumeration extra data
     *
     * @param clazz    Enumeration class
     * @param consumer Processing data
     */
    private static void loadEnumExtraData(Class<?> clazz, Consumer<EnumExtraData> consumer) {
        ApplicationContext applicationContext = SpringUtil.getApplicationContext();
        if (applicationContext == null) {
            return;
        }
        Map<String, IEnumExtraDataLoader> beanMap = applicationContext.getBeansOfType(IEnumExtraDataLoader.class);
        if (beanMap != null && !beanMap.isEmpty()) {
            for (IEnumExtraDataLoader enumExtraDataLoader : beanMap.values()) {
                if (!clazz.equals(enumExtraDataLoader.tag())) {
                    continue;
                }
                List<EnumExtraData> enumExtraDataList = enumExtraDataLoader.load();
                if (enumExtraDataList != null && !enumExtraDataList.isEmpty()) {
                    for (EnumExtraData enumExtraData : enumExtraDataList) {
                        consumer.accept(enumExtraData);
                    }
                }
            }
        }
    }
}
