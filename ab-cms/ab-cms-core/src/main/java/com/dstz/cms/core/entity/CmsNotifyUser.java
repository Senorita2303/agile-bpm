package com.dstz.cms.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.dstz.base.entity.AbModel;

import java.util.Date;

/**
 * Announcement user associated objects and
 *
 */
@TableName("cms_notify_user")
public class CmsNotifyUser extends AbModel<CmsNotifyUser> {

    /**
     * Primary Key
     */
    @TableId(value = "id_", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * Announcement ID
     */
    @TableField("notify_id_")
    private String notifyId;

    /**
     * User ID
     */
    @TableField("user_id_")
    private String userId;

    /**
     * (Creation time)Reading time
     */
    @TableField(value = "create_time_", fill = FieldFill.INSERT)
    private Date createTime;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public CmsNotifyUser(String notifyId, String userId) {
        this.notifyId = notifyId;
        this.userId = userId;
    }
}
