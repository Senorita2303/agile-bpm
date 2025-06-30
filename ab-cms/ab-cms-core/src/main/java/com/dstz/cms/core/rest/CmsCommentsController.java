package com.dstz.cms.core.rest;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dstz.base.common.constats.AbAppRestConstant;
import com.dstz.base.web.controller.AbCrudController;
import com.dstz.cms.core.entity.CmsComments;

/**
 * Comment Management Front Controller
 *
 */
@RestController

@RequestMapping(AbAppRestConstant.CMS_SERVICE_PREFIX + "/cmsComments")
public class CmsCommentsController extends AbCrudController<CmsComments> {

    @Override
    protected String getEntityDesc() {
        return "Comment management";
    }

}
