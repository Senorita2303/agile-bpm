package com.dstz.auth.core.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.dstz.auth.authentication.api.constant.AuthStatusCode;
import com.dstz.auth.core.entity.ResourceRole;
import com.dstz.auth.core.entity.SysApplication;
import com.dstz.auth.core.entity.SysResource;
import com.dstz.auth.core.manager.ResourceRoleManager;
import com.dstz.auth.core.manager.SysApplicationManager;
import com.dstz.auth.core.manager.SysResourceManager;
import com.dstz.auth.core.mapper.ResourceRoleMapper;
import com.dstz.auth.rest.model.dto.GrantRoleResourceDTO;
import com.dstz.auth.rest.model.vo.SysResourceTreeVO;
import com.dstz.base.common.cache.ICache;
import com.dstz.base.common.constats.AbCacheRegionConstant;
import com.dstz.base.common.constats.NumberPool;
import com.dstz.base.common.constats.StrPool;
import com.dstz.base.common.exceptions.BusinessMessage;
import com.dstz.base.common.utils.BeanConversionUtils;
import com.dstz.base.common.utils.BeanCopierUtils;
import com.dstz.base.manager.impl.AbBaseManagerImpl;
import com.dstz.org.api.GroupApi;
import com.dstz.org.api.enums.GroupType;
import com.dstz.org.api.model.IGroup;
import com.google.common.collect.Streams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Resource role association table General service implementation class
 *
 */
@Service("resourceRoleManager")
public class ResourceRoleManagerImpl extends AbBaseManagerImpl<ResourceRole> implements ResourceRoleManager {

    @Autowired
    ResourceRoleMapper resourceRoleMapper;

    @Autowired
    ICache iCache;

    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    SysApplicationManager sysApplicationManager;

    @Autowired
    SysResourceManager sysResourceManager;

    @Autowired
    private GroupApi groupApi;

    @Override
    public List<ResourceRole> getAllByRoleId(String roleId) {
        return resourceRoleMapper.getByRoleId(roleId);
    }


    @CacheEvict(cacheNames = AbCacheRegionConstant.SYS_RESOURCE, allEntries = true)
    @Override
    public void grantRoleResource(GrantRoleResourceDTO dto) {
        resourceRoleMapper.removeByRoleAndSystem(dto.getRoleId(), dto.getAppId());
        
        Stream<String> resIdStream = Stream.empty();
        ResourceRole.Builder builder = ResourceRole.Builder.newBuilder().withRoleId(dto.getRoleId()).withAppId(dto.getAppId()).withHalfChecked(NumberPool.BOOLEAN_FALSE);
        if (CollUtil.isNotEmpty(dto.getResIds())) {
            resIdStream = Stream.concat(resIdStream, dto.getResIds().stream());
        }
        // Set Half Selected
        resIdStream = Stream.concat(resIdStream, Stream.of(StrUtil.EMPTY).peek(o -> builder.withHalfChecked(NumberPool.BOOLEAN_TRUE)));
        if (CollUtil.isNotEmpty(dto.getHalfResIds())) {
            resIdStream = Stream.concat(resIdStream, dto.getHalfResIds().stream());
        }

        Iterator<ResourceRole> iterator = resIdStream
                .filter(resId -> !StrPool.NUMBER_ZERO.equals(resId) && StrUtil.isNotEmpty(resId))
                .map(resId -> builder.withResourceId(resId).build())
                .iterator();

        // Batch storage
        bulkCreate(IterUtil.asIterable(iterator));
    }

    @Cacheable(cacheNames = AbCacheRegionConstant.SYS_RESOURCE, key = "#url")
    @Override
    public Set<String> getAccessRoleByUrl(String url) {
        url = StrUtil.trimToNull(url);
        if (StrUtil.isEmpty(url)) {
            return Collections.emptySet();
        }

        // Get the role ID based on the resource address
        Set<String> roleIds = resourceRoleMapper.getRoleIdByResourceUrl(url);
        if (CollUtil.isEmpty(roleIds)) {
            return Collections.emptySet();
        }

        // Get the role code
        return Optional.ofNullable(groupApi.getByGroupIds(GroupType.ROLE.getType(), roleIds))
                .map(Streams::stream)
                .orElseGet(Stream::empty)
                .filter(o -> o.getAttrValue("enabled", Boolean.class))
                .map(IGroup::getGroupCode)
                .collect(Collectors.toSet());
    }

    @Override
    public List<SysResourceTreeVO> getRoleResTreeData(String roleId, String code) {
        SysApplication system = sysApplicationManager.getByAlias(code);
        Assert.notNull(system,()->new BusinessMessage(AuthStatusCode.SYS_IS_NOT_DEFINITION.formatDefaultMessage(code)));
        return BeanConversionUtils.listToTree(getRoleRes(system.getId(), roleId));
    }

    @Override
    public List<SysResourceTreeVO> getRoleRes(String systemId, String roleId) {

        List<SysResource> roleResourceList = sysResourceManager.getBySystemAndRole(systemId, roleId);
        Set<String> userResourceId = new HashSet<>(roleResourceList.size(), 1);
        roleResourceList.forEach(resouces -> userResourceId.add(resouces.getId()));
        List<SysResource> resourceList = sysResourceManager.getBySystemId(systemId);
        List<SysResourceTreeVO> sysResourceTreeVOS = BeanCopierUtils.transformList(resourceList,SysResourceTreeVO.class);
        for (SysResourceTreeVO sysResource : sysResourceTreeVOS) {
            if (userResourceId.contains(sysResource.getId())) {
                sysResource.setChecked(true);
            }
        }

        if (CollectionUtil.isEmpty(resourceList)) {
            resourceList = new ArrayList<>();
        }

        SysResource rootRes = new SysResource();
        String rootName = sysApplicationManager.getById(systemId).getName();
        rootRes.setName(rootName);
        rootRes.setId(NumberPool.INTEGER_ZERO.toString());
        rootRes.setCode("root");
        // Root Node
        rootRes.setAppId(systemId);
        resourceList.add(rootRes);
        return sysResourceTreeVOS;
    }
}
