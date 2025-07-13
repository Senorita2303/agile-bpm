package com.dstz.base.common.valuemap;

import java.util.Collection;
import java.util.Map;

/**
 * ab value map loader
 *
 */
public interface AbValueMapLoader<K, V> {

    /**
     * Loading Mapping
     *
     * @param abValueMap Value Mapper
     * @param mapKeys    Mapping Keys
     * @return Associated value, the value should return a Map or object
     */
    Map<K, V> loading(AbValueMap abValueMap, Collection<K> mapKeys);

}
