package com.dstz.sys.rest.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.common.constats.AbAppRestConstant;
import com.dstz.base.common.enums.ErrorLogLeve;
import com.dstz.base.common.enums.GlobalApiCodes;
import com.dstz.base.common.events.AbErrorLogEvent;
import com.dstz.base.common.exceptions.BusinessException;
import com.dstz.base.common.property.PropertyEnum;
import com.dstz.base.common.utils.AbFreemarkUtil;
import com.dstz.base.common.utils.ConstantUtil;
import com.dstz.base.common.utils.EnumUtil;
import com.dstz.base.common.utils.HanYuPinyinUtil;
import com.dstz.base.common.utils.JsonUtils;
import com.dstz.base.utils.AbDataSourceUtils;
import com.dstz.base.web.controller.AbBaseController;
import com.dstz.sys.api.SystemVariableApi;
import com.dstz.sys.rest.model.dto.AutoTranslateDTO;
import com.dstz.sys.rest.model.dto.ParseStrByFreeMarkerDTO;
import com.dstz.sys.sdk.BaiduFanYiClient;
import com.fasterxml.jackson.databind.JsonNode;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;

/**
 * @Name SysToolsController
 * @description: System tools
 */
@RestController
@RequestMapping(AbAppRestConstant.SYS_SERVICE_PREFIX + "/tools")
public class SysToolsController extends AbBaseController {
	private static Logger logger = LoggerFactory.getLogger(SysToolsController.class);

	@Autowired
	private SystemVariableApi systemVariableApi;

	/**
	 * <pre>
	 * Get the json form of the enumeration according to the path of an enumeration class for front-end use
	 * Note! ! If the enumeration is in the middle of a class, the path is as follows: com.dstz.base.db.model.Column$TYPE
	 * </pre>
	 *
	 * @param path
	 * @param listMode
	 * @param key
	 *            :In non-list mode, construct a map with the specified key in the enumeration
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getEnum")
	public ApiResponse getEnum(@RequestParam("path") String path, @RequestParam("listMode") boolean listMode, @RequestParam(name = "key", required = false) String key) throws Exception {
		if (listMode) {
			return ApiResponse.success(EnumUtil.toJSONArray(path));
		}
		JsonNode jsonNode = EnumUtil.toJSON(path);
		if (StrUtil.isEmpty(key)) {
			return ApiResponse.success(jsonNode);
		}
		Map<String, Object> map = new HashMap<>(jsonNode.size());
		JsonUtils.parseObject(jsonNode, Map.class).forEach((k, v) -> {
			Map m = (Map) v;
			map.put(m.get(key).toString(), v);
		});

		return ApiResponse.success(map);
	}

	/**
	 * <pre>
	 * Get the constant of key (field name) according to path (class path)
	 * ps: If key is empty, all static final static variables of path class will be obtained
	 * </pre>
	 *
	 * @param path
	 * @param key
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getConstant")
	public Object getConstant(@RequestParam("path") String path, @RequestParam("key") String key) throws Exception {
		if (StrUtil.isEmpty(key)) {
			return ConstantUtil.get(path);
		}
		return ConstantUtil.get(path, key);
	}

	@RequestMapping("getInterFaceImpls")
	public Object getInterFaceImpls(@RequestParam("classPath") String classPath) throws Exception {
		Class<?> clazz = Class.forName(classPath);
		Map<String, ?> map = SpringUtil.getBeansOfType(clazz);
		return map.values();
	}

	@RequestMapping("getInterFaceImplsResult")
	public ApiResponse<?> getInterFaceImplsResult(@RequestParam("classPath") String classPath) throws Exception {
		Class<?> clazz = Class.forName(classPath);
		Map<String, ?> map = SpringUtil.getBeansOfType(clazz);
		return ApiResponse.success(map.values());
	}

	/**
	 * @param chinese
	 * @param type
	 *            1: Full spelling 0: First letter of the pinyin
	 * @throws Exception
	 */
	@RequestMapping(value = "pinyin")
	public ApiResponse<?> pinyin(@RequestBody Map<String, Object> map) throws Exception {
		String chinese = (String) map.get("chinese");
		Integer type = (Integer) map.get("type");
		String result = "";
		if (type == 1) {// Full spelling
			result = HanYuPinyinUtil.getPinyinString(chinese);
		} else if (type == 2) {// Initial Pinyin
			result = HanYuPinyinUtil.getFirstLetters(chinese, HanyuPinyinCaseType.LOWERCASE);
		} else if (type == 3) {// Baidu Translate English
			try {
				result = BaiduFanYiClient.translateMulti(new AutoTranslateDTO(chinese, "en-US")).get(0).getDstText();
				if (result == null || "undefined".equals(result)) {
					throw new RuntimeException("Baidu Translate returned undefined");
				}
				result = StrUtil.join("", ReUtil.findAllGroup0("[a-zA-Z0-9_]+", result));
				result = StrUtil.toCamelCase(result, ' ');
			} catch (Exception e) {
				// Wrong use of initials Pinyin
				result = HanYuPinyinUtil.getFirstLetters(chinese, HanyuPinyinCaseType.LOWERCASE);
				SpringUtil.publishEvent(AbErrorLogEvent.createErrorLog(new BusinessException(GlobalApiCodes.BASE_COMMON_ERROR, e), ErrorLogLeve.ERROR));
			}
		}

		return ApiResponse.success(result);
	}

