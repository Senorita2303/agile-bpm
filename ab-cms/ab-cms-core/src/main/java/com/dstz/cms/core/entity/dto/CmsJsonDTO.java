package com.dstz.cms.core.entity.dto;

import java.io.Serializable;

/**
 * CMD attachment property DTO object
 */
public class CmsJsonDTO implements Serializable {

    private String id;

    private String name;

    private String size;

    public CmsJsonDTO(String id, String name, String size) {
        this.id = id;
        this.name = name;
        this.size = size;
    }

    public CmsJsonDTO() {
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
