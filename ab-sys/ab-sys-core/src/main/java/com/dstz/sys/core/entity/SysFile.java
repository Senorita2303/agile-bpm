package com.dstz.sys.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.dstz.base.common.valuemap.AbValueMap;
import com.dstz.base.common.valuemap.AbValueMapType;
import com.dstz.base.entity.AbModel;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * System accessories
 * </p>
 *
 */
@TableName("sys_file")
public class SysFile extends AbModel<SysFile> {

    /**
     * ID
     */
    @TableId(value = "id_", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * Attachment name
     */
    @TableField("name_")
    private String name;

    /**
     * Uploader
     */
    @TableField("uploader_")
    private String uploader;

    /**
     * Attachment path
     */
    @TableField("path_")
    private String path;

    /**
     * Dictionary categories
     */
    @AbValueMap(type = AbValueMapType.DICT, fixValue = "fileType", matchField = "code", attrMap = @AbValueMap.AttrMap(originName = "name"))
    @TableField("type_code_")
    private String typeCode;

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
     * Organizations affiliated with
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

    /**
     * Logical deletion marker
     */
    @TableField("delete_")
    private Integer delete;

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

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
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

    public Integer getDelete() {
        return delete;
    }

    public void setDelete(Integer delete) {
        this.delete = delete;
    }

    @Override
    public Serializable pkVal() {
        return this.id;
    }

    public SysFile(String id, String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }

    public SysFile() {
    }
}
