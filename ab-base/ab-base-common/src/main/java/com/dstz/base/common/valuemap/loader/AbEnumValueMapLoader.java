package com.dstz.base.common.valuemap.loader;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Converter;
import cn.hutool.core.convert.ConverterRegistry;
import cn.hutool.core.util.*;
import com.dstz.base.common.valuemap.AbValueMap;
import com.dstz.base.common.valuemap.AbValueMapLoader;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Enumeration Value Loader
 *
 */
public class AbEnumValueMapLoader implements AbValueMapLoader<Object, Object> {

    @Override
    public Map<Object, Object> loading(AbValueMap abValueMap, Collection<Object> mapKeys) {
        if (ArrayUtil.length(abValueMap.fixClass()) != 1 &&  !EnumUtil.isEnum(abValueMap.fixClass()[0])) {
            throw new IllegalArgumentException(ClassUtil.getClassName(abValueMap.fixClass(), false) + " 非枚举类");
        }
        final Class<?> enumClass = abValueMap.fixClass()[0];
        // Reflection to get the enumeration field type
        Method keyMethod;
        if (StrUtil.isEmpty(abValueMap.matchField())) {
            // key, code, and type are commonly used. If not found, name matching is used.
            keyMethod = Stream.of("key", "code", "type").map(fieldName -> BeanUtil.getPropertyDescriptor(enumClass, fieldName)).filter(Objects::nonNull).findFirst().map(PropertyDescriptor::getReadMethod).orElseGet(() -> ReflectUtil.getMethodByName(enumClass, "name"));
        } else {
            keyMethod = BeanUtil.getPropertyDescriptor(enumClass, abValueMap.matchField()).getReadMethod();
        }
        Converter<Object> converter = ConverterRegistry.getInstance().getDefaultConverter(keyMethod.getReturnType());
        // Reflection type mapping for matching
        Map<Object, Object> mapKeyTypeMap = new HashMap<>(mapKeys.size());
        for (Object mapKey : mapKeys) {
            mapKeyTypeMap.put(converter.convert(mapKey, null), mapKey);
        }
        Map<Object, Object> dataMap = new HashMap<>(mapKeys.size());
        for (Object enumConstant : enumClass.getEnumConstants()) {
            Object fieldValue = ReflectUtil.invoke(enumConstant, keyMethod);
            Object refKey = mapKeyTypeMap.get(fieldValue);
            if (mapKeyTypeMap.containsKey(fieldValue)) {
                dataMap.put(refKey, enumConstant);
            }
        }
        return dataMap;
    }


}
