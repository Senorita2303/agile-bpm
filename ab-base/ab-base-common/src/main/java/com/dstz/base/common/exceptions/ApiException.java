package com.dstz.base.common.exceptions;

import com.dstz.base.common.codes.IBaseCode;

/**
 * <pre>
 * Common exception definition of the interface, used to package exception information when an exception occurs
 * </pre>
 */
public class ApiException extends RuntimeException {

	private static final long serialVersionUID = -254913157871507053L;

	/**
	 * Basic response code
	 */
	private final IBaseCode baseCode;

	public ApiException(IBaseCode baseCode) {
		super(baseCode.getMessage());
		this.baseCode = baseCode;
	}

	public ApiException(IBaseCode baseCode, Throwable throwable) {
		super(baseCode.getMessage(), throwable);
		this.baseCode = baseCode;
	}

	protected ApiException(IBaseCode baseCode, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(baseCode.getMessage(), cause, enableSuppression, writableStackTrace);
		this.baseCode = baseCode;
	}

	public IBaseCode getBaseCode() {
		return baseCode;
	}
}
