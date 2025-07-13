package com.dstz.org.core.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.StrUtil;
import com.dstz.base.api.dto.PageListDTO;
import com.dstz.base.query.AbQueryFilter;
import com.dstz.org.core.entity.Role;
import com.dstz.org.core.manager.OrgPostManager;
import com.dstz.org.core.mapper.OrgRelationMapper;
import com.dstz.org.vo.OrgPostVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Universal organization-position universal business processing implementation
 *
 */
@Service("orgPostManager")
public class OrgPostManagerImpl implements OrgPostManager {

	private final OrgRelationMapper orgRelationMapper;

	public OrgPostManagerImpl(OrgRelationMapper orgRelationMapper) {
		this.orgRelationMapper = orgRelationMapper;
	}

	@Override
	public List<Role> getByIds(Collection<String> ids) {
		if (CollUtil.isEmpty(ids)) {
			return new ArrayList<>();
		}
		final int hashsetSize = (int) (ids.size() / MapUtil.DEFAULT_LOAD_FACTOR) + 1;
		Set<String> groupIds = new LinkedHashSet<>(hashsetSize);
		Set<String> roleIds = new LinkedHashSet<>(hashsetSize);

		for (String id : ids) {
			int dashedIndex = StrUtil.indexOf(id, CharPool.UNDERLINE);
			if (dashedIndex == StrUtil.INDEX_NOT_FOUND) {
				throw new IllegalArgumentException(String.format("岗位ID格式不正确, id:%s", id));
			}
			groupIds.add(id.substring(0, dashedIndex));
			roleIds.add(id.substring(dashedIndex + 1));
		}
		return orgRelationMapper.getPostsByRoleIdsAndGroupIds(roleIds, groupIds).stream().map(OrgPostVO::toRole).collect(Collectors.toList());
	}

	@Override
	public PageListDTO<Role> queryPosts(AbQueryFilter queryFilter) {
		PageListDTO<OrgPostVO> orgPostPageList = orgRelationMapper.queryPosts(queryFilter);
		return new PageListDTO<>(orgPostPageList.getPageSize(), orgPostPageList.getPage(), orgPostPageList.getTotal(), CollUtil.map(orgPostPageList.getRows(), OrgPostVO::toRole, false));
	}
}
