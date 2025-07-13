package com.dstz.org.api.enums;

import java.util.Arrays;
import java.util.Optional;

/**
 * Group Type
 *
 */
public enum GroupType {

    /**
     * organization
     */
    ORG("org", "Organization"),

    /**
     * role
     */
    ROLE("role", "Role"),

    /**
     * position
     */
    POST("post", "Position");

    /**
     * type
     */
    private final String type;

    /**
     * describe
     */
    private final String desc;

    GroupType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * Matches type. If type does not match, returns null.
     *
     * @param type type
     * @return Group Type
     */
    public static GroupType fromTypeIfNull(String type) {
        Optional<GroupType> groupType = Arrays.stream(values()).filter(item -> item.getType().equalsIgnoreCase(type)).findFirst();
        return groupType.orElse(null);
    }

    /**
     * Matches type, if type throws an exception
     *
     * @param type type
     * @return Group Type
     */
    public static GroupType fromType(String type) throws IllegalArgumentException {
        GroupType groupType = fromTypeIfNull(type);
        if (groupType == null) {
            throw new IllegalArgumentException(String.format("%s undefined", type));
        }
        return groupType;
    }
}
