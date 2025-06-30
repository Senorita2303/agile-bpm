package com.dstz.sys.core.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dstz.base.common.cache.ICache;
import com.dstz.base.common.constats.AbCacheRegionConstant;
import com.dstz.base.common.constats.NumberPool;
import com.dstz.base.common.encrypt.EncryptUtil;
import com.dstz.base.common.enums.EnvironmentConstants;
import com.dstz.base.common.property.SysPropertyService;
import com.dstz.base.manager.impl.AbBaseManagerImpl;
import com.dstz.sys.core.entity.SysProperties;
import com.dstz.sys.core.manager.SysPropertiesManager;
import com.dstz.sys.core.mapper.SysPropertiesMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * System properties General service implementation class
 *
 */
@Service("sysPropertiesManager")
public class SysPropertiesManagerImpl extends AbBaseManagerImpl<SysProperties> implements SysPropertiesManager, SysPropertyService {

    private final SysPropertiesMapper sysPropertiesMapper;

    private final ICache cache;

    public SysPropertiesManagerImpl(SysPropertiesMapper sysPropertiesMapper, ICache cache) {
        this.sysPropertiesMapper = sysPropertiesMapper;
        this.cache = cache;
    }

    @Override
    public boolean isExist(SysProperties sysProperties) {
        return sysPropertiesMapper.isExist(sysProperties) > 0;
    }

    @Override
    public String getValByCode(String code) {
        final String currentEnv = StringUtils.defaultString(SpringUtil.getActiveProfile(), EnvironmentConstants.DEV.getKey());
        final String cacheKey = currentEnv + StrUtil.COLON + code;
        String value = cache.get(AbCacheRegionConstant.PROPERTIES_CACHE_REGION, cacheKey, () -> loadPropertyValue(currentEnv, code));
        return StrUtil.emptyToNull(value);
    }

    private String loadPropertyValue(String environment, String code) {
        LambdaQueryWrapper<SysProperties> queryWrapper = Wrappers.lambdaQuery(SysProperties.class)
                .select(SysProperties::getEncrypt, SysProperties::getValue, SysProperties::getEnvironment)
                .in(SysProperties::getEnvironment, StringUtils.upperCase(environment), EnvironmentConstants.DEV.getKey())
                .eq(SysProperties::getCode, code);

        List<SysProperties> sysPropertiesList = sysPropertiesMapper.selectList(queryWrapper);

        // In multiple environments, extract the parameters of the specified environment and the dev environment. If there is only one configuration item, return it. If there are multiple, filter out the corresponding environment's return
        SysProperties sysProperties = Optional.ofNullable(CollUtil.findOne(sysPropertiesList, o -> StrUtil.equalsIgnoreCase(o.getEnvironment(), environment)))
                .orElseGet(() -> CollUtil.findOne(sysPropertiesList, o -> StrUtil.equalsIgnoreCase(o.getEnvironment(), EnvironmentConstants.DEV.getKey())));

        if (sysProperties == null || (sysProperties != null && sysProperties.getValue() == null)) {
            return StrUtil.EMPTY;
        }
        return NumberPool.BOOLEAN_TRUE.equals(sysProperties.getEncrypt()) ? EncryptUtil.decrypt(sysProperties.getValue()) : sysProperties.getValue();
    }


    /**
     * Add all system properties to the cache by grouping them into different environments
     */
    @Override
    public void reloadProperty() {
        cache.invalidateRegion(AbCacheRegionConstant.PROPERTIES_CACHE_REGION);
    }

    @Override
    public Integer getIntByCode(String code) {
        return Convert.toInt(getByCode(code), 0);
    }


    @Override
    public Long getLongByCode(String code) {
        return Convert.toLong(getByCode(code), 0L);
    }

    @Override
    public Boolean getBooleanByCode(String code) {
        return Convert.toBool(getByCode(code), false);
    }
}
