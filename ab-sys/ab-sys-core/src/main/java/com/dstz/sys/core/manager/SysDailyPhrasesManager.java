package com.dstz.sys.core.manager;

import com.dstz.base.api.dto.PageListDTO;
import com.dstz.base.api.dto.QueryParamDTO;
import com.dstz.base.manager.AbBaseManager;
import com.dstz.sys.core.entity.SysDailyPhrases;

import java.util.List;

/**
 * <p>
 * Common user terms, general business category
 * </p>
 *
 */
public interface SysDailyPhrasesManager extends AbBaseManager<SysDailyPhrases> {

    /**
     * Pagination query
     *
     * @param paramDTO Query parameters
     */
    PageListDTO<SysDailyPhrases> listJson(QueryParamDTO paramDTO);

    /**
     * All common phrases enabled by the current user
     *
     */
     List<SysDailyPhrases> enableList();

    /**
     * Add or modify
     *
     * @param sysDailyPhrases Common phrase objects
     */
    void saveOrUpdate(SysDailyPhrases sysDailyPhrases);
}
