package com.dstz.base.common.valuemap;

import cn.hutool.core.util.StrUtil;
import com.dstz.base.common.valuemap.loader.AbNullValueMapLoader;

import java.lang.annotation.*;

/**
 * Value Mapping
 *
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AbValueMap {

    /**
     * Mapping Type
     *
     * @return Mapping Type
     */
    AbValueMapType type();

    /**
     * Loader
     *
     * @return Loader
     */
    Class<? extends AbValueMapLoader<?, ?>> loader() default AbNullValueMapLoader.class;

    /**
     * Fixed value
     *
     * @return Fixed value
     */
    String fixValue() default StrUtil.EMPTY;

    /**
     * Match Fields
     *
     * @return Match Fields
     */
    String matchField() default StrUtil.EMPTY;

    /**
     * Fixed Class
     *
     * @return Fixed Class
     */
    Class<?>[] fixClass() default {};

    /**
     * Field mapping, if not configured, all fields of the loader will be rendered
     *
     * @return Field Mapping
     */
    AttrMap[] attrMap() default {};

    /**
     * Attribute Mapping
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({})
    @interface AttrMap {

        /**
         * Original field name
         *
         * @return Original field name
         */
        String originName();

        /**
         * Target field name, can be empty
         *
         * @return Mapping target field name
         */
        String targetName() default StrUtil.EMPTY;
    }
}
