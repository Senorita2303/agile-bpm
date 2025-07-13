package com.dstz.org.core.manager.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dstz.base.common.constats.NumberPool;
import com.dstz.base.common.exceptions.BusinessMessage;
import com.dstz.base.common.utils.BeanCopierUtils;
import com.dstz.base.manager.impl.AbBaseManagerImpl;
import com.dstz.org.core.constant.OrgStatusCode;
import com.dstz.org.core.entity.Role;
import com.dstz.org.core.manager.OrgRelationManager;
import com.dstz.org.core.manager.RoleManager;
import com.dstz.org.core.mapper.RoleMapper;
import com.dstz.org.dto.RemoveCheckRelationDTO;
import com.dstz.org.dto.SaveRoleDTO;
import com.dstz.org.vo.ResourceRoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Role management general service implementation class
 *
 */
@Service("roleManager")
public class RoleManagerImpl extends AbBaseManagerImpl<Role> implements RoleManager {

    @Autowired
    RoleMapper roleMapper;
    @Autowired
    OrgRelationManager orgRelationManager;


    /**
     * Save character
     *
     * @param saveRoleDTO Save role DTO
     */
    @Override
    public String saveRoleDto(SaveRoleDTO saveRoleDTO) {
        String desc;
        Role role = BeanCopierUtils.transformBean(saveRoleDTO, Role.class);
        // Check if the role already exists
        Assert.isFalse(isRoleExist(role),
                () -> new BusinessMessage(OrgStatusCode.ROLE_CODE_IS_EXIST));
        if (StrUtil.isEmpty(role.getId())) {
            roleMapper.insert(role);
            desc = "Add %s successfully";
        } else {
            roleMapper.updateById(role);
            desc = "Update %s successfully";
        }
        return desc;
    }

    @Override
    public boolean isRoleExist(Role role) {
        return !Objects.equals(roleMapper.isRoleExist(role), NumberPool.INTEGER_ZERO);
    }

    /**
     * Batch delete roles
     *
     * @param ids Entity ID Set
     * @return Number of deletions
     */
    @Override
    public int removeByIds(Collection<? extends Serializable> ids) {
        for (Serializable id : ids) {
            Assert.isFalse(StrUtil.isEmptyIfStr(id), () -> new BusinessMessage(OrgStatusCode.DEL_FAILED_PARAM_IS_EMPTY));
            orgRelationManager.beforeRemoveRelCheck(new RemoveCheckRelationDTO(null, id.toString()));
        }
        return super.removeByIds(ids);
    }

    /**
     * Search for roles by role code
     * Relative position interface implementation    GroupRelationApiImpl
     *
     * @param code Role code
     * @return Role
     */
    @Override
    public Role getByCode(String code) {
        return selectOne(Wrappers.lambdaQuery(Role.class).eq(Role::getCode, code));
    }

    /**
     * Get roles by user ID
     * Group business interface adaptation  AbGroupApiImpl
     *
     * @param userId User id
     * @return Character collection
     */
    @Override
    public List<Role> getByUserId(String userId) {
        return roleMapper.getByUserId(userId);
    }

    /**
     * Query the role list by role code set
     * Group business interface adaptation  AbGroupApiImpl
     *
     * @param codes Role code
     * @return Character collection
     */
    @Override
    public List<Role> selectByCodes(Collection<String> codes) {
        return roleMapper.selectList(Wrappers.lambdaQuery(Role.class).in(Role::getCode, codes));
    }

    /**
     * Get a list of roles on a resource
     *
     * @param resourcesId Resource ID
     * @return Resource Role List
     */
    @Override
    public List<ResourceRoleVO> getRoleListByResource(String resourcesId) {
        return roleMapper.getRoleListByResource(resourcesId);
    }
}
