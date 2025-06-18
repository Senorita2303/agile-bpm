package com.dstz.base.common.utils;

/**
 * Type conversion, often used to eliminate list dictionary conversion warnings
 *
 */
public class CastUtils {

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object object) {
        return (T) object;
    }

}
