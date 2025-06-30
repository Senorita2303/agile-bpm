package com.dstz.org.dto;

import com.dstz.base.common.constats.NumberPool;

import java.util.Date;

public class OrgRelationDTO implements java.io.Serializable {

	/**
	 * ID
	 */
	private String id;

	/**
	 * Group ID
	 */
	private String groupId;

	/**
	 * Role ID
	 */
	private String userId;

	/**
	 * 0: default organization, 1: secondary organization
	 */
	private Integer isMaster = NumberPool.INTEGER_ZERO;

	/**
	 * Role ID
	 */
	private String roleId;

	/**
	 * Status: 1 enabled, 0 disabled
	 */
	private Integer status = NumberPool.INTEGER_ONE;

	/**
	 * 类型：groupUser,groupRole,userRole,groupUserRole
	 */
	private String type;

	/**
	 * Creation time
	 */
	private Date createTime;

	/**
	 * Creator ID
	 */
	private String createBy;

	/**
	 * Organizations affiliated with
	 */
	private String createOrgId;

	/**
	 * Update time
	 */
	private Date updateTime;

	/**
	 * Updater
	 */
	private String updater;

	/**
	 * Updater ID
	 */
	private String updateBy;

	/**
	 * Frontend fields
	 */

	private String[] roleIds;

	private String[] userIds;

	public OrgRelationDTO() {
	}

	public String[] getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String[] roleIds) {
		this.roleIds = roleIds;
	}

	public String[] getUserIds() {
		return userIds;
	}

	public void setUserIds(String[] userIds) {
		this.userIds = userIds;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getIsMaster() {
		return isMaster;
	}

	public void setIsMaster(Integer isMaster) {
		this.isMaster = isMaster;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getCreateOrgId() {
		return createOrgId;
	}

	public void setCreateOrgId(String createOrgId) {
		this.createOrgId = createOrgId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
}
