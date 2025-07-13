package com.dstz.cms.core.rest;


import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.web.controller.AbCrudController;
import com.dstz.cms.core.entity.CmsFrequentUsed;
import com.dstz.cms.core.manager.CmsFrequentUsedManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.dstz.base.api.vo.ApiResponse.success;
import static com.dstz.base.common.constats.AbAppRestConstant.CMS_SERVICE_PREFIX;

/**
 * <p>
 * Common process management front-end controller
 * </p>
 *
 */
@RestController
@RequestMapping(CMS_SERVICE_PREFIX + "/cmsFrequentUsed")
public class CmsFrequentUsedController extends AbCrudController<CmsFrequentUsed> {

    @Autowired
    private CmsFrequentUsedManager cmsFrequentUsedManager;

    @Override
    protected String getEntityDesc() {
        return "Common process management";
    }

    /**
     * Get all common processes of the current user
     *
     * @return List<CmsFrequentUsed> Common process collection
     */
    @RequestMapping("list")
    public ApiResponse<List<CmsFrequentUsed>> list() {
        return success(cmsFrequentUsedManager.list());
    }

    /**
     * Add common processes in batches
     *
     * @param defIdList Common process ID collection
     */
    @RequestMapping("saveBatch")
    public ApiResponse<String> saveBatch(@RequestBody List<String> defIdList) {
        return success(() -> cmsFrequentUsedManager.saveBatch(defIdList));
    }

}
