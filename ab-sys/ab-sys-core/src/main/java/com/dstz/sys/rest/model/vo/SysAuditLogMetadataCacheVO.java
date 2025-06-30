package com.dstz.sys.rest.model.vo;

import java.io.Serializable;

/**
 * @Name SysAuditLogMetadataCacheVO
 */
public class SysAuditLogMetadataCacheVO implements Serializable {

    private static final long serialVersionUID = -1961365464945900536L;
    /**
     * Metadata primary key number
     */
    private String id;

    /**
     * Judgment condition expression
     */
    private String predicateExpr;


    /**
     * Business primary key acquisition expression
     */
    private String bizIdExpr;

    /**
     * Record data acquisition expression
     */
    private String dataExpr;

    /**
     * Log description template, supports SPEL expressions
     */
    private String descriptionTpl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPredicateExpr() {
        return predicateExpr;
    }

    public void setPredicateExpr(String predicateExpr) {
        this.predicateExpr = predicateExpr;
    }

    public String getBizIdExpr() {
        return bizIdExpr;
    }

    public void setBizIdExpr(String bizIdExpr) {
        this.bizIdExpr = bizIdExpr;
    }

    public String getDataExpr() {
        return dataExpr;
    }

    public void setDataExpr(String dataExpr) {
        this.dataExpr = dataExpr;
    }

    public String getDescriptionTpl() {
        return descriptionTpl;
    }

    public void setDescriptionTpl(String descriptionTpl) {
        this.descriptionTpl = descriptionTpl;
    }
}
