package com.dstz.cms.core.mapper;

import com.dstz.base.api.dto.PageListDTO;
import com.dstz.base.mapper.AbBaseMapper;
import com.dstz.base.query.impl.DefaultAbQueryFilter;
import com.dstz.cms.core.entity.CmsNews;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * News Table Mapper Interface
 * </p>
 *
 */
@Mapper
public interface CmsNewsMapper extends AbBaseMapper<CmsNews> {

    /**
     * Pagination Query
     *
     * @param defaultAbQueryFilter Query conditions
     * @return Paging Data
     */
    PageListDTO<CmsNews> listJson(DefaultAbQueryFilter defaultAbQueryFilter);

    /**
     * Get streamlined news on the homepage (excluding attachments and other useless fields)
     *
     * @param defaultAbQueryFilter Query conditions
     * @return News Collection
     */
    PageListDTO<CmsNews> getNewsPage(DefaultAbQueryFilter defaultAbQueryFilter);

}
