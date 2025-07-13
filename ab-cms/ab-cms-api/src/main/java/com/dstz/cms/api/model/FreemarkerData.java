package com.dstz.cms.api.model;

import com.dstz.base.common.utils.UserContextUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 * Replace the entity class of the data in the template
 *
 */

public class FreemarkerData {

    /**
     * Title (name of news/event/announcement/process)
     */
    private String subject;

    /**
     * Opinions expressed (need to be filled in for pending/rejected items in the process module)
     */
    private String submitOpinion;

    /**
     * Previous node name (required for process rejection)
     */
    private String submitTaskname;

    /**
     * Type (Chinese: News/Schedule/Announcement/Process)
     */
    private String type;

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~The following attributes can be ignored~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/


    /**
     * Sender (automatically filled, can be omitted)
     */
    private String senderName = UserContextUtils.getUser().get().getFullName();

    /**
     * Sending time (automatically filled, can be omitted)
     */
    private String sendTime = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getSubmitOpinion() {
        return submitOpinion;
    }

    public void setSubmitOpinion(String submitOpinion) {
        this.submitOpinion = submitOpinion;
    }

    public String getSubmitTaskname() {
        return submitTaskname;
    }

    public void setSubmitTaskname(String submitTaskname) {
        this.submitTaskname = submitTaskname;
    }

    /**
     * Used for quick process creation (process to-do, process rejection, submitTaskname attribute is required for rejection, to-do can be null)
     */
    public FreemarkerData(String subject, String submitOpinion, String submitTaskname) {
        this.subject = subject;
        this.submitOpinion = submitOpinion;
        this.submitTaskname = submitTaskname;
    }

    /**
     * Quickly create construction objects (for news, announcements, itineraries, and process end)
     */
    public FreemarkerData(String subject) {
        this.subject = subject;
    }

    public FreemarkerData() {
    }

}
