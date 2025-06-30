package com.dstz.org.dto;

import javax.validation.constraints.NotBlank;

public class SaveRoleDTO implements java.io.Serializable {

    private String id;

    /**
     * Role Name
     */
    @NotBlank(message = "Name cannot be empty!")
    private String name;

    /**
     * code
     */
    @NotBlank(message = "The code cannot be empty!")
    private String code;

    /**
     * 0: Disable, 1: Enable
     */
    private Integer enabled;

    /**
     * Character Level
     */
    private Integer level;

    /**
     * describe
     */
    private String desc;

    /**
     * Classification dictionary code
     */
    @NotBlank(message = "Group cannot be empty!")
    private String typeCode;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    @Override
    public String toString() {
        return "SaveRoleDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", enabled=" + enabled +
                ", level=" + level +
                ", desc='" + desc + '\'' +
                ", typeCode='" + typeCode + '\'' +
                '}';
    }
}
