package com.dstz.auth.core.manager;

import com.dstz.auth.authentication.api.model.ISysResource;
import com.dstz.auth.core.entity.SysResource;
import com.dstz.auth.rest.model.dto.SysResourceDTO;
import com.dstz.auth.rest.model.vo.SysResourceVO;
import com.dstz.base.manager.AbBaseManager;

import java.util.List;

/**
 * <p>
 * System authority resource definition General business class
 * </p>
 *
 */
public interface SysResourceManager extends AbBaseManager<SysResource> {

    /**
     * Get a list of entities by subsystem ID.
     */
    List<SysResource> getBySystemId(String id);


    /**
     * Get resources by system and role ID.
     *
     * @param systemId
     * @param roleId
     * @return
     */
    List<SysResource> getBySystemAndRole(String systemId, String roleId);

    /**
     * Determine whether the resource exists.
     *
     * @param resource
     * @return
     */
    boolean isExist(SysResource resource);

    /**
     * Recursively delete resource data based on resource id.
     *
     * @param resId
     */
    void removeByResId(String resId);

    /**
     * Get resources based on system id and user id.
     *
     * @param appId  App ID
     * @param userId User ID
     * @return Resources List
     */
    List<SysResource> getByAppIdAndUserId(String appId, String userId);

    /**
     * Get resource details based on id
     *
     * @param id
     * @param systemId
     * @param parentId
     * @return
     */
    SysResourceVO getResourceDetailById(String id, String parentId, String systemId);


    /**
     * Save the tree structure
     * @param sysResource
     */
    void saveTree(SysResourceDTO sysResource);

    /**
     * Save a single resource
     * @param sysResource
     */
    String saveSysResource(SysResource sysResource);

    /**
     * Delete resource objects by resource code collection
     * @param  codeList  Resource code collection
     */
    void deleteResourceByCode(List<String> codeList);
}
