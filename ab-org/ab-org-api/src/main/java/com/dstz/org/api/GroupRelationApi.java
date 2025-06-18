package com.dstz.org.api;

import com.dstz.org.api.model.IGroup;

import java.util.List;

/**
 * @Description Group relationship services, such as obtaining positions in a specified department and the superior positions in a specified department
 * @Optional These organizations are not ORGs. Common API specifications can be implemented or not.
 * @Note: If this is not implemented, the following [Process Personnel Plug-in] cannot be used
 * 1. Equivalent post plug-in
 */
public interface GroupRelationApi {

	/**
	 * Get positions through organization ID and role, such as getting the head of a department
	 *
	 * @param orgIds
	 * @param roleKeys
	 * @return
	 */
	List<? extends IGroup> getPostByGroupAndRoles(String orgIds, String roleKeys);

	/**
	 * Get the user of the specified position of the superior of the specified organization, such as getting the head of the superior of a department
	 *
	 * @param orgIds
	 * @param roleKeys
	 * @return
	 */
	List<? extends IGroup> getPostByGroupParentAndRoles(String orgIds, String roleKeys);

	/**
	 * Get the organizational position of the specified level in the specified organization's superior, such as getting the organizational head of the two levels above in a department
	 *
	 * @param orgIds
	 * @param parentOrgSpecicalLevel
	 * @param roleKeys
	 * @return
	 */
	List<? extends IGroup> getPostByGroupSpecicalLevelParentAndRoles(String orgIds, Integer parentOrgSpecicalLevel,
			String roleKeys);

	/**
	 * Get the position of the specified organization type in the specified organization's superior, such as a department gets the company director whose superior is a branch company
	 *
	 * @param orgIds
	 * @param parentOrgFilterType
	 * @param roleKeys
	 * @return
	 */
	List<? extends IGroup> getPostByGroupSpecicalTypeParentAndRoles(String orgIds, Integer parentOrgFilterType,
			String roleKeys);

	/**
	 * Get the personnel in the specified position among the subordinates
	 *
	 * @param orgIds
	 * @param roleKey
	 * @return
	 */
	List<? extends IGroup> getPostByGroupChildAndRoles(String orgIds, String roleKey);

	/**
	 * Get the specified type of personnel in the subordinates
	 *
	 * @param orgIds
	 * @param parentOrgFilterType
	 * @param roleKey
	 * @return
	 */
	List<? extends IGroup> getPostByGroupSpecicalTypeChildAndRoles(String orgIds, Integer parentOrgFilterType,
			String roleKey);
}
