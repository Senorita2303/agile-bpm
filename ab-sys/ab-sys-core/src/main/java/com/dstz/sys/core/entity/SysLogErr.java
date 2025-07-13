package com.dstz.sys.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.dstz.base.entity.AbModel;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * System exception log
 * </p>
 *
 */
@TableName("sys_log_err")
public class SysLogErr extends AbModel<SysLogErr> {

    /**
     * ID
     */
    @TableId(value = "id_", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * Account
     */
    @TableField("account_")
    private String account;

    /**
     * Account
     */
    @TableField("full_name_")
    private String fullName;

    /**
     * IP source
     */
    @TableField("ip_")
    private String ip;

    /**
     * IP address
     */
    @TableField("ip_address_")
    private String ipAddress;

    /**
     * Status: unchecked, checked, fixed
     */
    @TableField("status_")
    private String status = "unchecked";

    /**
     * Wrong URL
     */
    @TableField("url_")
    private String url;

    /**
     * Error message
     */
    @TableField("content_")
    private String content;

    /**
     * Request Head
     */
    @TableField("heads_")
    private String heads;

    /**
     * Request parameters
     */
    @TableField("request_param_")
    private String requestParam;

    /**
     * Error time
     */
    @TableField(value = "create_time_", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * Error exception stack
     */
    @TableField("stack_trace_")
    private String stackTrace;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHeads() {
        return heads;
    }

    public void setHeads(String heads) {
        this.heads = heads;
    }

    public String getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    @Override
    public Serializable pkVal() {
        return this.id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
