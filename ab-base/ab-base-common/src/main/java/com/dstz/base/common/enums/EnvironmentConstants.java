package com.dstz.base.common.enums;

import cn.hutool.core.util.StrUtil;

public enum EnvironmentConstants {

    DEV("DEV", ",Development-Default"),
    SIT("SIT", "Test"),
    DEMO("DEMO", "Case"),
    UAT("UAT", "User Testing"),
    GRAY("GRAY", "Grayscale"),
    PROD("PROD", "Production");


    private final String key;
    private final String value;

    EnvironmentConstants(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static String getKes() {
        StringBuilder sb = new StringBuilder();
        for (EnvironmentConstants e : EnvironmentConstants.values()) {
            sb.append("[").append(e.key).append("]");
        }
        return sb.toString();
    }

    public static boolean contain(String key) {
        if (StrUtil.isEmpty(key)) {
            return false;
        }

        for (EnvironmentConstants e : EnvironmentConstants.values()) {
            if (key.equals(e.key)) {
                return true;
            }
        }
        return false;
    }
}
