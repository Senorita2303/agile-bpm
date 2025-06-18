package com.dstz.org.api;

import cn.hutool.core.util.StrUtil;
import com.dstz.base.common.constats.StrPool;
import com.dstz.org.api.enums.GroupType;
import com.dstz.org.api.model.IGroup;
import com.dstz.org.core.entity.Group;
import com.dstz.org.core.entity.Role;
import com.dstz.org.vo.OrgRelationUserVO;

/**
 * Group Role/Position
 *
 */
class AbRole extends AbDelegate<Role> implements IGroup{

	private static final long serialVersionUID = 6636298359317208345L;

	private final GroupType groupType;

	private AbRole(Role role, GroupType groupType) {
		super(role);
		this.groupType = groupType;
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
		return groupType.getType();
	}

	@Override
	public String getGroupCode() {
		return delegate.getCode();
	}

	public Integer getEnabled(){
		return delegate.getEnabled();
	}

	public Integer getLevel() {
		return delegate.getLevel();
	}

	@Override
	public Integer getGroupLevel() {
		return delegate.getLevel();
	}

	public static AbRole newRole(Role role) {
		return new AbRole(role, GroupType.ROLE);
	}

	public static AbRole newPost(Role post) {
		return new AbRole(post, GroupType.POST);
	}

	public static AbRole newPost(OrgRelationUserVO orgRelationVO) {
		Role role = new Role();
		role.setId(String.format(StrPool.FORMATSTR, orgRelationVO.getGroupId(), orgRelationVO.getRoleId()));
		role.setName(StrUtil.join(StrPool.DASHED, orgRelationVO.getGroupName(), orgRelationVO.getRoleName()));
		return newPost(role);
	}

	public static AbRole newPost(Group group, Role role) {
		Role post = new Role();
		post.setId(String.format(StrPool.FORMATSTR, group.getId(), role.getId()));
		post.setName(StrUtil.join(StrPool.DASHED, group.getName(), role.getName()));
		return newPost(post);
	}
}
