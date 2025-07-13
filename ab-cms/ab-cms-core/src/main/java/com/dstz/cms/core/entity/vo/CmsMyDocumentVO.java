package com.dstz.cms.core.entity.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * knowledge base	
 * </p>
 *
 */
public class CmsMyDocumentVO implements Serializable {

    /**
     * id
     */
    private String id;

    /**
     * Document Name
     */
    private String name;

    /**
     * Parent Directory ID
     */
    private String parentId;

    /**
     * Parent Directory Name
     */
    private String parentName;

    /**
     * Associated file name
     */
    private String fileType;

    /**
     * Inherited id (inherited = parentId, non-inherited = own id)
     */
    private String rightsId;

    /**
     * Number of reads
     */
    private Integer readNum;

    /**
     * Number of borrowers
     */
    private Integer borrowNum;

    /**
     * Permission name (avoid joint query and only display, the relationship is maintained in the permission table)
     */
    private String rightsName;

    /**
     * Creation time
     */
    private Date createTime;

    /**
     * From (Collection, My, Borrowing, Borrowed)
     */
    private String sources;

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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getRightsId() {
        return rightsId;
    }

    public void setRightsId(String rightsId) {
        this.rightsId = rightsId;
    }

    public Integer getReadNum() {
        return readNum;
    }

    public void setReadNum(Integer readNum) {
        this.readNum = readNum;
    }

    public Integer getBorrowNum() {
        return borrowNum;
    }

    public void setBorrowNum(Integer borrowNum) {
        this.borrowNum = borrowNum;
    }

    public String getRightsName() {
        return rightsName;
    }

    public void setRightsName(String rightsName) {
        this.rightsName = rightsName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSources() {
        return sources;
    }

    public void setSources(String sources) {
        this.sources = sources;
    }
}
