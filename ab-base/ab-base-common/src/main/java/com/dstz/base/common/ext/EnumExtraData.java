package com.dstz.base.common.ext;
/**
 * Enumeration extension data is used to extend the system-defined enumerations and to implement customized related data processing after introduction
 *
 */
public interface EnumExtraData {

    /**
     * Get the enumeration definition name
     *
     * @return Enumeration definition name
     */
    String getName();

    /**
     * Get the definition key
     *
     * @return Enumeration definition keys
     */
    String getKey();

    /**
     * Get definition description
     *
     * @return Enumeration definition description
     */
    String getDesc();
}
