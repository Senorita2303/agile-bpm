package com.dstz.cms.core.entity.vo;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * <p>
 * Quick menu table
 * </p>
 *
 */
@TableName("cms_fast_menu")
public class CmsFastMenuVO implements Serializable{

    /**
     * Primary Key
     */
    private String id;

    /**
     * Owner
     */
    private String userId;

    /**
     * Resource ID
     */
    private String resourceId;

    /**
     * Resource Name
     */
    private String resourceName;

    /**
     * Icon code
     */
    private String icon;

    /**
     * Resource address
     */
    private String resourceUrl;

    public CmsFastMenuVO(String id, String userId, String resourceId, String resourceName, String resourceUrl,String icon) {
        this.id = id;
        this.userId = userId;
        this.resourceId = resourceId;
        this.resourceName = resourceName;
        this.resourceUrl = resourceUrl;
        this.icon = icon;
    }

    public CmsFastMenuVO( String resourceId, String resourceName, String resourceUrl,String icon) {
        this.resourceId = resourceId;
        this.resourceName = resourceName;
        this.resourceUrl = resourceUrl;
        this.icon = icon;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
