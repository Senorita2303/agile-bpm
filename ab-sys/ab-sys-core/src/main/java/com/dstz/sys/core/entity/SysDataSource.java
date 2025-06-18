package com.dstz.sys.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.dstz.base.common.utils.JsonUtils;
import com.dstz.base.common.valuemap.AbValueMap;
import com.dstz.base.common.valuemap.AbValueMapType;
import com.dstz.base.entity.AbModel;
import com.dstz.base.enums.AbDbType;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * Data Source
 * </p>
 *
 */
@TableName("sys_data_source")
public class SysDataSource extends AbModel<SysDataSource> {

    /**
     * ID
     */
    @TableId(value = "id_", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * Alias
     */
    @TableField("alias_")
    private String alias;

    /**
     * Name
     */
    @TableField("name_")
    private String name;

    /**
     * Description
     */
    @TableField("desc_")
    private String desc;

    /**
     * Type
     */
    @TableField("db_type_")
    @AbValueMap(type = AbValueMapType.ENUM, fixClass = AbDbType.class, matchField = "db", attrMap = @AbValueMap.AttrMap(originName = "desc"))
    private String dbType;

    /**
     * java classpath
     */
    @TableField("class_path_")
    private String classPath;

    /**
     * Configuration properties
     */
    @TableField("attributes_json_")
    private String attributesJson;

    /**
     * Database template Id
     */
    @TableField("def_id_")
    private String defId;

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
     * Configuration from the library
     */
    @TableField("replica_")
    private String replica;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public String getAttributesJson() {
        return attributesJson;
    }

    public void setAttributesJson(String attributesJson) {
        this.attributesJson = attributesJson;
    }

    public String getDefId() {
        return defId;
    }

    public void setDefId(String defId) {
        this.defId = defId;
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

    public String getReplica() {
        return replica;
    }

    public void setReplica(String replica) {
        this.replica = replica;
    }
}
