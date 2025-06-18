package com.dstz.org.vo;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.dstz.org.core.entity.Role;

/**
 * Organization Position VO
 *
 */
public class OrgPostVO {

	private String roleId;

	private String roleCode;

	private String roleName;

	private String groupId;

	private String groupCode;

	private String groupName;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Override
	public String toString() {
		return "OrgPostVO{" + "roleId='" + roleId + '\'' + ", roleCode='" + roleCode + '\'' + ", roleName='" + roleName
				+ '\'' + ", groupId='" + groupId + '\'' + ", groupCode='" + groupCode + '\'' + ", groupName='"
				+ groupName + '\'' + '}';
	}

	public Role toRole() {
		Role role = new Role();
		role.setId(StrUtil.join(StrPool.UNDERLINE, getGroupId(), getRoleId()));
		role.setCode(StrUtil.join(StrPool.UNDERLINE, getGroupCode(), getRoleCode()));
		role.setName(StrUtil.join(StrPool.DASHED, getGroupName(), getRoleName()));
		return role;
	}
}
