package com.dstz.sys.rest.model.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;

/**
 * @Name ElementIconGetDTO
 * @description: Get icon list
 */
public class ElementIconGetDTO implements Serializable {
    private static final long serialVersionUID = -295778810172188790L;
    private Integer pageSize = 10;
    private Integer pageNumber = 1;
    private String name;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
