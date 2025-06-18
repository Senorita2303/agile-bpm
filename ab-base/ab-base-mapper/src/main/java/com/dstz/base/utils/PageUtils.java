package com.dstz.base.utils;


import com.dstz.base.common.enums.GlobalApiCodes;
import com.dstz.base.common.exceptions.BusinessException;

import java.util.List;
import java.util.function.Consumer;

/**
 * Batch processing of bulk operations
 */

public class PageUtils {


    /**
     * @param pageSize Number of pages per time
     * @param list     List that needs paging
     * @param handler  Execution Method
     * @param <T>
     */
    public static <T> void handle(int pageSize, List<T> list, Consumer<List<T>> handler) {
        if (pageSize <= 0) {
            throw new BusinessException(GlobalApiCodes.PARAMETER_INVALID.formatDefaultMessage("pageSize"));
        }
        if (list == null || list.isEmpty()) {
            return;
        }
        if (list.size() <= pageSize) {
            handler.accept(list);
        } else {
            for (int i = 0; i < list.size(); i += pageSize) {
                handler.accept(list.subList(i, Math.min(i + pageSize, list.size())));
            }
        }
    }
}

