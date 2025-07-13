package com.dstz.sys.rest.model.dto;

import com.dstz.sys.api.constant.OnlineDocMethod;

import java.io.Serializable;

/**
 * @Name OnlinDocParamDTO
 * @description: Document operation parameter DTO
 */
public class OnlineDocParamDTO implements Serializable {

	private Integer method;
	private OnlineDocParam params;

	public Integer getMethod() {
		return method;
	}

	public void setMethod(OnlineDocMethod method) {
		this.method = method.getKey();
	}

	public OnlineDocParam getParams() {
		return params;
	}

	public void setParams(OnlineDocParam params) {
		this.params = params;
	}

	public OnlineDocParamDTO() {
	}

	public OnlineDocParamDTO(OnlineDocMethod method, OnlineDocParam params) {
		this.method = method.getKey();
		this.params = params;
	}

	@Override
	public String toString() {
		return "OnlineDocParamDTO{" + "method=" + method + ", params=" + params + '}';
	}
}
