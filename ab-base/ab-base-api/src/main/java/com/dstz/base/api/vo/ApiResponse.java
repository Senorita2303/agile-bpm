package com.dstz.base.api.vo;

import com.dstz.base.api.dto.PageListDTO;
import com.dstz.base.api.service.Executable;

/**
 * <pre>
 * Global interface response definition
 * </pre>
 */
public class ApiResponse<T> {

	/**
	 * Status Code - Success
	 */
	public static final String CODE_SUCCESS = "Success";

	/**
	 * Status-Success-Message
	 */
	public static final String CODE_SUCCESS_MESSAGE = "Operation successful";

	/**
	 * Success
	 */
	private Boolean isOk;

	/**
	 * Status Code
	 */
	private String code;

	/**
	 * Information
	 */
	private String message;

	/**
	 * Script log, need to enable scriptLog to return
	*/
	private String scriptLog;

	/**
	 * Response Body
	 */
	private T data;

	public Boolean getIsOk() {
		return isOk;
	}

	public void setIsOk(Boolean ok) {
		isOk = ok;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return message;
	}

	public void setMessage(String msg) {
		this.message = msg;
	}

	public String getMessage() {
		return message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	/**
	 * Is the use successful?
	 *
	 * @param isOk Success
	 * @return Interface response
	 */
	public ApiResponse<T> withIsOk(Boolean isOk) {
		this.isOk = isOk;
		return this;
	}

	/**
	 * Using Status Code
	 *
	 * @param code Status Code
	 * @return Interface response
	 */
	public ApiResponse<T> withCode(String code) {
		this.code = code;
		return this;
	}

	/**
	 * Using Status Messages
	 *
	 * @param msg Status Messages
	 * @return Interface response
	 */
	public ApiResponse<T> withMessage(String msg) {
		this.message = msg;
		return this;
	}

	/**
	 * Usage Data
	 *
	 * @param data data
	 * @return Interface response
	 */
	public ApiResponse<T> withData(T data) {
		this.data = data;
		return this;
	}

	/**
	 * Successful response
	 *
	 * @param <T> T
	 * @return Interface response
	 */
	public static <T> ApiResponse<T> success() {
		return success((T) null);
	}

	/**
	 * Successful response
	 *
	 * @param data data
	 * @param <T>  T
	 * @return Interface response
	 */
	public static <T> ApiResponse<T> success(T data) {
		return new ApiResponse<T>().withIsOk(Boolean.TRUE).withCode(CODE_SUCCESS).withMessage(CODE_SUCCESS_MESSAGE)
				.withData(data);
	}

	public static <T> ApiResponse<PageListVO<T>> success(PageListDTO<T> data) {
		return new ApiResponse<PageListVO<T>>().withIsOk(Boolean.TRUE).withCode(CODE_SUCCESS)
				.withMessage(CODE_SUCCESS_MESSAGE).withData(new PageListVO<T>(data));
	}

	/**
	 * Successful response (no return value)
	 *
	 * @param executable Executor, does not return any data (for example: add, delete, or modify)
	 * @return ApiResponse
	 */
	public static <T> ApiResponse<T> success(Executable executable) {
		executable.execute();
		// Due to the operation return, it is changed from English to Chinese to improve the experience
		return ApiResponse.<T>success().withMessage(CODE_SUCCESS_MESSAGE);
	}

	/**
	 * Failure Response
	 *
	 * @param code    Response Code
	 * @param message Response Information
	 * @param <T>     T
	 * @return Interface response
	 */
	public static <T> ApiResponse<T> fail(String code, String message) {
		return fail(code, message, null);
	}

	/**
	 * 失败响应
	 *
	 * @param code    Response Code
	 * @param message Response Information
	 * @param body    content body
	 * @param <T>     T
	 * @return Interface response
	 */
	public static <T> ApiResponse<T> fail(String code, String message, T body) {
		return new ApiResponse<T>().withIsOk(Boolean.FALSE).withCode(code).withMessage(message).withData(body);
	}

	@Override
	public String toString() {
		return "ApiResponse{" + "isOk=" + isOk + ", code='" + code + '\'' + ", message='" + message + '\'' + ", data="
				+ data + '}';
	}

	public String getScriptLog() {
		return scriptLog;
	}

	public void setScriptLog(String scriptLog) {
		this.scriptLog = scriptLog;
	}

}
