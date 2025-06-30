package com.dstz.org.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class SaveRoleUsersDTO implements java.io.Serializable {

    /**
     * Role ID
     */
    @NotBlank(message = "Parameter cannot be empty!")
    private String roleId;

    @NotEmpty(message = "Please select a user!")
    private String[] userIds;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String[] getUserIds() {
        return userIds;
    }

    public void setUserIds(String[] userIds) {
        this.userIds = userIds;
    }
}
