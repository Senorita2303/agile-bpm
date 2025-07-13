package com.dstz.cms.core.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dstz.base.common.utils.UserContextUtils;
import com.dstz.base.entity.AbModel;

import java.util.Date;
import java.io.Serializable;

/**
 * <p>
 * Quick menu table
 * </p>
 *
 */
@TableName("cms_fast_menu")
public class CmsFastMenu extends AbModel<CmsFastMenu> {

    /**
     * Primary Key
     */
    @TableId(value = "id_", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * Owner
     */
    @TableField("user_id_")
    private String userId;

    /**
     * Resource ID
     */
    @TableField("resource_id_")
    private String resourceId;

    /**
     * Is it a mobile terminal (0 No 1 Yes)
     */
    @TableField("mobile_")
    private int mobile;

    /**
     * Creation time
     */
    @TableField(value = "create_time_", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * Founder
     */
    @TableField(value = "create_by_", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * Customized construction method for batch saving on PC
     */
    public CmsFastMenu(String userId, String resourceId) {
        this.userId = userId;
        this.resourceId = resourceId;
        this.mobile = 0;
    }

    /**
     * Customized construction method for batch saving on mobile terminals
     */
    public CmsFastMenu(String resourceId) {
        this.userId = UserContextUtils.getUserId();
        this.resourceId = resourceId;
        this.mobile = 1;
    }

    public CmsFastMenu(String id, String userId, String resourceId, int mobile, Date createTime, String createBy) {
        this.id = id;
        this.userId = userId;
        this.resourceId = resourceId;
        this.mobile = mobile;
        this.createTime = createTime;
        this.createBy = createBy;
    }

    public CmsFastMenu() {
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
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

    @Override
    public Serializable pkVal() {
        return this.id;
    }

    public int getMobile() {
        return mobile;
    }

    public void setMobile(int mobile) {
        this.mobile = mobile;
    }
}
