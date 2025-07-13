package com.dstz.base.common.property;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.Objects;

public interface IBaseProperty {


    /**
     * Get attribute key
     * @param
     * @return
     */
    String getKey();

    /**
     * Get attribute description
     * @return
     */
    String getDesc();

    /**
     * Get default values
     * @return
     */
    Object getDefaultValue();

    /**
     * Get the configuration property value of the YAML file
     *
     * @param typeClass Attribute value type
     * @param <T>       Attribute Class
     * @return Attribute Value
     */
    default <T> T getYamlValue(Class<T> typeClass) {
        Object value = ObjectUtil.defaultIfNull(SpringUtil.getProperty(getKey()), getDefaultValue());
        return Objects.isNull(value) ? null : Convert.convert(typeClass, value);
    }

    /**
     * Get system property values
     *
     * @param typeClass Attribute Type
     * @param <T>       Attribute Class
     * @return Property Value
     */
    default <T> T getPropertyValue(Class<T> typeClass) {
        SysPropertyService sysPropertyService = SpringUtil.getBean(SysPropertyService.class);
        Object value = ObjectUtil.defaultIfNull(sysPropertyService.getValByCode(getKey()), getDefaultValue());
        return Objects.isNull(value) ? null : DefaultConversionService.getSharedInstance().convert(value, typeClass);
    }
}
