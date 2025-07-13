package com.dstz.auth.core.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dstz.auth.authentication.api.model.ISysApplication;
import com.dstz.base.entity.AbModel;
import java.util.Date;
import java.io.Serializable;

/**
 * <p>
 * Application
 * </p>
 *
 */
@TableName("sys_application")
public class SysApplication extends AbModel<SysApplication>  implements ISysApplication {

    /**
     * ID
     */
    @TableId(value = "id_", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * Application Name
     */
    @TableField("name_")
    private String name;

    /**
     * Code
     */
    @TableField("code_")
    private String code;

    /**
     * Key
     */
    @TableField("secret_")
    private String secret;

    /**
     * Resource Collection
     */
    @TableField("resource_ids_")
    private String resourceIds;

    /**
     * Scope of Authorization
     */
    @TableField("scope_")
    private String scope;

    /**
     * Refresh seconds
     */
    @TableField("refresh_token_validity_")
    private Integer refreshTokenValidity;

    /**
     * Validity
     */
    @TableField("access_token_validity_")
    private Integer accessTokenValidity;

    /**
     * Authorization Type
     */
    @TableField("grant_types_")
    private String grantTypes;

    /**
     * Automatic authorization
     */
    @TableField("autoapprove_")
    private Integer autoapprove;

    /**
     * Permissions
     */
    @TableField("authorities_")
    private String authorities;

    /**
     * System address, if empty, it is the current system
     */
    @TableField("url_")
    private String url;

    /**
     * Callback address
     */
    @TableField("redirect_uri_")
    private String redirectUri;

    /**
     * Open
     */
    @TableField("open_type_")
    private String openType;

    /**
     * Availability
     */
    @TableField("enabled_")
    private Integer enabled;

    /**
     * Is it enabled by default?
     */
    @TableField("is_default_")
    private Integer isDefault;

    /**
     * Description Notes
     */
    @TableField("desc_")
    private String desc;

    /**
     * Extended Configuration
     */
    @TableField("config_")
    private String config;

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
     * Updater ID
     */
    @TableField(value = "update_by_", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * Updater
     */
    @TableField(value = "updater_", fill = FieldFill.INSERT_UPDATE)
    private String updater;


    /**
     * Application type 0 web application 1 mobile terminal 2 third-party application
     */
    @TableField("app_type_")
    private Integer appType;

    @Override
    public Integer getAppType() {
        return appType;
    }

    public void setAppType(Integer appType) {
        this.appType = appType;
    }

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
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getSecret() {
        return secret;
    }
    
    public void setSecret(String secret) {
        this.secret = secret;
    }
    
    public String getResourceIds() {
        return resourceIds;
    }
    
    public void setResourceIds(String resourceIds) {
        this.resourceIds = resourceIds;
    }
    
    public String getScope() {
        return scope;
    }
    
    public void setScope(String scope) {
        this.scope = scope;
    }
    
    public Integer getRefreshTokenValidity() {
        return refreshTokenValidity;
    }
    
    public void setRefreshTokenValidity(Integer refreshTokenValidity) {
        this.refreshTokenValidity = refreshTokenValidity;
    }
    
    public Integer getAccessTokenValidity() {
        return accessTokenValidity;
    }
    
    public void setAccessTokenValidity(Integer accessTokenValidity) {
        this.accessTokenValidity = accessTokenValidity;
    }
    
    public String getGrantTypes() {
        return grantTypes;
    }
    
    public void setGrantTypes(String grantTypes) {
        this.grantTypes = grantTypes;
    }
    
    public Integer getAutoapprove() {
        return autoapprove;
    }
    
    public void setAutoapprove(Integer autoapprove) {
        this.autoapprove = autoapprove;
    }
    
    public String getAuthorities() {
        return authorities;
    }
    
    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getRedirectUri() {
        return redirectUri;
    }
    
    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }
    
    public String getOpenType() {
        return openType;
    }
    
    public void setOpenType(String openType) {
        this.openType = openType;
    }
    
    public Integer getEnabled() {
        return enabled;
    }
    
    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }
    
    public Integer getIsDefault() {
        return isDefault;
    }
    
    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    public String getConfig() {
        return config;
    }



    public void setConfig(String config) {
        this.config = config;
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
    public String getUpdateBy() {
        return updateBy;
    }
    
    @Override
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
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
    public Serializable pkVal() {
        return this.id;
    }
}
