package com.dstz.sys.core.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dstz.base.common.cache.ICache;
import com.dstz.base.common.constats.AbCacheRegionConstant;
import com.dstz.base.common.constats.NumberPool;
import com.dstz.base.common.constats.StrPool;
import com.dstz.base.common.exceptions.BusinessMessage;
import com.dstz.base.common.property.SysConfigurationApi;
import com.dstz.base.common.utils.BeanCopierUtils;
import com.dstz.base.common.utils.JsonUtils;
import com.dstz.base.manager.impl.AbBaseManagerImpl;
import com.dstz.sys.api.constant.SysApiCodes;
import com.dstz.sys.api.constant.SysCackeKeyConstant;
import com.dstz.sys.api.dto.QuickLoginTypeDTO;
import com.dstz.sys.core.entity.SysConfiguration;
import com.dstz.sys.core.manager.SysConfigurationManager;
import com.dstz.sys.rest.model.vo.SysConfigurationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.dstz.sys.api.constant.SysApiCodes.SYSCONFIG_CODE_ISUPDATA;

/**
 * General service implementation class
 *
 */
@Service("sysConfigurationManager")
public class SysConfigurationManagerImpl extends AbBaseManagerImpl<SysConfiguration> implements SysConfigurationManager {

    @Autowired
    private ICache iCache;

    @Override
    @Cacheable(cacheNames = AbCacheRegionConstant.PROPERTIES_CACHE_REGION, key = SysCackeKeyConstant.PROPERTIES_CACHE_REGION_RECEIVE_EL)
    public SysConfiguration getConfByCode(String code) {
        List<SysConfiguration> sysConfigurationList = this.selectByWrapper(Wrappers.lambdaQuery(SysConfiguration.class).eq(SysConfiguration::getCode, code));
        return CollUtil.isEmpty(sysConfigurationList) ? null : CollUtil.getFirst(sysConfigurationList);
    }

    @Override
    public void save(SysConfiguration sysConfiguration) {
        if (StringUtils.hasLength(sysConfiguration.getId())) {
            SysConfiguration sysConfiguration_old = this.getById(sysConfiguration.getId());
            if(StrUtil.isEmpty(sysConfiguration.getCode()) || (ObjectUtil.isNotNull(sysConfiguration_old) && sysConfiguration_old.getCode().equals(sysConfiguration.getCode()))){
                this.update(sysConfiguration);
                iCache.invalidate(AbCacheRegionConstant.PROPERTIES_CACHE_REGION, SysCackeKeyConstant.PROPERTIES_CACHE_REGION_RECEIVE_KEY + sysConfiguration.getCode());
                return;
            }
            throw new BusinessMessage(SYSCONFIG_CODE_ISUPDATA.formatDefaultMessage(sysConfiguration_old.getCode()));
        } else {
            Assert.isFalse(this.getBaseMapper().exists(Wrappers.lambdaQuery(SysConfiguration.class).eq(SysConfiguration::getCode, sysConfiguration.getCode())), () -> new BusinessMessage(SysApiCodes.CODE_DUPLICATE));
            this.create(sysConfiguration);
            iCache.put(AbCacheRegionConstant.PROPERTIES_CACHE_REGION, SysCackeKeyConstant.PROPERTIES_CACHE_REGION_RECEIVE_KEY + sysConfiguration.getCode(), sysConfiguration);
        }
        if (StrPool.DD.equals(sysConfiguration.getCode()) || StrPool.WX.equals(sysConfiguration.getCode())){
            changeQuickLoginType(sysConfiguration.getCode());
        }
    }

    @Override
    public void deleteByCode(String code) {
        remove(Wrappers.lambdaQuery(SysConfiguration.class).eq(SysConfiguration::getCode, code));
        iCache.invalidate(AbCacheRegionConstant.PROPERTIES_CACHE_REGION, SysCackeKeyConstant.PROPERTIES_CACHE_REGION_RECEIVE_KEY + code);

    }

