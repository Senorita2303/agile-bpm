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
 * 枚举的工具类
 *
 * @author aschs
 */
public class EnumUtil {
    /**
     * 防止被实例化
     */
    private EnumUtil() {

    }

    /**
     * <pre>
     * 把一个枚举类转成JSON，主要是为了方便前端直接调用(以下是jsp的用法)
     * 前端调用例子：
     * <%@page import="com.dstz.sys.persistence.enums.FieldControlType"%>
     * <%@page import="com.dstz.base.core.util.EnumUtil"%>
     * <script type="text/javascript">
     * var FieldControlType = <%=EnumUtil.toJSON(FieldControlType.class)%>;
     * </script>
     * 系统内置的异步获取类：toolsControllerUtil.js
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
     * 把一个枚举类的路径转成json数组
     * 注意！！如果枚举在类中间，那么路径如下：com.dstz.base.db.model.Column$TYPE
     * </pre>
     *
     * @param enumClassPath 枚举路径
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
     * 把一个枚举实例转成JSON，主要是为了方便前端直接调用
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
            // 让私有变量也能被访问到
            field.setAccessible(true);
            // 过滤掉自身的枚举（我都想不到枚举实例为什么也算是类的字段）和枚举类必有的全部变量的ENUM$VALUES字段
            if ("ENUM$VALUES".equals(field.getName()) || field.getType().equals(e.getClass())) {
                continue;
            }
            Object obj = field.get(e);
            if (obj instanceof Enum) {// 如果类型是Enum，那刚好继续解释多一次
                result.put(field.getName(), toJSON((Enum<?>) field.get(e)));
            } else {
                result.put(field.getName(), field.get(e));
            }

        }
        return JsonUtils.toJSONNode(result);
    }

    /**
     * 加载枚举额外数据
     *
     * @param clazz    枚举类
     * @param consumer 处理数据
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
