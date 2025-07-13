package com.dstz.base.common.valuemap.loader;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.dstz.base.common.utils.CastUtils;
import com.dstz.base.common.valuemap.AbValueMap;
import com.dstz.base.common.valuemap.AbValueMapLoader;
import com.dstz.base.common.valuemap.AbValueMapLoaderProvider;
import com.dstz.base.common.valuemap.AbValueMapType;
import com.dstz.org.api.GroupApi;
import com.dstz.org.api.enums.GroupType;
import com.dstz.org.api.model.IGroup;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Organization Map Value Loader
 *
 */
@Component
public class AbOrgValueMapLoader implements AbValueMapLoader<String, IGroup> {

	private static final String GROUP_ID_FIELD_NAME = LambdaUtil.getFieldName(IGroup::getGroupId);

	private static final String GROUP_CODE_FIELD_NAME = LambdaUtil.getFieldName(IGroup::getGroupCode);

	private final GroupApi groupApi;

	public AbOrgValueMapLoader(GroupApi groupApi) {
		this.groupApi = groupApi;
		AbValueMapLoaderProvider.register(AbValueMapType.ORG, this);
	}

	@Override
	public Map<String, IGroup> loading(AbValueMap abValueMap, Collection<String> mapKeys) {
		String fieldName = StrUtil.emptyToDefault(abValueMap.matchField(), GROUP_ID_FIELD_NAME);
		Iterator<? extends IGroup> iterator;
		if (StrUtil.equals(fieldName, GROUP_ID_FIELD_NAME)) {
			iterator = groupApi.getByGroupIds(GroupType.ORG.getType(), mapKeys);
		} else if (StrUtil.equals(fieldName, GROUP_CODE_FIELD_NAME)) {
			iterator = groupApi.getByGroupCodes(GroupType.ORG.getType(), mapKeys);
		} else {
			throw new IllegalStateException(StrUtil.format("{} Field {} not found", IGroup.class.getName(), fieldName));
		}
		Method readMethod = BeanUtil.getPropertyDescriptor(IGroup.class, fieldName).getReadMethod();
		return Optional.ofNullable(iterator).filter(IterUtil::isNotEmpty).map(IterUtil::asIterable)
				.map(iter -> CollUtil.toMap(iter, MapUtil.newHashMap(), k -> Convert.toStr(readMethod.invoke(k))))
				.<Map<String, IGroup>>map(CastUtils::cast).orElseGet(Collections::emptyMap);
	}
}
