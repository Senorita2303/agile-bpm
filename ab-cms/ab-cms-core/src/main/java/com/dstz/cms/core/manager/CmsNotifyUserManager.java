package com.dstz.cms.core.manager;

import com.dstz.base.manager.AbBaseManager;
import com.dstz.cms.core.entity.CmsNotifyUser;

import java.util.List;

/**
 * <p>
 * Announcement User Association Table General Business Class
 * </p>
 *
 */
public interface CmsNotifyUserManager extends AbBaseManager<CmsNotifyUser> {
    /**
     * @param notifyId Announcement ID
     * @param userId   User ID
     * @return Details of the object that reads information, etc.
     */
    CmsNotifyUser getNotifyUser(String notifyId, String userId);

    /**
     * Get the set of announcements that the current user has read
     *
     * @return The collection of announcements that the current user has read
     */
    List<String> getNotifyListByCurrentUser();

    /**
     * Delete announcement-related data
     */
    void deleteByNotifyId(String notifyId);

}
