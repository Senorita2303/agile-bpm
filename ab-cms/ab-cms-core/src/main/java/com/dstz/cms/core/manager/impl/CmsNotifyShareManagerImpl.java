package com.dstz.cms.core.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.IterUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dstz.base.common.identityconvert.SysIdentity;
import com.dstz.base.common.utils.UserContextUtils;
import com.dstz.base.manager.impl.AbBaseManagerImpl;
import com.dstz.cms.core.entity.CmsNotifyShare;
import com.dstz.cms.core.entity.dto.CmsNotifyDTO;
import com.dstz.cms.core.manager.CmsNotifyShareManager;
import com.dstz.org.api.GroupApi;
import com.dstz.org.api.UserApi;
import com.dstz.org.api.model.IGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.dstz.org.api.enums.GroupType.ORG;
import static java.util.stream.Collectors.toList;

/**
 * Announcement release corresponding department table General service implementation class
 *
 */
@Service("cmsNotifyShareManager")
public class CmsNotifyShareManagerImpl extends AbBaseManagerImpl<CmsNotifyShare> implements CmsNotifyShareManager {

	@Autowired
	private GroupApi groupApi;
	@Autowired
	private UserApi userApi;

	/**
	 * Batch save department relationships associated with announcements
	 *
	 * @param cmsNotifyDTO Announcement Object
	 */
	@Override
	public void saveRelation(CmsNotifyDTO cmsNotifyDTO) {
		List<String> groupIdList = cmsNotifyDTO.getGroupIdList();
		if (CollectionUtil.isEmpty(groupIdList)) {
			return;
		}
		for (String id : groupIdList) {
			String groupName = groupApi.getByGroupId(ORG.getType(), id).getGroupName();
			create(new CmsNotifyShare(cmsNotifyDTO.getId(), id, groupName));
		}
	}

	/**
	 * Delete the associated organization relationship by announcement ID
	 *
	 * @param notifyId Announcement ID
	 */
	@Override
	public void deleteByNotifyId(String notifyId) {
		remove(Wrappers.<CmsNotifyShare>lambdaQuery().eq(CmsNotifyShare::getNotifyId, notifyId));
	}

	/**
	 * Get the announcement ID collection associated with the current user
	 */
	@Override
	public List<String> getNotifyListByCurrentUser() {
		List<String> groupIdList = groupApi.getByUserId(UserContextUtils.getUserId()).stream().map(IGroup::getGroupId)
				.collect(toList());
		return selectByWrapper(Wrappers.<CmsNotifyShare>lambdaQuery().in(CmsNotifyShare::getGroupId, groupIdList))
				.stream().map(CmsNotifyShare::getNotifyId).distinct().collect(Collectors.toList());
	}

	/**
	 * Get the user ID set associated with the announcement
	 */
	@Override
	public List<SysIdentity> getUserListByNotifyId(String cmsNotifyId) {
		// Get the organization ID set associated with the announcement
		List<String> list = selectByWrapper(
				Wrappers.<CmsNotifyShare>lambdaQuery().eq(CmsNotifyShare::getNotifyId, cmsNotifyId)).stream()
				.map(CmsNotifyShare::getGroupId).collect(Collectors.toList());
		// Get all users under the organization ID set
		return Optional.ofNullable(userApi.getByGroupTypeAndGroupIds(ORG.getType(), list)).map(IterUtil::asIterable)
				.map(iter -> CollUtil.map(iter, SysIdentity::new, true)).orElseGet(Collections::emptyList);
	}

}
