package com.dstz.base.common.enums;

public enum ErrorLogLeve {
    ERROR("error", "Mistake"),
    WARING("waring", "Warn"),
    ;

    private String leve;
    private String desc;

    ErrorLogLeve(String leve, String desc) {
        this.leve = leve;
        this.desc = desc;
    }

    public String getLeve() {
        return leve;
    }

    public String getDesc() {
        return desc;
    }
}
