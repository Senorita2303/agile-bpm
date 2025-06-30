package com.dstz.auth.core.mapper;

import com.dstz.auth.core.entity.ResourceRole;
import com.dstz.base.mapper.AbBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * Resource Role Association Table Mapper Interface
 * </p>
 *
 */
@Mapper
public interface ResourceRoleMapper extends AbBaseMapper<ResourceRole> {
    /**
     * Get resource association information based on roleId
     * @param roleId
     * @return
     */
    List<ResourceRole> getByRoleId(String roleId);

    /**
     * Remove resource associations based on roleId and appId
     * @param roleId
     * @param appId
     */
    void removeByRoleAndSystem(@Param("roleId")String roleId, @Param("appId")String appId);

    /**
     * Get the mapping relationship between resources and roles
     * @return
     */
    List<ResourceRole> getAllResRole();

    /**
     * Get the role ID based on the resource URL
     *
     * @param url Resource URL
     * @return Role ID Collection
     */
    Set<String> getRoleIdByResourceUrl(String url);

    /**
     * Query the application ID set based on the role ID
     *
     * @param roleIds Role ID
     * @return Application ID Collection
     */
    Set<String> selectAppIdByRoleIds(@Param("roleIds") Iterable<String> roleIds);
}
