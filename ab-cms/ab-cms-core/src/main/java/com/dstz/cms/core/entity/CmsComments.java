package com.dstz.cms.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.dstz.base.entity.AbModel;

import java.util.Date;

/**
 * <p>
 * Comment Form
 * </p>
 *
 */
@TableName("cms_comments")
public class CmsComments extends AbModel<CmsComments> {

    /**
     * Comment record primary key
     */
    @TableId(value = "id_", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * Announcement or news record primary key
     */
    @TableField("msg_id_")
    private String msgId;

    /**
     * 0:Announcement 1:News
     */
    @TableField("comment_type_")
    private int commentType;

    /**
     * Comments
     */
    @TableField("comment_content_")
    private String commentContent;

    /**
     * Commenter primary key
     */
    @TableField(value = "create_by_", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * Commentator's name
     */

    @TableField(value = "creator_", fill = FieldFill.INSERT)
    private String creator;

    /**
     * Comment published time
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

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public int getCommentType() {
        return commentType;
    }

    public void setCommentType(int commentType) {
        this.commentType = commentType;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
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
}
