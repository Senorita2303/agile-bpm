package com.dstz.org.api.model;

/**
 * Group Type Interface
 *
 */
public interface IGroup extends java.io.Serializable {

	/**
	 * Attribute-Alias
	 */
	String ATTR_CODE = "code";

	/**
	 * Get Group ID
	 *
	 * @return Group ID
	 */
	String getGroupId();

	/**
	 * Get Group Name
	 *
	 * @return Group Name
	 */
	String getGroupName();

	/**
	 * Get group type
	 *
	 * @return Group Type
	 */
	String getGroupType();

	/**
	 * Group Code
	 *
	 * @return Group Code
	 */
	String getGroupCode();

	/**
	 * Get the parent ID
	 *
	 * @return Parent ID
	 */
	default String getParentId() {
		return null;
	}

	/**
	 * Get Organization Level
	 * @return Organizational Level
	 */
	Integer getGroupLevel();

	/**
	 * Get attribute value
	 *
	 * @param attrName Attribute Name
	 * @param tClass   Attribute value type class
	 * @param <T>      T
	 * @return Attribute Value
	 */
	<T> T getAttrValue(String attrName, Class<T> tClass);
}
