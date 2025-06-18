package com.dstz.base.common.valuemap.loader;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.dstz.base.common.utils.CastUtils;
import com.dstz.base.common.valuemap.AbValueMap;
import com.dstz.base.common.valuemap.AbValueMapLoader;
import com.dstz.base.common.valuemap.AbValueMapLoaderProvider;
import com.dstz.base.common.valuemap.AbValueMapType;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 * Spring bean value mapping
 *
 */
@Component
public class AbBeanValueMapLoader implements AbValueMapLoader<Serializable, Object> {

	public AbBeanValueMapLoader() {
		AbValueMapLoaderProvider.register(AbValueMapType.BEAN, this);
	}

	@Override
	public Map<Serializable, Object> loading(AbValueMap abValueMap, Collection<Serializable> mapKeys) {
		final Class<?> fixClass = abValueMap.fixClass()[0];
		Assert.notNull(fixClass, "@AbValueMap fixClass not specified");
		final PropertyDescriptor matchFieldPropertyDescriptor = BeanUtil.getPropertyDescriptor(fixClass, abValueMap.matchField());
		Assert.notNull(matchFieldPropertyDescriptor, "{}Property does not exist{}", fixClass.getName(), abValueMap.matchField());
		final Method matchFieldReadMethod = matchFieldPropertyDescriptor.getReadMethod();
		Assert.notNull(matchFieldPropertyDescriptor, "{}Property {} does not have a getter method", fixClass.getName(), abValueMap.matchField());

		Collection<Object> beans = CastUtils.cast(SpringUtil.getBeansOfType(fixClass).values());
		Map<Serializable, Object> mapValues = MapUtil.newHashMap(CollUtil.size(beans));
		CollUtil.forEach(beans, (bean, i) -> mapValues.put(ReflectUtil.invoke(bean, matchFieldReadMethod), bean));

		return mapValues;
	}
}
