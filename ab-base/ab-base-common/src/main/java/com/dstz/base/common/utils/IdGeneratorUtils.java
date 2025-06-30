package com.dstz.base.common.utils;

import cn.hutool.extra.spring.SpringUtil;
import com.dstz.base.common.idgen.IdGenerator;

/**
 * ID generator tool class
 *
 */
public class IdGeneratorUtils {

    private static volatile IdGenerator idGenerator;

    private IdGeneratorUtils() throws IllegalAccessException {
        throw new IllegalAccessException();
    }


    /**
     * Unique ID
     *
     * @return Unique ID
     */
    public static String nextId() {
        if (idGenerator == null) {
            synchronized (IdGeneratorUtils.class) {
                if (idGenerator == null) {
                    idGenerator = SpringUtil.getBean(IdGenerator.class);
                }
            }
        }
        return idGenerator.nextId();
    }
}
