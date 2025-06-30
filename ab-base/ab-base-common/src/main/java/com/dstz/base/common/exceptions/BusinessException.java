package com.dstz.base.common.exceptions;

import com.dstz.base.common.codes.IBaseCode;

/**
 * Business logic exceptions are often predictable exceptions. This exception is often an illegal operation message during development, such as a missing process form!
 *
 */
public class BusinessException extends ApiException {

    private static final long serialVersionUID = 4851159171511226800L;

    public BusinessException(IBaseCode baseCode) {
        super(baseCode);
    }

    public BusinessException(IBaseCode baseCode, Throwable throwable) {
        super(baseCode, throwable);
    }

	public static void te(IBaseCode baseCode, Throwable throwable) {
		throw new BusinessException(baseCode,throwable);
	}

	public static void te(IBaseCode baseCode) {
		throw new BusinessException(baseCode);
	}
}
