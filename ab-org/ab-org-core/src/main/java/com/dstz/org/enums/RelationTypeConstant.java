package com.dstz.org.enums;

import com.dstz.org.api.enums.GroupType;

/**
 * Organizational Level
 *
 */
public enum RelationTypeConstant {

    /**
     * Users and Groups
     */
    GROUP_USER("groupUser", "Users and Groups"),

    /**
     * position
     */
    POST("groupRole", "position"),

    /**
     * Users and roles
     */
    USER_ROLE("userRole", "Users and roles"),

    /**
     * Position user
     */
    POST_USER("groupUserRole", "Position user");

    private final String key;
    private final String label;

    RelationTypeConstant(String key, String label) {
        this.key = key;
        this.label = label;
    }

    public String label() {
        return label;
    }

    public String getLabel() {
        return label;
    }

    public String getKey() {
        return key;
    }


    /**
     * 通过组类型转换成与用户的关系类型
     *
     * @param groupType 组类型
     * @return 关联类型
     */
    public static RelationTypeConstant getUserRelationTypeByGroupType(String groupType) {
        switch (GroupType.fromType(groupType)) {
            case ORG:
                return RelationTypeConstant.GROUP_USER;
            case POST:
                return RelationTypeConstant.POST_USER;
            case ROLE:
                return RelationTypeConstant.USER_ROLE;
            default:
                return null;
        }
    }

}
