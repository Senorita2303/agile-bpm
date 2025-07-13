package com.dstz.cms.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dstz.base.entity.AbModel;

/**
 * <p>
 * Announcement release corresponding department table
 * </p>
 * 
 */
@TableName("cms_notify_share")
public class CmsNotifyShare extends AbModel<CmsNotifyShare> {

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
     * Department ID
     */
    @TableField("group_id_")
    private String groupId;

    /**
     * Department Name
     */
    @TableField("group_name_")
    private String groupName;

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

    public CmsNotifyShare(String notifyId, String groupId, String groupName) {
        this.notifyId = notifyId;
        this.groupId = groupId;
        this.groupName = groupName;
    }
}
