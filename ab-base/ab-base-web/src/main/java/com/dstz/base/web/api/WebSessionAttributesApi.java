package com.dstz.base.web.api;

import java.io.Serializable;
import java.util.Map;

/**
 * Web session attribute operation, used to operate the current session user attributes
 *
 */
public interface WebSessionAttributesApi {

	/**
	 * Setting Value
	 *
	 * @param name  Property name
	 * @param value Attribute value. If null is set, the corresponding attribute will be deleted.
	 */
	void setValue(String name, Serializable value);

	/**
	 * Set the batch value
	 *
	 * @param values Same usage as setValue
	 */
	void setValues(Map<String, ? extends Serializable> values);

	/**
	 * Get Value
	 *
	 * @param name Property name
	 */
	Serializable getValue(String name);
}
