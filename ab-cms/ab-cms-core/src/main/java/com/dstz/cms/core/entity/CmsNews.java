package com.dstz.cms.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.dstz.base.entity.AbModel;

import java.util.Date;

/**
 * <p>
 * News Sheet
 * </p>
 *
 */
@TableName("cms_news")
public class CmsNews extends AbModel<CmsNews> {

    /**
     * Primary Key
     */
    @TableId(value = "id_", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * News Headlines
     */
    @TableField("title_")
    private String title;

    /**
     * News content
     */
    @TableField("content_")
    private String content;

    /**
     * Carousel image file information
     */
    @TableField("images_")
    private String images;

    /**
     * Attachment Information
     */
    @TableField("attachments_")
    private String attachments;

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
     * Release time
     */
    @TableField("release_time_")
    private Date releaseTime;

    /**
     * Publisher or remover
     */
    @TableField("release_by_")
    private String releaseBy;

    /**
     * Name of the publisher or remover
     */
    @TableField("release_name_")
    private String releaseName;

    /**
     * Release status 0 Not released 1 Released
     */
    @TableField("status_")
    private Integer status;

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
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public void setId(String id) {
        this.id = id;
    }


    public void releaseNews(String userId, String fullName) {
        this.releaseBy = userId;
        this.releaseName = fullName;
        this.status = 1;
        this.releaseTime = new Date();
    }

    public void withdrawNews(String userId, String fullName) {
        this.releaseBy = userId;
        this.releaseName = fullName;
        this.status = 2;
        this.releaseTime = new Date();
    }

    public CmsNews(String id, String title, String content, String images, String attachments, int commentsNum, int visitNum, Date releaseTime, String releaseBy, String releaseName, Integer status, Date createTime, String createBy, String createOrgId, Date updateTime, String updater, String updateBy) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.images = images;
        this.attachments = attachments;
        this.commentsNum = commentsNum;
        this.visitNum = visitNum;
        this.releaseTime = releaseTime;
        this.releaseBy = releaseBy;
        this.releaseName = releaseName;
        this.status = status;
        this.createTime = createTime;
        this.createBy = createBy;
        this.createOrgId = createOrgId;
        this.updateTime = updateTime;
        this.updater = updater;
        this.updateBy = updateBy;
    }

    public CmsNews() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public Integer getCommentsNum() {
        return commentsNum;
    }

    public void setCommentsNum(Integer commentsNum) {
        this.commentsNum = commentsNum;
    }

    public int getVisitNum() {
        return visitNum;
    }

    public void setVisitNum(int visitNum) {
        this.visitNum = visitNum;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getReleaseBy() {
        return releaseBy;
    }

    public void setReleaseBy(String releaseBy) {
        this.releaseBy = releaseBy;
    }

    public String getReleaseName() {
        return releaseName;
    }

    public void setReleaseName(String releaseName) {
        this.releaseName = releaseName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
}
