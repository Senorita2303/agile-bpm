package com.dstz.sys.rest.controller;


import com.dstz.base.common.constats.AbAppRestConstant;
import com.dstz.base.web.controller.AbCrudController;
import com.dstz.sys.core.entity.SysLogErr;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * System exception log front-end controller
 * </p>
 *
 */
@RestController
@RequestMapping(AbAppRestConstant.SYS_SERVICE_PREFIX + "/logErr")

public class SysLogErrController extends AbCrudController<SysLogErr> {

    @Override
    protected String getEntityDesc() {
        return "System exception log";
    }
}
