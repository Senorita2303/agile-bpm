package com.dstz.base.common.jackson;

import com.fasterxml.jackson.databind.ser.BeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;

/**
 * Property Filters
 *
 */
public class OnlyFilterProvider extends FilterProvider {

    private final PropertyFilter propertyFilter;

    public OnlyFilterProvider(PropertyFilter propertyFilter) {
        this.propertyFilter = propertyFilter;
    }

    @Deprecated
    @Override
    public BeanPropertyFilter findFilter(Object filterId) {
        throw new UnsupportedOperationException("Access to deprecated filters not supported");
    }

    @Override
    public PropertyFilter findPropertyFilter(Object filterId, Object valueToFilter) {
        return propertyFilter;
    }
}
