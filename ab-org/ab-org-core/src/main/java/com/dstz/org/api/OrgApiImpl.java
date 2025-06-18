package com.dstz.org.api;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dstz.base.common.constats.StrPool;
import com.dstz.org.api.model.IGroup;
import com.dstz.org.core.entity.Group;
import com.dstz.org.core.mapper.GroupMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Organization-related interface implementation
 *
 */
@Service("orgApi")
public class OrgApiImpl implements OrgApi {

	private final GroupMapper groupMapper;

	public OrgApiImpl(GroupMapper groupMapper) {
		this.groupMapper = groupMapper;
	}

	@Override
	public IGroup getByOrgIdAndGrade(String orgId, String grade) {
		Group group = groupMapper.selectById(orgId);
		if (group == null) {
			return null;
		}
		if (StrUtil.equals(grade, group.getType())) {
			return new AbOrg(group);
		}
		// Search up a level
		for (Group parent : groupMapper.selectBatchIds(StrUtil.split(group.getPath(), StrPool.DOT))) {
			if (StrUtil.equals(grade, parent.getType())) {
				return new AbOrg(parent);
			}
		}
		return null;
	}

	@Override
	public List<IGroup> getHierarchyByOrgId(String orgId) {
		LambdaQueryWrapper<Group> queryWrapper = Wrappers.lambdaQuery(Group.class).like(Group::getPath, orgId);
		return groupMapper.selectList(queryWrapper).stream().map(AbOrg::new).collect(Collectors.toList());
	}

	@Override
	public List<IGroup> getHierarchyByOrgIdAndGrade(String orgId, String grade) {
		IGroup group = getByOrgIdAndGrade(orgId, grade);
		if (group == null) {
			return new ArrayList<>();
		}
		List<IGroup> groupList = new ArrayList<>();
		groupList.add(group);
		Optional.ofNullable(getHierarchyByOrgId(group.getGroupId())).filter(CollUtil::isNotEmpty).ifPresent(groupList::addAll);
		return groupList;
	}
}
