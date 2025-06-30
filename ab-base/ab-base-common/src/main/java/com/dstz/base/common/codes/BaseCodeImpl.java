package com.dstz.base.common.codes;

/**
 * Base code implementation
 *
 */
class BaseCodeImpl implements IBaseCode {

	private String code;

	private String message;

	public BaseCodeImpl() {
		super();
	}

	public BaseCodeImpl(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	@Override
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "BaseCodeImpl{" + "code='" + code + '\'' + ", message='" + message + '\'' + '}';
	}
}
