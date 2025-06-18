package com.dstz.cms.core.manager.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dstz.base.common.utils.UserContextUtils;
import com.dstz.base.manager.impl.AbBaseManagerImpl;
import com.dstz.cms.core.entity.CmsNotifyUser;
import com.dstz.cms.core.manager.CmsNotifyUserManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Announcement user association table General service implementation class
 *
 */
@Service("cmsNotifyUserManager")
public class CmsNotifyUserManagerImpl extends AbBaseManagerImpl<CmsNotifyUser> implements CmsNotifyUserManager {

    /**
     * Get the association details of the specified announcement ID and the specified user ID
     *
     * @param notifyId Announcement ID
     * @param userId   User ID
     * @return CmsNotifyUser Objects associated with whether the announcement has been read and the time it was read
     */
    @Override
    public CmsNotifyUser getNotifyUser(String notifyId, String userId) {
        return getBaseMapper().selectOne(Wrappers.<CmsNotifyUser>lambdaQuery()
                .eq(CmsNotifyUser::getNotifyId, notifyId)
                .eq(CmsNotifyUser::getUserId, userId));
    }

    /**
     * Get the set of announcements that the current user has read
     *
     * @return The collection of announcements that the current user has read
     */
    @Override
    public List<String> getNotifyListByCurrentUser() {
        return selectByWrapper(Wrappers.<CmsNotifyUser>lambdaQuery()
                .eq(CmsNotifyUser::getUserId, UserContextUtils.getUserId())).stream()
                .map(CmsNotifyUser::getNotifyId).collect(Collectors.toList());
    }

    /**
     * Delete announcement-related data
     */
    @Override
    public void deleteByNotifyId(String notifyId) {
        remove(Wrappers.<CmsNotifyUser>lambdaQuery().eq(CmsNotifyUser::getNotifyId, notifyId));
    }

}
