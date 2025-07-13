package com.dstz.base.common.codes;

import cn.hutool.core.util.StrUtil;
import com.dstz.base.api.vo.ApiResponse;
import org.slf4j.helpers.MessageFormatter;

/**
 * <pre>
 * System Interface Response Code Definition
 * </pre>
 */
public interface IBaseCode {

	/**
	 * Get Status Code
	 *
	 * @return Status Code
	 */
	String getCode();

	/**
	 * Get messages, support parameter formatting, usage is the same as slf4j
	 *
	 * @return Status information mode
	 */
	String getMessage();

	/**
	 * Formatting the default message
	 *
	 * @param arguments Parameters
	 * @return New BaseCode
	 */
	default IBaseCode formatDefaultMessage(Object... arguments) {
		return formatMessage(getMessage(), arguments);
	}

	/**
	 * Format message, parameters support format mode, usage is the same as slf4j
	 *
	 * @param messagePattern Get message mode, can do parameter formatting, usage is the same as slf4j
	 * @param arguments      Parameters
	 * @return New BaseCode
	 */
	@Deprecated
	default IBaseCode formatMessage(String messagePattern, Object... arguments) {
		return newBuilder().withCode(getCode()).withMessage(messagePattern, arguments).build();
	}

	/**
	 * <pre>
	 * Use hutool's StrUtil.format to format information
	 * eg：
	 * xxx.formatHutoolMessage("a={},b={}", a, b)
	 * </pre>	
	 * 
	 * @param messagePattern
	 * @param arguments
	 * @return
	 */
	default IBaseCode formatHutoolMessage(String messagePattern, Object... arguments) {
		return formatDefaultMessage(StrUtil.format(messagePattern, arguments));
	}

	/**
	 * New Builder
	 *
	 * @return Builder
	 */
	static Builder newBuilder() {
		return new Builder();
	}

	/**
	 * Builder
	 */
	class Builder {

		private final BaseCodeImpl baseCode = new BaseCodeImpl();

		/**
		 * Using Status Codes
		 *
		 * @param code Status Code
		 * @return Builder
		 */
		public Builder withCode(String code) {
			baseCode.setCode(code);
			return this;
		}

		/**
		 * Using Messages
		 *
		 * @param format    Formatting, consistent with the usage of slf4j logger
		 * @param arguments Formatting parameters
		 * @return Builder
		 */
		public Builder withMessage(String format, Object... arguments) {
			if (arguments == null || arguments.length == 0) {
				baseCode.setMessage(format);
			} else {
				baseCode.setMessage(MessageFormatter.arrayFormat(format, arguments).getMessage());
			}
			return this;
		}

		/**
		 * Constructing a response code
		 *
		 * @return Response Code
		 */
		public IBaseCode build() {
			return baseCode;
		}
	}

	/**
	 * Constructing an interface response
	 *
	 * @param <T> Interface response body
	 * @return Interface response
	 */
	default <T> ApiResponse<T> buildApiResponse() {
		return ApiResponse.fail(getCode(), getMessage());
	}
}
