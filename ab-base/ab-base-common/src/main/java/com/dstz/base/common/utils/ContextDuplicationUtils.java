package com.dstz.base.common.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.dstz.base.common.context.ContextDuplication;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 上下文复制工具类
 *
 * @author wacxhs
 */
public class ContextDuplicationUtils {

	private interface BeanHolder {
		List<ContextDuplication> CONTEXT_DUPLICATIONS = Arrays.stream(SpringUtil.getBeanNamesForType(ContextDuplication.class))
		                                                      .map(beanName -> SpringUtil.getBean(beanName, ContextDuplication.class))
		                                                      .sorted(AnnotationAwareOrderComparator.INSTANCE)
		                                                      .collect(Collectors.toList());
	}

	/**
	 * 从上下文中复制出对象
	 *
	 * @return 复制对象
	 */
	public static Object[] duplicateObjects() {
		Object[] duplicateObjects = new Object[BeanHolder.CONTEXT_DUPLICATIONS.size()];
		CollUtil.forEach(BeanHolder.CONTEXT_DUPLICATIONS, (contextDuplication, index) -> duplicateObjects[index] = contextDuplication.duplicate());
		return duplicateObjects;
	}

	/**
	 * 填充复制对象
	 *
	 * @param duplicateObjects 复制对象集
	 */
	public static void fillDuplicateObjects(Object[] duplicateObjects) {
		if (ArrayUtil.isNotEmpty(duplicateObjects)) {
			CollUtil.forEach(BeanHolder.CONTEXT_DUPLICATIONS, (contextDuplication, index) -> contextDuplication.fill(duplicateObjects[index]));
		}
	}
}