    @Override
    public List<SysConfigurationVO> getSysProperties(String codes) {
        List<String> codeList = StrUtil.split(codes, StrPool.C_COMMA);
        List<SysConfigurationVO> voList = new ArrayList<>();
        if (codeList.size()>NumberPool.INTEGER_ZERO){
            SysConfigurationVO sysConfigurationVO;
            for (String code: codeList) {
                SysConfiguration sysConfiguration = getConfByCode(code);
                if (ObjectUtil.isNotNull(sysConfiguration)){
                    sysConfigurationVO = BeanCopierUtils.transformBean(sysConfiguration,SysConfigurationVO.class);
                    sysConfigurationVO.setEnable(NumberPool.INTEGER_ONE.equals(sysConfiguration.getIsEnable()));
                    voList.add(sysConfigurationVO);
                }
            }
        }
        return voList;
    }

    @Override
    public int changeEnableByCode(String code) {
        SysConfiguration configuration = getConfByCode(code);
        configuration.setIsEnable(NumberPool.INTEGER_ONE.equals(configuration.getIsEnable())? NumberPool.INTEGER_ZERO :NumberPool.INTEGER_ONE);
        if (StrPool.DD.equals(code) || StrPool.WX.equals(code)){
            if (NumberPool.INTEGER_ONE.equals(configuration.getIsEnable())){
                changeQuickLoginType(code);
            } else {
                SysConfiguration quickLoginType = getConfByCode(StrPool.LOGINTYPE);
                if (ObjectUtil.isNotNull(quickLoginType)) {
                    QuickLoginTypeDTO quickLoginTypeDTO = SpringUtil.getBean(SysConfigurationApi.class).getConfigByCode(StrPool.LOGINTYPE, QuickLoginTypeDTO.class);
                    quickLoginTypeDTO.setAppLoginType(StrPool.EMPTY);
                    quickLoginTypeDTO.setPcLoginType(StrPool.EMPTY);
                    quickLoginType.setJson(JsonUtils.toJSONString(quickLoginTypeDTO));
                    iCache.invalidate(AbCacheRegionConstant.PROPERTIES_CACHE_REGION, SysCackeKeyConstant.PROPERTIES_CACHE_REGION_RECEIVE_KEY +StrPool.LOGINTYPE);
                    this.update(quickLoginType);
                }
            }
        }
        iCache.invalidate(AbCacheRegionConstant.PROPERTIES_CACHE_REGION, SysCackeKeyConstant.PROPERTIES_CACHE_REGION_RECEIVE_KEY + code);
        return this.update(configuration);
    }

    @Override
    public int changeQuickLoginType(String code) {
        QuickLoginTypeDTO quickLoginTypeDTO = SpringUtil.getBean(SysConfigurationApi.class).getConfigByCode(StrPool.LOGINTYPE, QuickLoginTypeDTO.class);

        String updateCode =  StrPool.DD.equals(code) ? StrPool.WX : StrPool.DD;
        SysConfiguration configuration = getConfByCode(updateCode);
        if (ObjectUtil.isNotNull(configuration)){
            configuration.setIsEnable(NumberPool.INTEGER_ZERO);
            iCache.invalidate(AbCacheRegionConstant.PROPERTIES_CACHE_REGION, SysCackeKeyConstant.PROPERTIES_CACHE_REGION_RECEIVE_KEY + configuration.getCode());
            this.update(configuration);//Disable
        }

        String loginType = StrPool.DD.equals(code) ? StrPool.DD : StrPool.QYWX;
        quickLoginTypeDTO.setPcLoginType(loginType);
        quickLoginTypeDTO.setAppLoginType(loginType);
        SysConfiguration quickLoginType = getConfByCode(StrPool.LOGINTYPE);
        quickLoginType.setJson(JsonUtils.toJSONString(quickLoginTypeDTO));
        iCache.invalidate(AbCacheRegionConstant.PROPERTIES_CACHE_REGION, SysCackeKeyConstant.PROPERTIES_CACHE_REGION_RECEIVE_KEY +StrPool.LOGINTYPE);
        return this.update(quickLoginType);//Modify login type
    }
}
