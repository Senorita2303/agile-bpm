package com.dstz.auth.authentication.api;

import com.dstz.auth.authentication.api.model.ISysApplication;
import com.dstz.auth.authentication.api.model.ISysResource;

import java.util.List;
import java.util.Set;
/**
 * System Resource Interface
 *
 */
public interface SysResourceApi {

	/**
	 * Get resource object by resource id
	 * @param  id  Resource ID
	 * @return ISysResource Resource Object
	 */
	ISysResource getResourceById(String id);

	/**
	 * Delete resource objects by resource code collection
	 * @param  code  Resource code collection
	 */
	 void deleteResourceByCode(List<String> code);

	/**
	 * Get the system owned by the current user
	 * @return
	 */
	List<ISysApplication> getCurrentUserSystem();
	/**
	 * Get the default system
	 * @return
	 */
	ISysApplication getDefaultSystem(String currentUserId);
	/**
	 * Get system resources by id
	 * @return
	 */
	List<ISysResource> getBySystemId(String systemId);
	/**
	 * Get system resources based on systemId and userId
	 * @return
	 */
	List<ISysResource> getByAppIdAndUser(String appId, String userId);
	/**
	 * Get the resource according to the URL
	 * @return
	 */
	Set<String> getAccessRoleByUrl(String url);

	/**
	 * According to the url query is url or method interface
	 */
	boolean isRoleByUrl(String url);

	/**
	 * Get resources for mobile tasks
	 */
	ISysResource getTodoResource();

	void deleteByCode(List<String> codeList);
}
