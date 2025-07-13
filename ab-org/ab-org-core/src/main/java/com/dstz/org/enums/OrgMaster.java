package com.dstz.org.enums;

public enum OrgMaster {

    //    Disable
    DEFAULT (0,"No","danger"),

    //   Enable
    MASTER(1,"Yes","success");



    private final Integer master;
    private final String desc;
    private String labelCss;

    OrgMaster(Integer master, String desc, String labelCss) {
        this.master = master;
        this.desc = desc;
        this.labelCss = labelCss;
    }

    public Integer getMaster() {
        return master;
    }

    public String getDesc() {
        return desc;
    }

    public String getLabelCss() {
        return labelCss;
    }

}
