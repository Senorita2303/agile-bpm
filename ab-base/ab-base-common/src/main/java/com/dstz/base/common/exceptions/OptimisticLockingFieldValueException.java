package com.dstz.base.common.exceptions;

/**
 * Optimistic lock field value is abnormal
 *
 */
public class OptimisticLockingFieldValueException extends RuntimeException{

	private static final long serialVersionUID = 8448037384631122436L;

	public OptimisticLockingFieldValueException(String message) {
		super(message, null, false, false);
	}
}
