package com.dstz.base.common.utils;

/**
 * 类型转换，常用于消除列表字典转换警告
 *
 * @author wacxhs
 */
public class CastUtils {

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object object) {
        return (T) object;
    }

}
