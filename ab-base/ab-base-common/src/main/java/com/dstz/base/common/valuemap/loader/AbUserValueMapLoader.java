package com.dstz.base.common.valuemap.loader;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.dstz.base.common.utils.CastUtils;
import com.dstz.base.common.valuemap.AbValueMap;
import com.dstz.base.common.valuemap.AbValueMapLoader;
import com.dstz.base.common.valuemap.AbValueMapLoaderProvider;
import com.dstz.base.common.valuemap.AbValueMapType;
import com.dstz.org.api.UserApi;
import com.dstz.org.api.model.IUser;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;

/**
 * User Association Loader
 *
 */
@Component("abUserValueMapLoader")
public class AbUserValueMapLoader implements AbValueMapLoader<String,IUser> {

	private static final String USER_ID_FIELD_NAME = LambdaUtil.getFieldName(IUser::getUserId);

	private static final String USER_NAME_FIELD_NAME = LambdaUtil.getFieldName(IUser::getUsername);

	private final UserApi userApi;

	public AbUserValueMapLoader(UserApi userApi) {
		this.userApi = userApi;
		AbValueMapLoaderProvider.register(AbValueMapType.USER, this);
	}

	@Override
	public Map<String, IUser> loading(AbValueMap abValueMap, Collection<String> mapKeys) {
		final String fieldName = StrUtil.emptyToDefault(abValueMap.matchField(), USER_ID_FIELD_NAME);
		Iterator<? extends IUser> iterator;
		if (USER_ID_FIELD_NAME.equalsIgnoreCase(fieldName)) {
			iterator = userApi.getByUserIds(mapKeys);
		} else if (USER_NAME_FIELD_NAME.equals(fieldName)) {
			iterator = userApi.getByUsernames(mapKeys);
		} else {
			throw new IllegalStateException(StrUtil.format("{} Field {} not found", IUser.class.getName(), fieldName));
		}
		Method readMethod = BeanUtil.getPropertyDescriptor(IUser.class, fieldName).getReadMethod();
		return Optional.ofNullable(iterator)
				.filter(IterUtil::isNotEmpty)
				.map(IterUtil::asIterable)
				.map(iterable -> CollUtil.toMap(iterable, MapUtil.newHashMap(), user -> Convert.toStr(ReflectUtil.invoke(user, readMethod))))
				.<Map<String, IUser>>map(CastUtils::cast)
				.orElseGet(Collections::emptyMap);
	}
}
