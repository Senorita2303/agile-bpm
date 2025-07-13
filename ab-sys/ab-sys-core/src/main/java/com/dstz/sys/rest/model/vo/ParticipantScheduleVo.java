package com.dstz.sys.rest.model.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @Name ParticipantScheduleVo
 */
public class ParticipantScheduleVo implements Serializable {

    private static final long serialVersionUID = -6592562315091674625L;
    /**
     * ID
     */
    protected String id;
    /**
     * Business association id. This id is used in callbacks. 
     */
    protected String bizId;

    /**
     * Title
     */
    protected String title;

    /**
     * Description
     */
    protected String remark;

    /**
     * Task connection
     */
    protected String taskUrl;

    /**
     * Type
     */
    protected String type;

    /**
     * How to open the task
     */
    protected String openType;

    /**
     * Owner
     */
    protected String owner;

    /**
     * Owner
     */
    protected String ownerName;

    /**
     * Participants
     */
    protected String participantNames;

    /**
     * Start date
     */
    protected java.util.Date startTime;

    /**
     * End date
     */
    protected java.util.Date endTime;

    /**
     * Actual start date
     */
    protected java.util.Date actualStartTime;

    /**
     * Completion time
     */
    protected java.util.Date completeTime;

    /**
     * Progress
     */
    protected Integer rateProgress;

    /**
     * Submitted by
     */
    protected String submitter;

    /**
     * Submitted by
     */
    protected String submitterName;

    /**
     * isLock
     */
    protected String isLock;

    /**
     * Creation time
     */
    protected java.util.Date createTime;

    /**
     * Creator
     */
    protected String createBy;

    /**
     * Update time
     */
    protected java.util.Date updateTime;

    /**
     * Updater
     */
    protected String updateBy;

    /**
     * Delete Mark
     */
    protected String deleteFlag;


    /**
     * Schedule ID
     */
    protected String scheduleId;

    /**
     * Participant's name
     */
    protected String participantorName;

    /**
     * Participants
     */
    protected String participantor;


    /**
     * ilka submitted comments
     */
    protected String submitComment;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTaskUrl() {
        return taskUrl;
    }

    public void setTaskUrl(String taskUrl) {
        this.taskUrl = taskUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOpenType() {
        return openType;
    }

    public void setOpenType(String openType) {
        this.openType = openType;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getParticipantNames() {
        return participantNames;
    }

    public void setParticipantNames(String participantNames) {
        this.participantNames = participantNames;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(Date actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public Integer getRateProgress() {
        return rateProgress;
    }

    public void setRateProgress(Integer rateProgress) {
        this.rateProgress = rateProgress;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public String getSubmitterName() {
        return submitterName;
    }

    public void setSubmitterName(String submitterName) {
        this.submitterName = submitterName;
    }

    public String getIsLock() {
        return isLock;
    }

    public void setIsLock(String isLock) {
        this.isLock = isLock;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getParticipantorName() {
        return participantorName;
    }

    public void setParticipantorName(String participantorName) {
        this.participantorName = participantorName;
    }

    public String getParticipantor() {
        return participantor;
    }

    public void setParticipantor(String participantor) {
        this.participantor = participantor;
    }

    public String getSubmitComment() {
        return submitComment;
    }

    public void setSubmitComment(String submitComment) {
        this.submitComment = submitComment;
    }
}
