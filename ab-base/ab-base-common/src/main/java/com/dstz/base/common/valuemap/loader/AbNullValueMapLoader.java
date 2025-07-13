package com.dstz.base.common.valuemap.loader;

import com.dstz.base.common.valuemap.AbValueMap;
import com.dstz.base.common.valuemap.AbValueMapLoader;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class AbNullValueMapLoader implements AbValueMapLoader<Object, Object> {

    public static final AbNullValueMapLoader NULL = new AbNullValueMapLoader();

    @Override
    public Map<Object, Object> loading(AbValueMap abValueMap, Collection<Object> mapKeys) {
        return Collections.emptyMap();
    }
}
