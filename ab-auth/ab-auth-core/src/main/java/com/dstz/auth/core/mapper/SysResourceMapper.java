package com.dstz.auth.core.mapper;

import com.dstz.auth.core.entity.SysResource;
import com.dstz.base.mapper.AbBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * System Permission Resource Definition Mapper Interface
 * </p>
 *
 */
@Mapper
public interface SysResourceMapper extends AbBaseMapper<SysResource> {
    /**
     * Get the definition object according to the subsystem ID.
     *
     * @param appId
     * @return
     */
    List<SysResource> getBySystemId(@Param("appId")String appId);

    /**
     * Get resources based on role and system id.
     *
     * @param appId
     * @param roleId
     * @return
     */
    List<SysResource> getBySystemAndRole(@Param("appId") String appId, @Param("roleId") String roleId);

    /**
     * Determine whether the resource exists.
     *
     * @param resource
     * @return
     */
    Integer isExist(SysResource resource);

    /**
     * Get the child node according to the parent ID.
     *
     * @param parentId
     * @return
     */
    List<SysResource> getByParentId(String parentId);

    /**
     * Get a list of resources based on system id and user id.
     *
     * @param appId   System ID
     * @param roleIds Role ID Collection
     * @return System resources
     */
    List<SysResource> getByAppIdAndRoleIds(@Param("appId") String appId, @Param("roleIds") Iterable<String> roleIds);
}
