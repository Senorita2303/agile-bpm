package com.dstz.auth.rest.model.dto;

import com.dstz.auth.core.entity.SysResource;

import java.util.List;

public class SysResourceDTO extends SysResource {
    private List<SysResourceDTO> children ;

    public List<SysResourceDTO> getChildren() {
        return children;
    }

    public void setChildren(List<SysResourceDTO> children) {
        this.children = children;
    }
}
