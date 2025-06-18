package com.dstz.cms.core.entity.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.dstz.base.common.valuemap.AbValueMap;
import com.dstz.base.common.valuemap.AbValueMapType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Home Components
 * </p>
 *
 */
public class CmsHomeVO implements Serializable {

    /**
     * ID
     */
    private String id;

    /**
     * name
     */
    private String name;

    /**
     * code
     */
    private String code;

    /**
     * Chart type: (built-in widget, chart module, iframe)
     */
    private String type;

    /**
     * Classification type: Data dictionary: (for purely custom classification filtering)
     */
    @AbValueMap(type = AbValueMapType.DICT, fixValue = "homeType", matchField = "code", attrMap = @AbValueMap.AttrMap(originName = "name"))
    private String typeCode;

    /**
     * Is it enabled (0 disable 1 enable)
     */
    private Integer enable;

    /**
     * Remark
     */
    private String remark;

    /**
     * Creator ID
     */
    private String createBy;

    /**
     * Creator Name
     */
    private String creator;


    /**
     * Creation time
     */
    private Date createTime;


    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
