package com.dstz.cms.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.dstz.base.common.valuemap.AbValueMap;
import com.dstz.base.common.valuemap.AbValueMapType;
import com.dstz.base.entity.AbModel;

import java.util.Date;

/**
 * System Announcement Table
 *
 */
@TableName("cms_notify")
public class CmsNotify extends AbModel<CmsNotify> {

    /**
     * Primary Key
     */
    @TableId(value = "id_", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * Announcement Title
     */
    @TableField("title_")
    private String title;

    /**
     * Announcement content
     */
    @TableField("content_")
    private String content;

    /**
     * Announcement type code
     */
    @AbValueMap(type = AbValueMapType.DICT, fixValue = "gglx", matchField = "code", attrMap = @AbValueMap.AttrMap(originName = "name"))
    @TableField("type_id_")
    private String typeId;

    /**
     * Attachment JSON format string information
     */
    @TableField("attachments_")
    private String attachments;

    /**
     * The publisher or the person who removed the product is distinguished by status
     */
    @TableField("release_by_")
    private String releaseBy;

    /**
     * Publisher Name
     */
    @TableField("release_name_")
    private String releaseName;

    /**
     * Release time or delisting time is distinguished according to the status
     */
    @TableField("release_time_")
    private Date releaseTime;

    /**
     * Release status 0 Unreleased 1 Released 2 Removed
     */
    @TableField("status_")
    private Integer status;

    /**
     * Number of reviews
     */
    @TableField("comments_num_")
    private int commentsNum;

    /**
     * Number of visits
     */
    @TableField("visit_num_")
    private int visitNum;

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
     * Updater
     */
    @TableField(value = "updater_", fill = FieldFill.INSERT_UPDATE)
    private String updater;

    /**
     * Updater ID
     */
    @TableField(value = "update_by_", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    public CmsNotify(String id, String title, String content, String typeId, String attachments, String releaseBy, String releaseName, Date releaseTime, Integer status, int commentsNum, int visitNum, Date createTime, String createBy, String createOrgId, Date updateTime, String updater, String updateBy) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.typeId = typeId;
        this.attachments = attachments;
        this.releaseBy = releaseBy;
        this.releaseName = releaseName;
        this.releaseTime = releaseTime;
        this.status = status;
        this.commentsNum = commentsNum;
        this.visitNum = visitNum;
        this.createTime = createTime;
        this.createBy = createBy;
        this.createOrgId = createOrgId;
        this.updateTime = updateTime;
        this.updater = updater;
        this.updateBy = updateBy;
    }

    public CmsNotify() {
    }

    @Override
    public String getId() {
        return id;
    }

    public void releaseNotify(String userId, String fullName) {
        this.releaseBy = userId;
        this.releaseName = fullName;
        this.status = 1;
        this.releaseTime = new Date();
    }

    public void withdrawNotify(String userId, String fullName) {
        this.releaseBy = userId;
        this.releaseName = fullName;
        this.status = 2;
        this.releaseTime = new Date();
    }



    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getTypeId() {
        return typeId;
    }

    public String getAttachments() {
        return attachments;
    }

    public String getReleaseBy() {
        return releaseBy;
    }

    public String getReleaseName() {
        return releaseName;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public Integer getStatus() {
        return status;
    }

    public int getCommentsNum() {
        return commentsNum;
    }

    public int getVisitNum() {
        return visitNum;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public String getCreateBy() {
        return createBy;
    }

    public String getCreateOrgId() {
        return createOrgId;
    }

    @Override
    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public String getUpdater() {
        return updater;
    }

    @Override
    public String getUpdateBy() {
        return updateBy;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public void setReleaseBy(String releaseBy) {
        this.releaseBy = releaseBy;
    }

    public void setReleaseName(String releaseName) {
        this.releaseName = releaseName;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCommentsNum(Integer commentsNum) {
        this.commentsNum = commentsNum;
    }

    public void setVisitNum(int visitNum) {
        this.visitNum = visitNum;
    }

    public void setCommentsNum(int commentsNum) {
        this.commentsNum = commentsNum;
    }
    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public void setCreateOrgId(String createOrgId) {
        this.createOrgId = createOrgId;
    }

    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public void setUpdater(String updater) {
        this.updater = updater;
    }

    @Override
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
