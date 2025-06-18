package com.dstz.base.common.vo;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.common.codes.IBaseCode;
import com.dstz.base.common.enums.GlobalApiCodes;
import com.dstz.base.common.exceptions.BusinessException;
import com.dstz.base.common.exceptions.BusinessMessage;

import java.util.Optional;
import java.util.function.Function;

/**
 * Interface Response Extractor
 *
 */
public class ApiResponseExtractor<T> {

	private final ApiResponse<T> apiResponse;

	public ApiResponseExtractor(ApiResponse<T> apiResponse) {
		Assert.notNull(apiResponse, () -> new BusinessException(GlobalApiCodes.REMOTE_CALL_ERROR.formatDefaultMessage("ApiResponse is null")));
		this.apiResponse = apiResponse;
	}

	/**
	 * Assert whether the interface response is successful
	 *
	 * <p>
	 *     Interface response if present
	 * </p>
	 *
	 * @return
	 */
	public ApiResponseExtractor<T> assertSuccess() {
		// Check if the login session has timed out
		if (StrUtil.equals(apiResponse.getCode(), GlobalApiCodes.LOGIN_INVALID.getCode())) {
			throw new BusinessMessage(GlobalApiCodes.LOGIN_INVALID);
		} else if (!StrUtil.equals(apiResponse.getCode(), GlobalApiCodes.SUCCESS.getCode())) {
			throw new BusinessException(IBaseCode.newBuilder().withCode(apiResponse.getCode()).withMessage(apiResponse.getMessage()).build());
		}
		return this;
	}

	public ApiResponseExtractor<T> assertCode(String code, Function<ApiResponse<T>, ? extends RuntimeException> errorFunction) {
		Assert.isTrue(apiResponse.getCode().equalsIgnoreCase(code), () -> errorFunction.apply(apiResponse));
		return this;
	}

	public T getData() {
		return apiResponse.getData();
	}

	public Optional<T> getDataOptional() {
		return Optional.ofNullable(apiResponse.getData());
	}

	public static <T> ApiResponseExtractor<T> of(ApiResponse<T> apiResponse) {
		return new ApiResponseExtractor<>(apiResponse);
	}
}
