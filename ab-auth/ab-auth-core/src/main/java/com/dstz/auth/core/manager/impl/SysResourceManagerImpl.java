package com.dstz.auth.core.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dstz.auth.authentication.api.constant.AuthStatusCode;
import com.dstz.auth.authentication.api.constant.ResouceTypeConstant;
import com.dstz.auth.core.entity.SysResource;
import com.dstz.auth.core.manager.SysResourceManager;
import com.dstz.auth.core.mapper.SysResourceMapper;
import com.dstz.auth.rest.model.dto.SysResourceDTO;
import com.dstz.auth.rest.model.vo.SysResourceVO;
import com.dstz.base.common.constats.NumberPool;
import com.dstz.base.common.exceptions.BusinessException;
import com.dstz.base.common.exceptions.BusinessMessage;
import com.dstz.base.common.utils.BeanCopierUtils;
import com.dstz.base.manager.impl.AbBaseManagerImpl;
import com.dstz.org.api.GroupApi;
import com.dstz.org.api.enums.GroupType;
import com.dstz.org.api.model.IGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * System permission resource definition General service implementation class
 *
 */
@Service("sysResourceManager")
public class SysResourceManagerImpl extends AbBaseManagerImpl<SysResource> implements SysResourceManager {

    @Autowired
    SysResourceMapper sysResourceMapper;
    @Autowired
    private GroupApi groupApi;


    @Override
    public List<SysResource> getBySystemId(String id) {
        return sysResourceMapper.getBySystemId(id);
    }

    @Override
    public List<SysResource> getBySystemAndRole(String systemId, String roleId) {
        return sysResourceMapper.getBySystemAndRole(systemId, roleId);
    }

    @Override
    public boolean isExist(SysResource resource) {
        return sysResourceMapper.isExist(resource) > 0;
    }

    @Override
    public void removeByResId(String resId) {
        SysResource resource = sysResourceMapper.selectById(resId);
        if (resource == null) {
            return;
        }
        List<SysResource> relatedResouces = new ArrayList<>();

        getChildList(relatedResouces, resource.getId());
        if (CollectionUtil.isNotEmpty(relatedResouces)) {
            sysResourceMapper.deleteBatchIds(relatedResouces.stream().map(SysResource::getId).collect(Collectors.toList()));
        }
        sysResourceMapper.deleteById(resId);
    }


    private void getChildList(List<SysResource> relatedResouces, String id) {
        List<SysResource> children = sysResourceMapper.getByParentId(id);
        if (CollectionUtil.isEmpty(children)) {
            return;
        }

        for (SysResource r : children) {
            getChildList(relatedResouces, r.getId());
        }
        relatedResouces.addAll(children);
    }

    @Override
    public List<SysResource> getByAppIdAndUserId(String appId, String userId) {
        List<String> roleIds = CollUtil.map(groupApi.getByGroupTypeAndUserId(GroupType.ROLE.getType(), userId), IGroup::getGroupId, true);
        if (CollUtil.isEmpty(roleIds)) {
            return new ArrayList<>();
        }
        return sysResourceMapper.getByAppIdAndRoleIds(appId, roleIds);
    }

    @Override
    public SysResourceVO getResourceDetailById(String id, String parentId, String systemId) {
        SysResourceVO sysResourceVO = new SysResourceVO();
        if (StrUtil.isEmpty(id)) {
            SysResource parent = this.getById(parentId);
            if (parent != null) {
                sysResourceVO.setAppId(parent.getAppId());
                sysResourceVO.setParentName(parent.getName());
                sysResourceVO.setParentId(parentId);
            } else {
                sysResourceVO.setAppId(systemId);
                sysResourceVO.setParentId(parentId);
            }

            sysResourceVO.setOpened(NumberPool.BOOLEAN_TRUE);
            return sysResourceVO;
        } else {
            SysResource sysResource = this.getById(id);

            BeanCopierUtils.copyProperties(sysResource, sysResourceVO);
            SysResource parent = this.getById(sysResource.getParentId());
            if (parent != null) {
                sysResourceVO.setParentName(parent.getName());
            }
        }
        return sysResourceVO;
    }

    @Override
    public void saveTree(SysResourceDTO sysResource) {
        if (CollUtil.isNotEmpty(sysResource.getChildren())) {
            saveTreeList(sysResource.getChildren(), sysResource.getId());
        }
    }

    @Override
    public String saveSysResource(SysResource sysResource) {
        checkResouce(sysResource);

        if (StrUtil.isEmpty(sysResource.getId())) {
            List<SysResource> list = sysResourceMapper.getByParentId(sysResource.getParentId());
            sysResource.setSn(list.size() + 1);
            Assert.isFalse(this.isExist(sysResource), () -> new BusinessMessage(AuthStatusCode.RESOURCES_CODE_REPEAT.formatDefaultMessage(sysResource.getName()+" "+sysResource.getCode())));
            this.create(sysResource);
        } else {
            this.update(sysResource);
        }
        return sysResource.getId();
    }

    @Override
    public void deleteResourceByCode(List<String> codeList) {
        remove(Wrappers.lambdaQuery(SysResource.class)
                .in(CollectionUtil.isNotEmpty(codeList), SysResource::getCode, codeList));
    }

    private void checkResouce(SysResource sysResource) {
        if (StrUtil.isEmpty(sysResource.getId())) {
            Assert.isFalse(this.isExist(sysResource), () -> new BusinessMessage(AuthStatusCode.RESOURCES_CODE_REPEAT));
        }
        // If it is a menu, then the parent must also be a menu to prevent the menu from being configured under the button
        if (ResouceTypeConstant.MENU.getKey().equals(sysResource.getType())) {
            SysResource parent = this.getById(sysResource.getParentId());
            if (parent == null) return;

            Assert.isTrue(ResouceTypeConstant.MENU.getKey().equals(parent.getType()), () -> new BusinessException(AuthStatusCode.RESOUCE_TYPE_CONSTANT_ERROR.formatDefaultMessage(parent.getName())));
        }

        sysResource.setUrl(StrUtil.isNotEmpty(sysResource.getUrl()) ? sysResource.getUrl().trim() : "");
    }

    private void saveTreeList(List<SysResourceDTO> sysResourceList, String parentId) {

        for (int i = 0; i < sysResourceList.size(); i++) {
            SysResourceDTO resource = sysResourceList.get(i);
            resource.setParentId(parentId);
            resource.setSn(i + 1);
            Assert.isFalse(this.isExist(resource), () -> new BusinessMessage(AuthStatusCode.RESOURCES_CODE_REPEAT.formatDefaultMessage(resource.getName()+" "+resource.getCode())));
            this.createOrUpdate(resource);
            if (CollUtil.isNotEmpty(resource.getChildren())) {
                saveTreeList(resource.getChildren(), resource.getId());
            }
        }
    }

}
