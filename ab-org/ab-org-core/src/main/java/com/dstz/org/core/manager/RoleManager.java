package com.dstz.org.core.manager;

import com.dstz.base.manager.AbBaseManager;
import com.dstz.org.core.entity.Role;
import com.dstz.org.dto.SaveRoleDTO;
import com.dstz.org.vo.ResourceRoleVO;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * Role Management General Business Class
 * </p>
 *
 */
public interface RoleManager extends AbBaseManager<Role> {

    String saveRoleDto(SaveRoleDTO saveRoleDTO);

    /**
     * Determines whether the role exists.
     *
     * @param role
     * @return
     */
    boolean isRoleExist(Role role);

    /**
     * Get the role according to the code
     *
     * @param code Role code
     * @return Role
     */
    Role getByCode(String code);

    /**
     * Get the role using the user ID
     *
     * @param userId User ID
     * @return Character Collection
     */
    List<Role> getByUserId(String userId);

    /**
     * Query the role list by role code set
     *
     * @param codes List Code
     * @return Character Collection
     */
    List<Role> selectByCodes(Collection<String> codes);

    /**
     * Get role collection based on resources
     *
     * @param resourcesId Resource ID
     * @return Character Collection
     */
    List<ResourceRoleVO> getRoleListByResource(String resourcesId);

}
