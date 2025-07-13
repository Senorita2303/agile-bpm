package com.dstz.cms.core.entity.vo;

import com.dstz.cms.core.entity.CmsComments;
import com.dstz.cms.core.entity.CmsNotify;
import com.dstz.cms.core.entity.CmsNotifyShare;

import java.util.List;


public class CmsNotifyVO extends CmsNotify {

    /**
     * Read status (0: unread, 1: read, 2: expired)
     */
    protected Integer isRead;

    /**
     * Department information related to the announcement (for display purposes)
     */
    private List<CmsNotifyShare> cmsNotifyShareList;

    /**
     * Comment information related to the announcement (for display)
     */
    private List<CmsComments> cmsCommentsList;


    public CmsNotifyVO() {
    }

    public List<CmsComments> getCmsCommentsList() {
        return cmsCommentsList;
    }

    public void setCmsCommentsList(List<CmsComments> cmsCommentsList) {
        this.cmsCommentsList = cmsCommentsList;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public List<CmsNotifyShare> getCmsNotifyShareList() {
        return cmsNotifyShareList;
    }

    public void setCmsNotifyShareList(List<CmsNotifyShare> cmsNotifyShareList) {
        this.cmsNotifyShareList = cmsNotifyShareList;
    }

}