	/**
	 * Get the current date
	 *
	 * @param format
	 *            Formatting
	 * @return Response message
	 */
	@RequestMapping("getCurrentTime")
	public ApiResponse<String> getCurrentTime(@RequestParam(value = "format") String format) {
		return ApiResponse.success(DateUtil.format(new Date(), format));
	}

	@RequestMapping("getResultEnum")
	public Object getResultEnum(@RequestParam String path, @RequestParam(required = false, defaultValue = "false") Boolean listMode) throws Exception {
		if (listMode) {
			return ApiResponse.success(EnumUtil.toJSONArray(path));
		}

		return ApiResponse.success(EnumUtil.toJSON(path));
	}

	/**
	 * class present
	 *
	 * @param classes
	 *            class name
	 * @return is present
	 */
	@RequestMapping(value = "/clsPresent", method = RequestMethod.POST)
	public ApiResponse<List<Boolean>> classPresent(@RequestBody List<String> classes) {
		if (CollUtil.isEmpty(classes) || classes.size() > 5) {
			return ApiResponse.fail(GlobalApiCodes.PARAMETER_INVALID.getCode(), GlobalApiCodes.PARAMETER_INVALID.getMessage());
		}
		List<Boolean> resultList = classes.stream().map(item -> ClassUtils.isPresent(item, this.getClass().getClassLoader())).collect(Collectors.toList());
		return ApiResponse.success(resultList);
	}

	@RequestMapping("trespass")
	public ApiResponse<String> trespass(@RequestParam String desc) {
		throw new BusinessException(GlobalApiCodes.REMOTE_CALL_ERROR.formatDefaultMessage("Illegal access, access information has been counted!"));
	}

	@RequestMapping(value = "getCurrentDataSource")
	public ApiResponse<?> getCurrentDataSource() {
		Map<String, Object> map = new HashMap<>();
		map.put("dbType", AbDataSourceUtils.getCurrentDataSource().getDbType().getDb());
		map.put("dsAlias", AbDataSourceUtils.getCurrentDataSource().getDsAlias());
		return ApiResponse.success(map);
	}

	/**
	 * Parse a string containing a freeMark variable
	 * 
	 * @param dto
	 *            str A string containing freeMarker variables
	 * @return
	 */
	@RequestMapping(value = "parseStrByFreeMarker")
	public ApiResponse<?> parseStrByFreeMarker(@RequestBody ParseStrByFreeMarkerDTO dto) {
		return ApiResponse.success(AbFreemarkUtil.parseByString(dto.getStr(), systemVariableApi.getVariableMap()));
	}

	@RequestMapping("getScode")
	public ApiResponse<String> getScode() {
		String issueNo = "";
		boolean isScode = PropertyEnum.IS_SCODE.getPropertyValue(Boolean.class);
		if (!isScode) {
			issueNo = "";
		}else {
			issueNo = SpringUtil.getProperty("abLicence.scode");
//			if (StrUtil.isEmpty(issueNo)) {
//				issueNo = "aschs";
//			}
		}

		return ApiResponse.success(issueNo);
	}
}
