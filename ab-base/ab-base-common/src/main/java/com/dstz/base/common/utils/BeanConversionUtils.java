package com.dstz.base.common.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.dstz.base.api.model.Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * bean conversion tool class
 *
 */
public class BeanConversionUtils {

    /**
     * @Description list data converted to Tree, mostly used in front-end json
     * @Description Just implement the interface Tree
     * @Extension You can obtain id and pid through reflection. Currently, only the implementation of Tree interface sorting is provided
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T extends Tree> List<T> listToTree(List<T> list) {
        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        Map<String, T> idToTreeMap = CollUtil.toMap(list, MapUtil.newHashMap(list.size()), Tree::getId);
        List<T> rootList = new ArrayList<>();
        for (T item : list) {
            T parent = idToTreeMap.get(item.getParentId());
            if (Objects.nonNull(parent)) {
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList());
                }
                parent.getChildren().add(item);
            } else {
                rootList.add(item);
            }
        }
        return rootList;
    }
}
