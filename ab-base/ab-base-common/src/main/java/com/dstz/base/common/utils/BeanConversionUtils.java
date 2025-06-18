package com.dstz.base.common.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.dstz.base.api.model.Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * bean 转换工具类
 *
 * @author jeff
 * @since 2022-01-24
 */
public class BeanConversionUtils {

    /**
     * @描述 list数据转Tree，大多使用在前台json中。
     * @说明 实现接口 Tree即可
     * @扩展 可通过反射获取id, pid，目前只提供Tree接口排序的实现
     * @author jeff
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
