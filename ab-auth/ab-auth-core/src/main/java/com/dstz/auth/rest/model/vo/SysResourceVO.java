package com.dstz.auth.rest.model.vo;

import com.dstz.auth.authentication.api.model.ISysResource;
import com.dstz.auth.core.entity.SysResource;

import java.io.Serializable;
import java.util.List;

public class SysResourceVO extends SysResource implements ISysResource, Serializable {
    private List<SysResource> children;

    private String parentName;

    protected boolean checked = false;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<SysResource> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List list) {
        this.children = children;
    }


    @Override
    public Boolean getHidden() {
        if (this.getEnable() == null) return false;

        return this.getEnable() == 0;
    }
}
