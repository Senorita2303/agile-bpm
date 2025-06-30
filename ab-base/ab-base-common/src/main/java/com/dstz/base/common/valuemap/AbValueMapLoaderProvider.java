package com.dstz.base.common.valuemap;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.dstz.base.common.valuemap.loader.AbEnumValueMapLoader;
import com.dstz.base.common.valuemap.loader.AbNullValueMapLoader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("all")
public class AbValueMapLoaderProvider {

    private static final Map<AbValueMapType, AbValueMapLoader<?, ?>> valueMapTypeMap = new ConcurrentHashMap<>();

    private static final Map<Class<? extends AbValueMapLoader>, AbValueMapLoader<?, ?>> valueMapLoaderClassMap = new ConcurrentHashMap<>();

    static {
        register(AbValueMapType.ENUM, new AbEnumValueMapLoader());
    }

    public static void register(AbValueMapType abValueMapType, AbValueMapLoader<?, ?> valueMapLoader) {
        valueMapTypeMap.put(abValueMapType, valueMapLoader);
        valueMapLoaderClassMap.put(valueMapLoader.getClass(), valueMapLoader);
    }

    public static AbValueMapLoader<Object, Object> findAbValueMapLoader(AbValueMap abValueMap) {
        AbValueMapLoader abValueMapLoader;
        if (AbNullValueMapLoader.class.equals(abValueMap.loader())) {
            abValueMapLoader = ObjectUtil.defaultIfNull(valueMapTypeMap.get(abValueMap.type()), AbNullValueMapLoader.NULL);
        } else {
            abValueMapLoader = valueMapLoaderClassMap.computeIfAbsent(abValueMap.loader(), SpringUtil::getBean);
        }
        return abValueMapLoader;
    }
}
