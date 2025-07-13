package com.dstz.org.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.dstz.base.common.constats.NumberPool;
import com.dstz.base.common.valuemap.AbValueMap;
import com.dstz.base.common.valuemap.AbValueMapType;
import com.dstz.base.entity.AbModel;
import com.dstz.org.enums.OrgMaster;
import com.dstz.org.enums.OrgStatus;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 */
@TableName("org_relation")
public class OrgRelation extends AbModel<OrgRelation> {

    /**
     * ID
     */
    @TableId(value = "id_", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * Group ID
     */
    @TableField("group_id_")
    private String groupId;
    /**
     * User ID
     */
    @TableField("user_id_")
    private String userId;
    /**
     * 0: default organization, 1: primary organization
     */
    @AbValueMap(type = AbValueMapType.ENUM, fixClass = OrgMaster.class, matchField = "master",
            attrMap = {@AbValueMap.AttrMap(originName = "desc", targetName = "isMasterDesc"),
                    @AbValueMap.AttrMap(originName = "labelCss", targetName = "isMasterCss")
            })
    @TableField("is_master_")
    private Integer isMaster = NumberPool.INTEGER_ZERO;
    /**
     * Role ID
     */
    @TableField("role_id_")
    private String roleId;
    /**
     * Status: 1 for enabled, 0 for disabled
     */
    @AbValueMap(type = AbValueMapType.ENUM, fixClass = OrgStatus.class, matchField = "status",
            attrMap = {@AbValueMap.AttrMap(originName = "desc", targetName = "statusDesc"),
                    @AbValueMap.AttrMap(originName = "labelCss", targetName = "statusCss")
            })
    @TableField("status_")
    private Integer status = NumberPool.INTEGER_ONE;
    /**
     * Type: groupUser, groupRole, userRole, groupUserRole
     */
    @TableField("type_")
    private String type;
    /**
     * Creation time
     */
    @TableField(value = "create_time_", fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * Creator ID
     */
    @TableField(value = "create_by_", fill = FieldFill.INSERT)
    private String createBy;
    /**
     * Organization
     */
    @TableField(value = "create_org_id_", fill = FieldFill.INSERT)
    private String createOrgId;
    /**
     * Update time
     */
    @TableField(value = "update_time_", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    /**
     * Updater
     */
    @TableField(value = "updater_", fill = FieldFill.INSERT_UPDATE)
    private String updater;
    /**
     * Updater ID
     */
    @TableField(value = "update_by_", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    public OrgRelation() {
    }

    public OrgRelation(String groupId, String userId, String roleId, String type) {
        this.groupId = groupId;
        this.userId = userId;
        this.roleId = roleId;
        this.type = type;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
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

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String getCreateBy() {
        return createBy;
    }

    @Override
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateOrgId() {
        return createOrgId;
    }

    public void setCreateOrgId(String createOrgId) {
        this.createOrgId = createOrgId;
    }

    @Override
    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String getUpdater() {
        return updater;
    }

    @Override
    public void setUpdater(String updater) {
        this.updater = updater;
    }

    @Override
    public String getUpdateBy() {
        return updateBy;
    }

    @Override
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Override
    public Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "OrgRelation{" +
                "id=" + id +
                ", groupId=" + groupId +
                ", userId=" + userId +
                ", isMaster=" + isMaster +
                ", roleId=" + roleId +
                ", status=" + status +
                ", type=" + type +
                ", createTime=" + createTime +
                ", createBy=" + createBy +
                ", createOrgId=" + createOrgId +
                ", updateTime=" + updateTime +
                ", updater=" + updater +
                ", updateBy=" + updateBy +
                "}";
    }
}
