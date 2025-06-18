package com.dstz.org.api;

import com.dstz.org.api.model.IGroup;

import java.util.List;

/**
 * 组织相关接口
 *
 * @author wacxhs
 */
public interface OrgApi {

	/**
	 * 根据组织ID和级别获取数据
	 *
	 * @param orgId 组织ID
	 * @param grade 组织级别
	 * @return
	 */
	IGroup getByOrgIdAndGrade(String orgId, String grade);


	/**
	 * 根据组织ID获取子集数据
	 *
	 * @param orgId 组织ID
	 * @return 子集数据（包括传入父及）
	 */
	List<IGroup> getHierarchyByOrgId(String orgId);

	/**
	 * 根据组织ID和级别获取层级数据列表
	 *
	 * @param orgId 组织ID
	 * @param grade 组织级别
	 * @return 层级数据列表
	 */
	List<IGroup> getHierarchyByOrgIdAndGrade(String orgId, String grade);
}
