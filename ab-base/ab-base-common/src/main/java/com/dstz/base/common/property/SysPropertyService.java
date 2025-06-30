package com.dstz.base.common.property;

public interface SysPropertyService {

    /**
     * Returns the value of an integer parameter by its alias.
     *
     * @param code Parameter code
     * @return Parameter Value
     */
    Integer getIntByCode(String code);

    /**
     * Returns the long integer value of the parameter according to its alias.
     *
     * @param code Parameter code
     * @return Parameter Value
     */
    Long getLongByCode(String code);

    /**
     * Returns the Boolean parameter value based on its alias.
     *
     * @param code Parameter code
     * @return Parameter Value
     */
    Boolean getBooleanByCode(String code);

    /**
     * Returns the string parameter value based on its alias.
     *
     * @param code Parameter code
     * @return Parameter Value
     */
	String getValByCode(String code);

}
