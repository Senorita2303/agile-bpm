package com.dstz.sys.rest.controller;

import cn.hutool.core.util.NumberUtil;
import com.dstz.base.common.constats.NumberPool;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.common.constats.AbAppRestConstant;
import com.dstz.base.common.encrypt.EncryptUtil;
import com.dstz.base.common.exceptions.BusinessMessage;
import com.dstz.base.common.property.SysPropertyService;
import com.dstz.base.web.controller.AbCrudController;
import com.dstz.sys.api.constant.SysApiCodes;
import com.dstz.sys.core.entity.SysProperties;
import com.dstz.sys.core.manager.SysPropertiesManager;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;

/**
 * @Name SysPropertiesController
 * @description: System properties
 */
@RestController
@RequestMapping(AbAppRestConstant.SYS_SERVICE_PREFIX + "/properties")
public class SysPropertiesController extends AbCrudController<SysProperties> {

	private final SysPropertiesManager sysPropertiesManager;

	private final SysPropertyService sysPropertyService;

	public SysPropertiesController(SysPropertiesManager sysPropertiesManager, SysPropertyService sysPropertyService) {
		this.sysPropertiesManager = sysPropertiesManager;
		this.sysPropertyService = sysPropertyService;
	}

	/**
	 * Save system property information
	 *
	 * @param sysProperties
	 * @throws Exception void
	 * @throws
	 */
	@RequestMapping(value = "save", method = RequestMethod.POST)
	@Override
	public ApiResponse<String> save(@RequestBody SysProperties sysProperties) {
		Assert.isFalse(sysPropertiesManager.isExist(sysProperties), () -> new BusinessMessage(
				SysApiCodes.KEY_WORD_DUPLICATE.formatDefaultMessage("Alias" + sysProperties.getCode())));
		checkIsDemoEnvironment();

		if (sysProperties.getEncrypt() != null && sysProperties.getEncrypt() == 1) {
			sysProperties.setValue(EncryptUtil.encrypt(sysProperties.getValue()));
		}

		if (StrUtil.isEmpty(sysProperties.getId())) {
			//Add a new save and if it is an encrypted type, encrypt it
			if (sysProperties.getEncrypt() != null && sysProperties.getEncrypt() == 1) {
				sysProperties.setValue(EncryptUtil.encrypt(sysProperties.getValue()));
			}
			sysPropertiesManager.create(sysProperties);
			sysPropertiesManager.reloadProperty();
			return ApiResponse.success("Added system properties successfully");
		}

		sysPropertiesManager.update(sysProperties);
		sysPropertiesManager.reloadProperty();
		return ApiResponse.success("Update system properties successfully");

	}

	/**
	 * Get the value of a system property
	 *
	 * @param code
	 * @throws Exception void
	 * @throws
	 */
	@RequestMapping(value = "getByCode")
	public ApiResponse<Object> getByCode(@RequestParam("code") String code) {
		String result = sysPropertyService.getValByCode(code);
		return ApiResponse.success(result);
	}

	@RequestMapping(value = "get")
	@Override
	public ApiResponse<SysProperties> get(@RequestParam(name = "id") String id) {
		SysProperties entity = null;
		if (StringUtils.hasLength(id)) {
			entity = abBaseManager.getById(id);
			if (entity != null && entity.getEncrypt().equals(NumberPool.INTEGER_ONE)) {
				entity.setValue(null);
			}
		}
		return ApiResponse.success(entity);
	}

	@Override
	protected String getEntityDesc() {
		return "System properties";
	}
}
