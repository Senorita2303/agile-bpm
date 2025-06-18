package com.dstz.org.core.mapper;

import com.dstz.base.mapper.AbBaseMapper;
import com.dstz.org.core.entity.Role;
import com.dstz.org.vo.ResourceRoleVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * Role Management Mapper Interface
 * </p>
 *
 */
@Mapper
public interface RoleMapper extends AbBaseMapper<Role> {

	/**
	 * Get the role list based on the resource id
	 *
	 * @param resourcesId Resource ID
	 * @return Character Collection
	 */
	List<ResourceRoleVO> getRoleListByResource(String resourcesId);

	/**
	 * Get the role using the user ID
	 *
	 * @param userId User ID
	 * @return Character Collection
	 */
	List<Role> getByUserId(String userId);

	/**
	 * Determine whether the role exists in the system.
	 *
	 * @param role Role
	 * @return  count
	 */
	Integer isRoleExist(Role role);
}
