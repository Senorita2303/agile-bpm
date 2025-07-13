package com.dstz.org.api;

import com.dstz.base.common.constats.NumberPool;
import com.dstz.org.api.enums.GroupType;
import com.dstz.org.api.model.IGroup;
import com.dstz.org.core.entity.Group;
import com.dstz.org.vo.GroupVO;

/**
 * Group Organization
 */
class AbOrg extends AbDelegate<Group> implements IGroup {

    private static final long serialVersionUID = -6454184965728216871L;

    public AbOrg(Group delegate) {
        super(delegate);
    }

    public static AbOrg of(GroupVO groupVO) {
        Group group = new Group();
        group.setId(groupVO.getId());
        group.setName(groupVO.getName());
        group.setParentId(groupVO.getParentId());
        group.setSn(groupVO.getSn());
        group.setCode(groupVO.getCode());
        group.setType(groupVO.getType());
        group.setDesc(groupVO.getDesc());
        group.setPath(groupVO.getPath());
        group.setPathName(groupVO.getPathName());
        return new AbOrg(group);
    }

    @Override
    public String getGroupId() {
        return delegate.getId();
    }

    @Override
    public String getGroupName() {
        return delegate.getName();
    }

    @Override
    public String getGroupType() {
        return GroupType.ORG.getType();
    }

    @Override
    public String getGroupCode() {
        return delegate.getCode();
    }

    @Override
    public String getParentId() {
        return delegate.getParentId();
    }

    public String getType() {
        return delegate.getType();
    }

    public String getPathName() {
        return delegate.getPathName();
    }

    @Override
    public Integer getGroupLevel() {
        if (delegate.getType() == null) {
            return NumberPool.INTEGER_ZERO;
        }
        return Integer.valueOf(delegate.getType());
    }
}
