package com.dstz.auth.rest.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.dstz.auth.authentication.api.model.ISysResource;
import com.dstz.auth.core.entity.SysResource;

import java.io.Serializable;
import java.util.List;

public class SysResourceTreeVO extends SysResource implements ISysResource, Serializable {


    /**
     * Is it assigned to a role?
     */
    @TableField(exist = false)
    protected boolean checked = false;


    private List<SysResource> children;

    @Override
    public List<SysResource> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List children) {
        this.children = children;
    }


    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }


    @Override
    public String getId() {
        return super.getId();
    }

    @Override
    public String getAppId() {
        return super.getAppId();
    }


    @Override
    public String getCode() {
        return super.getCode();
    }

    @Override
    public String getPath() {
        return super.getPath();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public String getUrl() {
        return super.getUrl();
    }

    @Override
    public Integer getEnable() {
        return super.getEnable();
    }

    @Override
    public Integer getOpened() {
        return super.getOpened();
    }

    @Override
    public Integer getIsApi() {
        return super.getIsApi();
    }

    @Override
    public String getIcon() {
        return super.getIcon();
    }

    @Override
    public String getType() {
        return super.getType();
    }

    @Override
    public Integer getSn() {
        return super.getSn();
    }

    @Override
    public String getParentId() {
        return super.getParentId();
    }


    @Override
    public Boolean getHidden() {
        if (this.getEnable() == null) return false;

        return this.getEnable() == 0;
    }

}
