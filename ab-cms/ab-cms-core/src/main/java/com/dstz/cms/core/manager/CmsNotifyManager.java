package com.dstz.cms.core.manager;

import com.dstz.base.api.dto.PageListDTO;
import com.dstz.base.api.dto.QueryParamDTO;
import com.dstz.base.manager.AbBaseManager;
import com.dstz.cms.core.entity.CmsNotify;
import com.dstz.cms.core.entity.dto.CmsNotifyDTO;
import com.dstz.cms.core.entity.vo.CmsNotifyListVO;
import com.dstz.cms.core.entity.vo.CmsNotifyVO;

import java.util.List;

/**
 * <p>
 * System Announcement Table General Business Category
 * </p>
 *
 */
public interface CmsNotifyManager extends AbBaseManager<CmsNotify> {

    /**
     * Pagination Query
     *
     * @param queryParamDto Query Object
     */
    PageListDTO<CmsNotifyVO> page(QueryParamDTO queryParamDto);

    /**
     * Query the announcement content of the specified ID
     *
     * @param id Announcement ID
     * @return CmsNotifyDTO Details of the announcement displayed
     */
    CmsNotifyVO details(String id);

    /**
     * Create announcement
     *
     * @param cmsNotifyDTO New announcement DTO object
     */
    void save(CmsNotifyDTO cmsNotifyDTO);

    /**
     * Modification Announcement
     *
     * @param cmsNotifyDTO Modified announcement DTO object
     */
    void update(CmsNotifyDTO cmsNotifyDTO);

    /**
     * View the list of announcements to which you belong (filter the organizations associated with the announcement)
     */
    PageListDTO<CmsNotifyListVO> getNotifyPage(QueryParamDTO queryParamDto);

    /**
     * Query the number of unread announcements for a user
     */
    int queryUnReadCount();

    /**
     * Release announcement
     *
     * @param id Announcement ID
     */
    void releaseNotify(String id);

    /**
     * Delisting Notice
     *
     * @param id Announcement ID
     */
    void withdrawNotify(String id);

}
