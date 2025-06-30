package com.dstz.org.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class SaveGroupUserRelDTO implements java.io.Serializable {

    @NotBlank(message = "Parameter cannot be empty")
    private String groupId;

    private String[] roleIds;

    @NotEmpty(message = "Please select a user!")
    private String[] userIds;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String[] roleIds) {
        this.roleIds = roleIds;
    }

    public String[] getUserIds() {
        return userIds;
    }

    public void setUserIds(String[] userIds) {
        this.userIds = userIds;
    }
}
