package com.dstz.sys.valueMapLoader;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dstz.base.common.valuemap.AbValueMap;
import com.dstz.base.common.valuemap.AbValueMapLoader;
import com.dstz.base.common.valuemap.AbValueMapLoaderProvider;
import com.dstz.base.common.valuemap.AbValueMapType;
import com.dstz.sys.core.entity.SysDataDict;
import com.dstz.sys.core.manager.SysDataDictManager;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Name AbTypeValueMapLoader
 * @description: Classification loader
 */
@Component("abTypeValueMapLoader")
public class AbTypeValueMapLoader implements AbValueMapLoader<String, Object> {

	private final SysDataDictManager sysDataDictManager;

	public AbTypeValueMapLoader(SysDataDictManager sysDataDictManager) {
		this.sysDataDictManager = sysDataDictManager;
		AbValueMapLoaderProvider.register(AbValueMapType.TYPE, this);
	}

	@Override
	public Map<String, Object> loading(AbValueMap abValueMap, Collection<String> mapKeys) {
		final String typeCode = abValueMap.fixValue();
		if (CollUtil.isEmpty(mapKeys)) {
			return Collections.emptyMap();
		}
		List<SysDataDict> sysDataDictList = sysDataDictManager.selectByWrapper(Wrappers.lambdaQuery(SysDataDict.class)
		                                                                               .eq(SysDataDict::getDictKey, typeCode)
		                                                                               .in(SysDataDict::getCode, mapKeys)
		);
		return sysDataDictList.stream().collect(Collectors.toMap(SysDataDict::getCode, Function.identity()));
	}
}
