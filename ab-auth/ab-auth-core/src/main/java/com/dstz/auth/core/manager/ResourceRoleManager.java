package com.dstz.auth.core.manager;

import com.dstz.auth.core.entity.ResourceRole;
import com.dstz.auth.rest.model.dto.GrantRoleResourceDTO;
import com.dstz.auth.rest.model.vo.SysResourceTreeVO;
import com.dstz.base.manager.AbBaseManager;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * Resource role association table General business class
 * </p>
 *
 */
public interface ResourceRoleManager extends AbBaseManager<ResourceRole> {
    /**
     * Query by id
     * @param roleId
     */
    List<ResourceRole> getAllByRoleId(String roleId);

    /**
     * Assign role resources.
     * 
     * @param dto grant dto 
     */
    void grantRoleResource(GrantRoleResourceDTO dto);

    /**
     * Get accessible roles through url
     * @param url
     * @return
     */
    Set<String> getAccessRoleByUrl(String url);

    /**
     * Query the resource tree based on the role id and code
     * @param roleId
     * @param code
     * @return
     */
    List<SysResourceTreeVO> getRoleResTreeData(String roleId, String code);

    /**
     * Query the resource tree based on the system ID and role ID
     * @param systemId
     * @param roleId
     * @return
     */
    List<SysResourceTreeVO> getRoleRes(String systemId, String roleId);
}
