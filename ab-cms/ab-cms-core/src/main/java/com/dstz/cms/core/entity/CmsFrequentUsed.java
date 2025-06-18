package com.dstz.cms.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dstz.base.entity.AbModel;

import java.io.Serializable;

/**
 * <p>
 * Common process management
 * </p>
 *
 */
@TableName("cms_frequent_used")
public class CmsFrequentUsed extends AbModel<CmsFrequentUsed> {

    /**
     * Common process ID
     */
    @TableId(value = "id_", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * User ID
     */
    @TableField("user_id_")
    private String userId;

    /**
     * Process ID
     */
    @TableField("def_id_")
    private String defId;

    public CmsFrequentUsed(String userId, String defId) {
        this.userId = userId;
        this.defId = defId;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getDefId() {
        return defId;
    }

    public void setDefId(String defId) {
        this.defId = defId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
