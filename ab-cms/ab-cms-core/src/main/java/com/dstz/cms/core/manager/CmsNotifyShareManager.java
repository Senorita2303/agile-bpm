package com.dstz.cms.core.manager;

import com.dstz.base.common.identityconvert.SysIdentity;
import com.dstz.base.manager.AbBaseManager;
import com.dstz.cms.core.entity.CmsNotifyShare;
import com.dstz.cms.core.entity.dto.CmsNotifyDTO;

import java.util.List;

/**
 * <p>
 * Announcement Release Corresponding Department Table General Business Category
 * </p>
 *
 */
public interface CmsNotifyShareManager extends AbBaseManager<CmsNotifyShare> {

    /**
     * Save the association between organizations and announcements
     *
     * @param cmsNotifyDTO Announcement Object
     */
    void saveRelation(CmsNotifyDTO cmsNotifyDTO);

    /**
     * Delete the associated organization relationship by announcement ID
     *
     * @param cmsNotifyId Announcement ID
     */
    void deleteByNotifyId(String cmsNotifyId);

    /**
     * Get the announcement ID collection associated with the current user
     *
     */
    List<String> getNotifyListByCurrentUser();

    /**
     * Get the user ID set associated with the announcement
     *
     */
    List<SysIdentity> getUserListByNotifyId(String cmsNotifyId);

}
