package com.dstz.org.vo;

import cn.hutool.core.util.StrUtil;
import com.dstz.base.common.constats.StrPool;
import com.dstz.base.common.valuemap.AbValueMap;
import com.dstz.base.common.valuemap.AbValueMapType;
import com.dstz.org.enums.OrgMaster;
import com.dstz.org.enums.OrgStatus;
import com.dstz.org.enums.RelationTypeConstant;

import java.util.Date;

/**
 * Associated Users
 *
 */
public class OrgRelationUserVO implements java.io.Serializable {

	/**
	 * Association ID
	 */
	private String id;

	/**
	 * Associated Types {@link RelationTypeConstant}
	 */
	private String type;

	/**
	 * Group ID
	 */
	private String groupId;

	/**
	 * Group Name
	 */
	private String groupName;

	/**
	 * 0: default organization, 1: secondary organization
	 */
	@AbValueMap(type = AbValueMapType.ENUM, fixClass = OrgMaster.class, matchField = "master", attrMap = {
			@AbValueMap.AttrMap(originName = "desc", targetName = "isMasterDesc"),
			@AbValueMap.AttrMap(originName = "labelCss", targetName = "isMasterCss") })
	private Integer isMaster;

	/**
	 * 1: Enable, 0: Disable
	 */
	@AbValueMap(type = AbValueMapType.ENUM, fixClass = OrgStatus.class, matchField = "status", attrMap = {
			@AbValueMap.AttrMap(originName = "desc", targetName = "statusDesc"),
			@AbValueMap.AttrMap(originName = "labelCss", targetName = "statusCss") })
	private Integer status;

	/**
	 * User ID
	 */
	private String userId;

	/**
	 * User Accounts
	 */
	private String userAccount;

	/**
	 * User Name
	 */
	private String userFullname;

	/**
	 * Character Name
	 */
	private String roleName;

	/**
	 * Role code
	 */
	private String roleCode;

	/**
	 * Role ID
	 */
	private String roleId;

	/**
	 * Creation time
	 */
	private Date createTime;

	/**
	 * Get job title
	 *
	 * @return Position Title
	 */
	public String getPostName() {
		if (StrUtil.isNotEmpty(groupName) && StrUtil.isNotEmpty(getRoleName())) {
			return String.format("%s-%s", getGroupName(), getRoleName());
		} else {
			return StrUtil.EMPTY;
		}
	}

	/**
	 * Get the job ID
	 *
	 * @return Position ID
	 */
	public String getPostId() {
		if (StrUtil.isNotEmpty(getGroupId()) && StrUtil.isNotEmpty(getRoleId())) {
			return String.format(StrPool.FORMATSTR, getGroupId(), getRoleId());
		} else {
			return StrUtil.EMPTY;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Integer getIsMaster() {
		return isMaster;
	}

	public void setIsMaster(Integer isMaster) {
		this.isMaster = isMaster;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getUserFullname() {
		return userFullname;
	}

	public void setUserFullname(String userFullname) {
		this.userFullname = userFullname;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "OrgRelationUserVO{" + "id='" + id + '\'' + ", type='" + type + '\'' + ", groupId='" + groupId + '\''
				+ ", groupName='" + groupName + '\'' + ", isMaster=" + isMaster + ", status=" + status + ", userId='"
				+ userId + '\'' + ", userAccount='" + userAccount + '\'' + ", userFullname='" + userFullname + '\''
				+ ", roleName='" + roleName + '\'' + ", roleCode='" + roleCode + '\'' + ", roleId='" + roleId + '\''
				+ ", createTime=" + createTime + '}';
	}
}
