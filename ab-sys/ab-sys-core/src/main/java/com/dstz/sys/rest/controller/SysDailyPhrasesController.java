package com.dstz.sys.rest.controller;


import static com.dstz.base.api.vo.ApiResponse.success;
import static com.dstz.base.common.constats.AbAppRestConstant.SYS_SERVICE_PREFIX;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dstz.base.api.dto.QueryParamDTO;
import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.api.vo.PageListVO;
import com.dstz.base.common.utils.UserContextUtils;
import com.dstz.base.query.ConditionType;
import com.dstz.base.query.impl.DefaultAbQueryFilter;
import com.dstz.base.web.controller.AbCrudController;
import com.dstz.sys.core.entity.SysDailyPhrases;
import com.dstz.sys.core.manager.SysDailyPhrasesManager;

/**
 * <p>
 * Common user terms front-end controller
 * </p>
 *
 */
@RestController
@RequestMapping(SYS_SERVICE_PREFIX + "/sysDailyPhrases")
public class SysDailyPhrasesController extends AbCrudController<SysDailyPhrases> {

    @Autowired
    private SysDailyPhrasesManager sysDailyPhrasesManager;

    @Override
    protected String getEntityDesc() {
        return "Common user phrases";
    }

    /**
     * Pagination query
     *
     * @param queryParamDto Query object
     * @return PageListDTO List of announcements displayed
     */
    @RequestMapping("listJson")
    @Override
    public ApiResponse<PageListVO<SysDailyPhrases>> listJson(@Valid @RequestBody QueryParamDTO queryParamDto) {
        return success(sysDailyPhrasesManager.listJson(queryParamDto));
    }

    /**
     * Save or modify entity data
     *
     * @param sysDailyPhrases Common phrase objects
     * @return Interface response object
     */
    @PostMapping("saveOrUpdate")
    public ApiResponse<String> saveOrUpdate(@RequestBody SysDailyPhrases sysDailyPhrases) {
        return success(() -> sysDailyPhrasesManager.saveOrUpdate(sysDailyPhrases));
    }



    /**
     * Enable or disable
     *
     * @param sysDailyPhrases Common phrase objects
     */
    @PostMapping("updateEnable")
    public ApiResponse<String> updateEnable(@RequestBody SysDailyPhrases sysDailyPhrases) {
        return success(() -> sysDailyPhrasesManager.update(null,Wrappers.lambdaUpdate(SysDailyPhrases.class)
                .eq(SysDailyPhrases::getId,sysDailyPhrases.getId())
                .set(SysDailyPhrases::getEnable,sysDailyPhrases.getEnable())));
    }

    /**
     * Get all common phrases for the current user (filter enabled)
     *
     * @return List<SysDailyPhrases> Common phrases collection
     */
    @GetMapping("list")
    public ApiResponse<List<SysDailyPhrases>> list() {
        return success(sysDailyPhrasesManager.enableList());
    }

}
