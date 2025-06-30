package com.dstz.org.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.dstz.base.common.valuemap.AbValueMap;
import com.dstz.base.common.valuemap.AbValueMapType;
import com.dstz.base.entity.AbModel;
import com.dstz.org.api.enums.GroupGradeConstant;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Organizational Structure
 * </p>
 *
 */
@TableName("org_group")
public class Group extends AbModel<Group> {

    /**
     * Primary Key
     */
    @TableId(value = "id_", type = IdType.ASSIGN_ID)
    protected String id;

    /**
     * name
     */
    @TableField("name_")
    protected String name;

    /**
     * Parent ID
     */
    @TableField("parent_id_")
    protected String parentId;

    /**
     * Sorting
     */
    @TableField("sn_")
    protected Integer sn;

    @TableField("code_")
    protected String code;

    /**
     * Type: 0 Group, 1 Company, 3 Department
     */
    @AbValueMap(type = AbValueMapType.ENUM, fixClass = GroupGradeConstant.class, matchField = "key", attrMap = {@AbValueMap.AttrMap(originName = "label", targetName = "groupType")})
    @TableField("type_")
    protected String type;

    /**
     * describe
     */
    @TableField("desc_")
    protected String desc;

    /**
     * Organizational Path
     */
    @TableField("path_")
    protected String path;

    /**
     * Organizational Path
     */
    @TableField("path_name_")
    protected String pathName;

    /**
     * Creation time
     */
    @TableField(value = "create_time_", fill = FieldFill.INSERT)
    protected Date createTime;

    /**
     * Creator ID
     */
    @TableField(value = "create_by_", fill = FieldFill.INSERT)
    protected String createBy;

    /**
     * Organization
     */
    @TableField(value = "create_org_id_", fill = FieldFill.INSERT)
    protected String createOrgId;

    /**
     * Update time
     */
    @TableField(value = "update_time_", fill = FieldFill.INSERT_UPDATE)
    protected Date updateTime;

    /**
     * Updater
     */
    @TableField(value = "updater_", fill = FieldFill.INSERT_UPDATE)
    protected String updater;

    /**
     * Updater ID
     */
    @TableField(value = "update_by_", fill = FieldFill.INSERT_UPDATE)
    protected String updateBy;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getSn() {
        return sn;
    }

    public void setSn(Integer sn) {
        this.sn = sn;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
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


}
