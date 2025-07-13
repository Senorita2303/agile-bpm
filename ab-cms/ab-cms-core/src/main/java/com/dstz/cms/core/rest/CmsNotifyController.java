package com.dstz.cms.core.rest;


import com.dstz.base.api.dto.QueryParamDTO;
import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.api.vo.PageListVO;
import com.dstz.base.web.controller.AbCrudController;
import com.dstz.cms.core.entity.CmsNotify;
import com.dstz.cms.core.entity.dto.CmsNotifyDTO;
import com.dstz.cms.core.entity.vo.CmsNotifyListVO;
import com.dstz.cms.core.entity.vo.CmsNotifyVO;
import com.dstz.cms.core.manager.CmsNotifyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.dstz.base.api.vo.ApiResponse.success;
import static com.dstz.base.common.constats.AbAppRestConstant.CMS_SERVICE_PREFIX;

/**
 * System Announcement Table Front-end Controller
 *
 */
@RestController
@RequestMapping(CMS_SERVICE_PREFIX + "/cmsNotify")
public class CmsNotifyController extends AbCrudController<CmsNotify> {

    @Autowired
    private CmsNotifyManager cmsNotifyManager;

    @Override
    protected String getEntityDesc() {
        return "System announcement module";
    }

    /**
     * Pagination Query
     *
     * @param queryParamDto Query Object
     * @return PageListDTO List of displayed announcements
     */
    @RequestMapping("listJson")
    @Override
    public ApiResponse<PageListVO<CmsNotifyVO>> listJson(@Valid @RequestBody QueryParamDTO queryParamDto) {
        return success(cmsNotifyManager.page(queryParamDto));
    }

    /**
     * Query the announcement content of the specified ID
     *
     * @param id Announcement ID
     * @return ApiResponse General return object, including the displayed announcement details
     */
    @GetMapping("getOne")
    public ApiResponse<CmsNotifyVO> getById(@RequestParam("id") String id) {
        return success(cmsNotifyManager.details(id));
    }

    /**
     * View the list of announcements to which you belong (filter the organizations associated with the announcement)
     */
    @RequestMapping("getNotifyPage")
    public ApiResponse<PageListVO<CmsNotifyListVO>> getNotifyPage(@RequestBody QueryParamDTO queryParamDto) {
        return success(cmsNotifyManager.getNotifyPage(queryParamDto));
    }

    /**
     * Create announcement
     *
     * @param cmsNotifyDTO New announcement DTO object
     */
    @RequestMapping("saveDto")
    public ApiResponse<String> saveDto(@RequestBody CmsNotifyDTO cmsNotifyDTO) {
        return success(() -> cmsNotifyManager.save(cmsNotifyDTO));
    }

    /**
     * Modification Announcement
     *
     * @param cmsNotifyDTO Modified announcement DTO object
     */
    @RequestMapping("updateDto")
    public ApiResponse<String> updateDto(@RequestBody CmsNotifyDTO cmsNotifyDTO) {
        return success(() -> cmsNotifyManager.update(cmsNotifyDTO));
    }


    /**
     * Query the number of unread announcements for a user
     */
    @RequestMapping("queryUnReadCount")
    public ApiResponse<Integer> queryUnReadCount() {
        return success(cmsNotifyManager.queryUnReadCount());
    }

    /**
     * Release announcement
     *
     * @param id Announcement ID
     */
    @RequestMapping("releaseNotify/{id}")
    public ApiResponse<String> releaseNotify(@PathVariable String id) {
        return success(() -> cmsNotifyManager.releaseNotify(id));
    }

    /**
     * Delisting Notice
     *
     * @param id Announcement ID
     */
    @RequestMapping("withdrawNotify/{id}")
    public ApiResponse<String> withdrawNotify(@PathVariable String id) {
        return success(() -> cmsNotifyManager.withdrawNotify(id));
    }

}
