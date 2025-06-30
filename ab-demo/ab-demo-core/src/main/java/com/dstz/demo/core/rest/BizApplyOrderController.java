package com.dstz.demo.core.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dstz.base.common.constats.AbAppRestConstant;
import com.dstz.base.web.controller.AbCrudController;
import com.dstz.demo.core.entity.BizApplyOrder;
import com.dstz.demo.core.manager.BizApplyOrderManager;

@RestController

@RequestMapping(AbAppRestConstant.DEMO_SERVICE_PREFIX + "/applyOrder")
public class BizApplyOrderController extends AbCrudController<BizApplyOrder> {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbCrudController.class);
	@Autowired
	private BizApplyOrderManager orderMananger;

	@Override
	protected String getEntityDesc() {
		return "demoController";
	}
}