package com.dstz.org.enums;

public enum OrgStatus {

    //    Disable
    DISABLE(0,"Disable","danger"),

    //   Enable
    ENABLE(1,"Enable", "success");

    private final Integer status;
    private final String desc;
    private String labelCss;

    OrgStatus(Integer status, String desc, String labelCss) {
        this.status = status;
        this.desc = desc;
        this.labelCss = labelCss;
    }

    public String getLabelCss() {
        return labelCss;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
