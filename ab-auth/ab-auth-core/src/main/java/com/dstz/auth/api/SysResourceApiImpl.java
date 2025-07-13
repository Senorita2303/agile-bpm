package com.dstz.auth.api;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dstz.auth.authentication.api.SysResourceApi;
import com.dstz.auth.authentication.api.model.ISysApplication;
import com.dstz.auth.authentication.api.model.ISysResource;
import com.dstz.auth.core.entity.SysResource;
import com.dstz.auth.core.manager.ResourceRoleManager;
import com.dstz.auth.core.manager.SysApplicationManager;
import com.dstz.auth.core.manager.SysResourceManager;
import com.dstz.auth.rest.model.vo.SysResourceVO;
import com.dstz.base.common.constats.AbCacheRegionConstant;
import com.dstz.base.common.utils.BeanCopierUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static com.dstz.auth.authentication.api.constant.AuthCacheKeyConstant.IS_ROLE;

/**
 * User system resource service interface
 *
 */
@Service
public class SysResourceApiImpl implements SysResourceApi {
    @Autowired
    SysResourceManager sysResourceManager;
    @Autowired
    SysApplicationManager sysApplicationManager;
    @Autowired
    ResourceRoleManager resourceRoleManager;

    // Fixed URL address for handling matters
    private static final String TODO_RESOURCE = "/pages/bpm/definitionList";

    /**
     * Get resource object by resource id
     *
     * @param id Resource ID
     * @return ISysResource Resource Object
     */
    @Override
    public ISysResource getResourceById(String id) {
        return BeanCopierUtils.transformBean(sysResourceManager.getById(id), SysResourceVO.class);
    }

    /**
     * Delete resource objects by resource code collection
     *
     * @param codeList Resource code collection
     */
    @Override
    public void deleteResourceByCode(List<String> codeList) {
        sysResourceManager.deleteResourceByCode(codeList);
    }

    @Override
    public List<ISysApplication> getCurrentUserSystem() {
        return (List) sysApplicationManager.getCurrentUserSystem();
    }

    @Override
    public ISysApplication getDefaultSystem(String currentUserId) {
        return sysApplicationManager.getDefaultSystem(currentUserId);
    }

    @Override
    public List<ISysResource> getBySystemId(String systemId) {
        List<SysResource> bySystemId = sysResourceManager.getBySystemId(systemId);
        return (List) sysResourceManager.getBySystemId(systemId);
    }

    @Override
    public List<ISysResource> getByAppIdAndUser(String appId, String userId) {
        List<SysResource> byAppIdAndUserId = sysResourceManager.getByAppIdAndUserId(appId, userId);
        return (List) sysResourceManager.getByAppIdAndUserId(appId, userId);
    }

    @Override
    public Set<String> getAccessRoleByUrl(String url) {
        return resourceRoleManager.getAccessRoleByUrl(url);
    }

    @Override
    @Cacheable(cacheNames = AbCacheRegionConstant.SYS_RESOURCE, key = IS_ROLE)
    public boolean isRoleByUrl(String url) {
        return CollUtil.isNotEmpty(sysResourceManager.selectByWrapper(Wrappers.lambdaQuery(SysResource.class).select(SysResource::getType).eq(SysResource::getType, "API").eq(SysResource::getUrl, url)));
    }

    @Override
    public ISysResource getTodoResource() {
        List<SysResource> resources = sysResourceManager.selectByWrapper(Wrappers.lambdaQuery(SysResource.class)
                .eq(SysResource::getUrl, TODO_RESOURCE));
        if (CollectionUtil.isEmpty(resources)) {
            return null;
        }
        return BeanCopierUtils.transformBean(resources.get(0), SysResourceVO.class);
    }

	@Override
	public void deleteByCode(List<String> codeList) {
		this.deleteResourceByCode(codeList);
	}

}
