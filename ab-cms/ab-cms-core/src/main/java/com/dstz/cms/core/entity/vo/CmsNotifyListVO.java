package com.dstz.cms.core.entity.vo;

import com.dstz.base.common.valuemap.AbValueMap;
import com.dstz.base.common.valuemap.AbValueMapType;

import java.io.Serializable;
import java.util.Date;


public class CmsNotifyListVO implements Serializable {

    /**
     * Primary Key
     */
    private String id;

    /**
     * Announcement Title
     */
    private String title;

    /**
     * Announcement type code
     */
    @AbValueMap(type = AbValueMapType.DICT, fixValue = "gglx", matchField = "code", attrMap = @AbValueMap.AttrMap(originName = "name"))
    private String typeId;

    /**
     * Read status (0: unread, 1: read, 2: expired)
     */
    private Integer isRead;

    /**
     * Number of reviews
     */
    private Integer commentsNum;

    /**
     * Number of visits
     */
    private Integer visitNum;

    /**
     * Release time or delisting time is distinguished according to the status
     */
    private Date releaseTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public Integer getCommentsNum() {
        return commentsNum;
    }

    public void setCommentsNum(Integer commentsNum) {
        this.commentsNum = commentsNum;
    }

    public Integer getVisitNum() {
        return visitNum;
    }

    public void setVisitNum(Integer visitNum) {
        this.visitNum = visitNum;
    }

}
