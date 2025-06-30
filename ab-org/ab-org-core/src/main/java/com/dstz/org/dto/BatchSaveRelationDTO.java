package com.dstz.org.dto;

import javax.validation.constraints.NotBlank;

public class BatchSaveRelationDTO implements java.io.Serializable {

	/**
	 * Group ID
	 */
	@NotBlank(message = "Organization cannot be empty")
	private String groupIds;

	/**
	 * Role ID
	 */
	@NotBlank(message = "Role cannot be empty")
	private String roleIds;

	public String getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
}
