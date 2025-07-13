package com.dstz.org.api;

import cn.hutool.core.util.StrUtil;
import com.dstz.base.common.constats.NumberPool;
import com.dstz.base.common.constats.StrPool;
import com.dstz.org.api.enums.GroupType;
import com.dstz.org.api.model.IGroup;
import com.dstz.org.enums.RelationTypeConstant;
import com.dstz.org.vo.OrgPostVO;

/**
 * Associated Users
 *
 */
public class AbOrgRelation implements IGroup {

    /**
     * Association ID
     */
    private String id;

    private String name;

    /**
     * Associated Types {@link RelationTypeConstant}
     */
    private String type;

    /**
     * Group ID
     */
    private String groupId;

    /**
     * Group Name
     */
    private String groupName;


    /**
     * Character Name
     */
    private String roleName;


    /**
     * Role ID
     */
    private String roleId;

    public static AbOrgRelation newPost(OrgPostVO orgPostVO) {
        AbOrgRelation abOrgRelation = new AbOrgRelation();
        abOrgRelation.setId(String.format(StrPool.FORMATSTR, orgPostVO.getGroupId(), orgPostVO.getRoleId()));
        abOrgRelation.setName(StrUtil.join(StrPool.DASHED, orgPostVO.getGroupName(), orgPostVO.getRoleName()));
        abOrgRelation.setGroupId(orgPostVO.getGroupId());
        abOrgRelation.setGroupName(orgPostVO.getGroupName());
        abOrgRelation.setRoleId(orgPostVO.getRoleId());
        abOrgRelation.setRoleName(orgPostVO.getRoleName());
        abOrgRelation.setType(GroupType.POST.getType());
        return abOrgRelation;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String getGroupType() {
        return type;
    }

    @Override
    public String getGroupCode() {
        return null;
    }

    @Override
    public <T> T getAttrValue(String attrName, Class<T> tClass) {
        return null;
    }

    @Override
    public Integer getGroupLevel() {
        return NumberPool.INTEGER_ZERO;
    }
}
