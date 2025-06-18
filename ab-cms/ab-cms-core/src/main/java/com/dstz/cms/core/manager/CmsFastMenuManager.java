package com.dstz.cms.core.manager;

import com.dstz.base.manager.AbBaseManager;
import com.dstz.cms.core.entity.CmsFastMenu;
import com.dstz.cms.core.entity.vo.CmsFastMenuVO;

import java.util.List;

/**
 * <p>
 * Quick Menu Management General Business Class
 * </p>
 *
 */
public interface CmsFastMenuManager extends AbBaseManager<CmsFastMenu> {

    /**
     * Get the current user's shortcut menu (common applications)
     * @param type Is it a mobile terminal?  Mobile: 1    PC: 0
     */
    List<CmsFastMenuVO> getCmsFastMenuVO(int type);

    /**
     * Clear the shortcut menu for the current user
     */
    void removeAll();

    /**
     * Add shortcut menus in batches
     *
     * @param resourceIdList Menu ID collection
     */
    void saveBatch(List<String> resourceIdList);

    /**
     * Added shortcut menu
     *
     * @param id Menu ID collection
     */
    String saveOne(String id);
}
