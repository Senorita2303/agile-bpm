package com.dstz.org.api;

import com.dstz.org.api.model.IGroup;

import java.util.List;

/**
 * Organization related interfaces
 *
 */
public interface OrgApi {

	/**
	 * Get data based on organization ID and level 
	 *
	 * @param orgId Organization ID
	 * @param grade Organizational level
	 * @return
	 */
	IGroup getByOrgIdAndGrade(String orgId, String grade);


	/**
	 * Get subset data based on organization ID
	 *
	 * @param orgId Organization ID
	 * @return Subset data (including the parent data passed in)
	 */
	List<IGroup> getHierarchyByOrgId(String orgId);

	/**
	 * Get a list of hierarchical data based on organization ID and level
	 *
	 * @param orgId Organization ID
	 * @param grade Organizational level
	 * @return Hierarchical data list
	 */
	List<IGroup> getHierarchyByOrgIdAndGrade(String orgId, String grade);
}
