package com.dstz.sys.core.mapper;

import com.dstz.base.api.dto.PageListDTO;
import com.dstz.base.query.AbQueryFilter;
import com.dstz.sys.core.entity.SysDailyPhrases;
import com.dstz.base.mapper.AbBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * User common language Mapper interface
 * </p>
 *
 */
@Mapper
public interface SysDailyPhrasesMapper extends AbBaseMapper<SysDailyPhrases> {

    /**
     * Pagination interface for common phrases (system built-in common phrases or your own common phrases)
     */
    PageListDTO<SysDailyPhrases> listJson(AbQueryFilter filter);
}
