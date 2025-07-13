package com.dstz.demo.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.dstz.base.entity.AbModel;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Order Information
 * </p>
 *
 */
@TableName("biz_apply_order")
public class BizApplyOrder extends AbModel<BizApplyOrder> {

    private static final long serialVersionUID = 4701340819462582895L;

    /**
     * ID
     */
    @TableId(value = "id_", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * Order Number
     */
    @TableField("apply_no_")
    private String applyNo;

    /**
     * Channel Manager
     */
    @TableField("qdjl")
    private String qdjl;

    /**
     * Channel Manager ID
     */
    @TableField("qdjl_id_")
    private String qdjlId;

    /**
     * Organization ID
     */
    @TableField("org_id_")
    private String orgId;

    /**
     * Organization
     */
    @TableField("org_name_")
    private String orgName;

    /**
     * Creation time
     */
    @TableField(value = "create_time_", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * Remark
     */
    @TableField("remark")
    private String remark;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getApplyNo() {
        return applyNo;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }

    public String getQdjl() {
        return qdjl;
    }

    public void setQdjl(String qdjl) {
        this.qdjl = qdjl;
    }

    public String getQdjlId() {
        return qdjlId;
    }

    public void setQdjlId(String qdjlId) {
        this.qdjlId = qdjlId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
