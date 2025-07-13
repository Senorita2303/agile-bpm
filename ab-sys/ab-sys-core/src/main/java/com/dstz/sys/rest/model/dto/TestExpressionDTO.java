package com.dstz.sys.rest.model.dto;

import java.io.Serializable;

/**
 * @Name TestExpressionDTO
 * @description: Test log expression Dto
 */
public class TestExpressionDTO implements Serializable {
	private static final long serialVersionUID = -5631456838690512653L;

	/**
	 * Request parameters
	 */
	private String requestParam;
	/**
	 * Request body
	 */
	private String requestBody;

	/**
	 * Response body
	 */
	private String responseBody;

	/**
	 * Expression
	 */
	private String expressionString;

	public String getRequestParam() {
		return requestParam;
	}

	public void setRequestParam(String requestParam) {
		this.requestParam = requestParam;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public String getExpressionString() {
		return expressionString;
	}

	public void setExpressionString(String expressionString) {
		this.expressionString = expressionString;
	}
}
