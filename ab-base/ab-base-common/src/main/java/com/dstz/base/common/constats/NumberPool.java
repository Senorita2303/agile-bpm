package com.dstz.base.common.constats;

/**
 * Numeric constant pool
 *
 */
public class NumberPool {

	private NumberPool() throws IllegalAccessException {
		throw new IllegalAccessException();
	}

	/**
	 * Reusable Long constant for zero.
	 */
	public static final Long LONG_ZERO = 0L;
	/**
	 * Reusable Long constant for one.
	 */
	public static final Long LONG_ONE = 1L;
	/**
	 * Reusable Long constant for minus one.
	 */
	public static final Long LONG_MINUS_ONE = -1L;
	/**
	 * Reusable Integer constant for zero.
	 */
	public static final Integer INTEGER_ZERO = 0;
	/**
	 * Reusable Integer constant for one.
	 */
	public static final Integer INTEGER_ONE = 1;
	/**
	 * Reusable Integer constant for two
	 */
	public static final Integer INTEGER_TWO = 2;
	/**
	 * Reusable Integer constant for minus one.
	 */
	public static final Integer INTEGER_MINUS_ONE = -1;
	/**
	 * Reusable Short constant for zero.
	 */
	public static final Short SHORT_ZERO = (short) 0;
	/**
	 * Reusable Short constant for one.
	 */
	public static final Short SHORT_ONE = (short) 1;
	/**
	 * Reusable Short constant for minus one.
	 */
	public static final Short SHORT_MINUS_ONE = (short) -1;
	/**
	 * Reusable Byte constant for zero.
	 */
	public static final Byte BYTE_ZERO = (byte) 0;
	/**
	 * Reusable Byte constant for one.
	 */
	public static final Byte BYTE_ONE = (byte) 1;
	/**
	 * Reusable Byte constant for minus one.
	 */
	public static final Byte BYTE_MINUS_ONE = (byte) -1;
	/**
	 * Reusable Double constant for zero.
	 */
	public static final Double DOUBLE_ZERO = 0.0d;
	/**
	 * Reusable Double constant for one.
	 */
	public static final Double DOUBLE_ONE = 1.0d;
	/**
	 * Reusable Double constant for minus one.
	 */
	public static final Double DOUBLE_MINUS_ONE = -1.0d;
	/**
	 * Reusable Float constant for zero.
	 */
	public static final Float FLOAT_ZERO = 0.0f;
	/**
	 * Reusable Float constant for one.
	 */
	public static final Float FLOAT_ONE = 1.0f;
	/**
	 * Reusable Float constant for minus one.
	 */
	public static final Float FLOAT_MINUS_ONE = -1.0f;

	/**
	 * {@link Integer#MAX_VALUE} as a {@link Long}.
	 *
	 * @since 3.12.0
	 */
	public static final Long LONG_INT_MAX_VALUE = (long) Integer.MAX_VALUE;

	/**
	 * {@link Integer#MIN_VALUE} as a {@link Long}.
	 *
	 * @since 3.12.0
	 */
	public static final Long LONG_INT_MIN_VALUE = (long) Integer.MIN_VALUE;

	/**
	 * The value of integer that is considered false
	 */
	public static final Integer BOOLEAN_FALSE = 0;

	/**
	 * The value of integer that is considered true
	 */
	public static final Integer BOOLEAN_TRUE = 1;

}