package com.dstz.cms.core.manager;

import com.dstz.base.api.dto.PageListDTO;
import com.dstz.base.api.dto.QueryParamDTO;
import com.dstz.base.manager.AbBaseManager;
import com.dstz.cms.core.entity.CmsNews;
import com.dstz.cms.core.entity.dto.CmsNewsDTO;

import java.util.List;

/**
 * <p>
 * News List General Business
 * </p>
 *
 */
public interface CmsNewsManager extends AbBaseManager<CmsNews> {
    /**
     * Get an entity by its ID
     *
     * @param id Entity ID
     * @return Physical Records
     */
    CmsNewsDTO details(String id);

    /**
     * Add or modify news
     *
     * @param cmsNews News Object
     */
    void saveOrUpdate(CmsNews cmsNews);

    /**
     * Release News
     *
     * @param id News ID
     */
    void releaseNews(String id);

    /**
     * Removed news
     *
     * @param id News ID
     */
    void withdrawNews(String id);

    /**
     * Paginated List
     */
    PageListDTO<CmsNews> listJson(QueryParamDTO queryParamDto);

    /**
     * Get two fixed, streamlined news on the homepage (excluding attachments and other useless fields)
     */
    PageListDTO<CmsNews> getNewsPage(QueryParamDTO queryParamDto);
}
