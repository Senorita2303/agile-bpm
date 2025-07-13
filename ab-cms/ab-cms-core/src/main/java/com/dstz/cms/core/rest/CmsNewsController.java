package com.dstz.cms.core.rest;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dstz.base.api.dto.PageListDTO;
import com.dstz.base.api.dto.QueryParamDTO;
import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.api.vo.PageListVO;
import com.dstz.base.web.controller.AbCrudController;
import com.dstz.cms.core.entity.CmsNews;
import com.dstz.cms.core.entity.dto.CmsNewsDTO;
import com.dstz.cms.core.manager.CmsNewsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.dstz.base.api.vo.ApiResponse.success;
import static com.dstz.base.common.constats.AbAppRestConstant.CMS_SERVICE_PREFIX;

/**
 * News Management Front-end Control Layer
 *
 */
@RestController
@RequestMapping(CMS_SERVICE_PREFIX + "/cmsNews")
public class CmsNewsController extends AbCrudController<CmsNews> {

    @Autowired
    private CmsNewsManager cmsNewsManager;

    @Override
    protected String getEntityDesc() {
        return "News Management";
    }

    /**
     * Paginated List
     */
    @Override
    @PostMapping("listJson")
    public ApiResponse<PageListVO<CmsNews>> listJson(@Valid @RequestBody QueryParamDTO queryParamDto) {
        PageListDTO<CmsNews> pageList = cmsNewsManager.listJson(queryParamDto);
        return ApiResponse.success(pageList);
    }

    /**
     * Get two fixed, streamlined news on the homepage (excluding attachments and other useless fields)
     */
    @PostMapping("getNewsPage")
    public ApiResponse<PageListVO<CmsNews>> getNewsPage(@RequestBody QueryParamDTO queryParamDto) {
        return success(cmsNewsManager.getNewsPage(queryParamDto));
    }

    /**
     * Read the specified news
     *
     * @param id News ID

     */
    @GetMapping("read")
    public ApiResponse<String> read(@RequestParam String id) {
        return success(() -> cmsNewsManager.update(null, Wrappers.lambdaUpdate(CmsNews.class)
                .eq(CmsNews::getId, id)
                .setSql("visit_num_ = visit_num_ + 1")));
    }

    /**
     * Query the news content of the specified ID
     *
     * @param id News ID
     * @return ApiResponse General return object, including the displayed news details
     */
    @GetMapping("getOne")
    public ApiResponse<CmsNewsDTO> getById(@RequestParam String id) {
        return success(cmsNewsManager.details(id));
    }

    /**
     * Add or modify news
     *
     * @param cmsNews News Object
     * @return Interface response object ApiResponse
     */
    @RequestMapping("saveOrUpdate")
    public ApiResponse<String> saveOrUpdate(@RequestBody CmsNews cmsNews) {
        return success(() -> cmsNewsManager.saveOrUpdate(cmsNews));
    }

    /**
     * Release News
     *
     * @param id News ID
     */
    @RequestMapping("releaseNews/{id}")
    public ApiResponse<String> releaseNews(@PathVariable String id) {
        return success(() -> cmsNewsManager.releaseNews(id));
    }

    /**
     * Removed news
     *
     * @param id News ID
     */
    @RequestMapping("withdrawNews/{id}")
    public ApiResponse<String> withdrawNews(@PathVariable String id) {
        return success(() -> cmsNewsManager.withdrawNews(id));
    }

}
