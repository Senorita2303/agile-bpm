package com.dstz.org.vo;


import com.dstz.base.common.valuemap.AbValueMap;
import com.dstz.base.common.valuemap.AbValueMapType;
import com.dstz.org.api.enums.GroupGradeConstant;

import java.util.List;

public class GroupVO implements java.io.Serializable {

    private String id;

    /**
     * name
     */
    private String name;

    /**
     * Parent ID
     */
    private String parentId;

    /**
     * Sort Order
     */
    private Integer sn;

    private String code;

    /**
     * Type: 0 Group, 1 Company, 3 Department
     */
    private String type;

    /**
     * describe
     */
    private String desc;

    /**
     * Organizational Path
     */
    private String path;

    /**
     * Organizational Path
     */
    private String pathName;

    /**
     * Name of parent organization
     */
    private String parentName;

    /**
     * Job Collection
     */
    private List<OrgRelationUserVO> orgRelationList;

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

    public Integer getSn() {
        return sn;
    }

    public void setSn(Integer sn) {
        this.sn = sn;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<OrgRelationUserVO> getOrgRelationList() {
        return orgRelationList;
    }

    public void setOrgRelationList(List<OrgRelationUserVO> orgRelationList) {
        this.orgRelationList = orgRelationList;
    }

}
