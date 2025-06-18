package com.dstz.sys.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.dstz.base.common.constats.NumberPool;
import com.dstz.base.common.freemark.IFreemarkerEngine;
import com.dstz.base.common.valuemap.AbValueMap;
import com.dstz.base.common.valuemap.AbValueMapType;
import com.dstz.base.entity.AbModel;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * System properties
 * </p>
 *
 */
@TableName("sys_properties")
public class SysProperties extends AbModel<SysProperties> {

    /**
     * ID
     */
    @TableId(value = "id_", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * Attribute code
     */
    @TableField("code_")
    private String code;

    /**
     * Attribute name
     */
    @TableField("name_")
    private String name;

    /**
     * Value
     */
    @TableField("value_")
    private String value;

    /**
     * Description
     */
    @TableField("desc_")
    private String desc;

    /**
     * Is it encrypted?
     */
    @TableField("encrypt_")
    private Integer encrypt;

    /**
     * Environmental parameters
     */
    @TableField("environment_")
    @AbValueMap(type = AbValueMapType.DICT, fixValue = "environment", matchField = "code", attrMap = @AbValueMap.AttrMap(originName = "name"))
    private String environment;

    /**
     * Grouping
     */
    @TableField("type_code_")
   // @AbValueMap(type = AbValueMapType.DICT, fixValue = "property", matchField = "code", attrMap = @AbValueMap.AttrMap(originName = "name"))
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
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public void setId(String id) {
        this.id = id;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    public Integer getEncrypt() {
        if(encrypt == null){
            return NumberPool.BOOLEAN_FALSE;
        }
        return encrypt;
    }
    
    public void setEncrypt(Integer encrypt) {
        this.encrypt = encrypt;
    }
    
    public String getEnvironment() {
        return environment;
    }
    
    public void setEnvironment(String environment) {
        this.environment = environment;
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

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
