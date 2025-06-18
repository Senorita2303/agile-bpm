package com.dstz.sys.rest.model.vo;

import com.dstz.base.api.model.Tree;

import java.io.Serializable;
import java.util.List;

/**
 * @description: VO class in the tree on the left side of the log module
 */
public class SysAuditLogTreeVO implements Tree, Serializable {

    /**
     * Unique ID
     */
    private String id;
    /**
     * Only used for list-to-tree filtering, no other meaning
     */
    private String code;
    /**
     * Display name
     */
    private String name;

    /**
     * Superior id
     */
    private String parentId;

    /**
     * Subclass collection
     */
    private List<SysAuditLogTreeVO> children;


    @Override
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

    @Override
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public List<SysAuditLogTreeVO> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List children) {
        this.children = children;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SysAuditLogTreeVO(String id, String name, String parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }
}
