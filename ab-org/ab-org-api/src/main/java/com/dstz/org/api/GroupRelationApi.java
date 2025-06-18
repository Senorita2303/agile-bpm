package com.dstz.org.api;

import com.dstz.org.api.model.IGroup;

import java.util.List;

/**
 * @author Jeff
 * @描述 组关系服务，比如获取指定部门岗位，指定部门上级岗位
 * @可不实现 这些组织并非ORG 通用的API规范，可实现可不实现，
 * @注意： 若不实现则无法使用以下【流程人员插件】
 * 1、相当岗位插件
 */
public interface GroupRelationApi {

	/**
	 * 通过组织ID，角色获取 岗位,如获取某部门的部门负责人
	 *
	 * @param orgIds
	 * @param roleKeys
	 * @return
	 */
	List<? extends IGroup> getPostByGroupAndRoles(String orgIds, String roleKeys);

	/**
	 * 获取指定组织上级的指定岗位用户，如获取某部门上级的部门负责人
	 *
	 * @param orgIds
	 * @param roleKeys
	 * @return
	 */
	List<? extends IGroup> getPostByGroupParentAndRoles(String orgIds, String roleKeys);

	/**
	 * 获取指定组织上级中，指定级别的组织岗位，如某部门获取上两级的组织负责人
	 *
	 * @param orgIds
	 * @param parentOrgSpecicalLevel
	 * @param roleKeys
	 * @return
	 */
	List<? extends IGroup> getPostByGroupSpecicalLevelParentAndRoles(String orgIds, Integer parentOrgSpecicalLevel,
	                                                                 String roleKeys);

	/**
	 * 获取指定组织上级中，指定组织类型的岗位，如某部门获取上级为分公司的公司总监
	 *
	 * @param orgIds
	 * @param parentOrgFilterType
	 * @param roleKeys
	 * @return
	 */
	List<? extends IGroup> getPostByGroupSpecicalTypeParentAndRoles(String orgIds, Integer parentOrgFilterType, String roleKeys);

	/**
	 * 获取下级中指定岗位的人员
	 *
	 * @param orgIds
	 * @param roleKey
	 * @return
	 */
	List<? extends IGroup> getPostByGroupChildAndRoles(String orgIds, String roleKey);

	/**
	 * 获取下级中指定类型的人员
	 *
	 * @param orgIds
	 * @param parentOrgFilterType
	 * @param roleKey
	 * @return
	 */
	List<? extends IGroup> getPostByGroupSpecicalTypeChildAndRoles(String orgIds, Integer parentOrgFilterType, String roleKey);
}
