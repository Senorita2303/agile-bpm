package com.dstz.sys.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.dstz.base.entity.AbModel;

import java.util.Date;

/**
 * <p>
 * Common user phrases
 * </p>
 *
 */
@TableName("sys_daily_phrases")
public class SysDailyPhrases extends AbModel<SysDailyPhrases> {

    /**
     * Primary key id is not empty
     */
    @TableId(value = "id_", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * Common language text
     */
    @TableField("locution_")
    private String locution;

    /**
     * Is it enabled?
     */
    @TableField("enable_")
    private Integer enable;

    /**
     * Is it the default 0 Custom 1 Default
     */
    @TableField("is_default_")
    private Integer isDefault = 0;

    /**
     * Creator
     */
    @TableField(value = "create_by_", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * Creator Name
     */
    @TableField(value = "creator_", fill = FieldFill.INSERT)
    private String creator;

    /**
     * Creation time
     */
    @TableField(value = "create_time_", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * Modification time
     */
    @TableField(value = "update_time_", fill = FieldFill.UPDATE)
    private Date updateTime;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getLocution() {
        return locution;
    }

    public void setLocution(String locution) {
        this.locution = locution;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public String getCreateBy() {
        return createBy;
    }

    @Override
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
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
    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
