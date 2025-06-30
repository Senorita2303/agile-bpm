package com.dstz.cms.core.rest;


import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.web.controller.AbCrudController;
import com.dstz.cms.core.entity.CmsFastMenu;
import com.dstz.cms.core.entity.vo.CmsFastMenuVO;
import com.dstz.cms.core.manager.CmsFastMenuManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.dstz.base.api.vo.ApiResponse.success;
import static com.dstz.base.common.constats.AbAppRestConstant.CMS_SERVICE_PREFIX;

/**
 * Shortcut menu management front controller
 *
 */
@RestController
@RequestMapping(CMS_SERVICE_PREFIX + "/cmsFastMenu")
public class CmsFastMenuController extends AbCrudController<CmsFastMenu> {

    @Autowired
    private CmsFastMenuManager cmsFastMenuManager;

    @Override
    protected String getEntityDesc() {
        return "Quick menu management";
    }

    /**
     * Get the shortcut menu associated with the current user
     *
     * @return List<CmsFastMenu> Shortcut menu collection
     */
    @GetMapping("fastMenuList")
    public ApiResponse<List<CmsFastMenuVO>> listByUser() {
        return success(cmsFastMenuManager.getCmsFastMenuVO(0));
    }

    /**
     * Get the mobile shortcut menu associated with the current user
     *
     * @return List<CmsFastMenu> Shortcut menu collection
     */
    @GetMapping("fastMenuMobileList")
    public ApiResponse<List<CmsFastMenuVO>> mobileListByUser() {
        return success(cmsFastMenuManager.getCmsFastMenuVO(1));
    }

    /**
     * Added shortcut menu
     *
     * @param id Menu ID
     */
    @GetMapping("saveOne")
    public ApiResponse<String> saveOne(@RequestParam String id) {
        return success(cmsFastMenuManager.saveOne(id));
    }

    /**
     * Add shortcut menus in batches
     *
     * @param resourceIdList Menu ID collection
     */
    @PostMapping("saveBatch")
    public ApiResponse<String> saveBatch(@RequestBody List<String> resourceIdList) {
        return success(() -> cmsFastMenuManager.saveBatch(resourceIdList));
    }

    /**
     * Clear the shortcut menu for the current user
     */
    @RequestMapping("removeAll")
    public ApiResponse<String> removeAll() {
        return success(() -> cmsFastMenuManager.removeAll());
    }

}
