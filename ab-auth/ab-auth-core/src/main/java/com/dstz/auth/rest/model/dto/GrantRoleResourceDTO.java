package com.dstz.auth.rest.model.dto;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * Role resource allocation
 * 
 */
public class GrantRoleResourceDTO {

	/**
	 * Role ID
	 */
	@NotEmpty(message = "Role ID cannot be empty")
	private String roleId;

	/**
	 * App ID
	 */
	@NotEmpty(message = "Application ID cannot be empty")
	private String appId;

	/**
	 * Resource ID
	 */
	private Set<String> resIds;

	/**
	 * Semi-selected resources
	 */
	private Set<String> halfResIds;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Set<String> getResIds() {
		return resIds;
	}

	public void setResIds(Set<String> resIds) {
		this.resIds = resIds;
	}

	public Set<String> getHalfResIds() {
		return halfResIds;
	}

	public void setHalfResIds(Set<String> halfResIds) {
		this.halfResIds = halfResIds;
	}

	@Override
	public String toString() {
		return "GrantRoleResourceDTO{" + "roleId='" + roleId + '\'' + ", appId='" + appId + '\'' + ", resIds=" + resIds
				+ ", halfResIds=" + halfResIds + '}';
	}
}
