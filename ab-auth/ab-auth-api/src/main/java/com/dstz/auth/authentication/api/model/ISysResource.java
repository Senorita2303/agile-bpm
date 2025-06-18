package com.dstz.auth.authentication.api.model;

import com.dstz.base.api.model.Tree;

public interface ISysResource extends Tree {

	/**
	 * Return Primary Key
	 *
	 * @return
	 */
	public String getId();

	/**
	 * Return Subsystem ID
	 *
	 * @return
	 */
	public String getAppId();

	/**
	 * Return Resource Alias
	 *
	 * @return
	 */
	public String getCode();

	public String getPath();

	/**
	 * Return Resource Name
	 *
	 * @return
	 */
	public String getName();

	/**
	 * Return to default address
	 *
	 * @return
	 */
	public String getUrl();

	/**
	 * Return to display menu (1, display, 0, do not display)
	 *
	 * @return
	 */
	public Integer getEnable();

	/**
	 * Return OPENED_
	 *
	 * @return
	 */
	public Integer getOpened();

	/**
	 * Is it an API resource?
	 */

	Integer getIsApi();

	/**
	 * Back Icon
	 *
	 * @return
	 */
	public String getIcon();

	/**
	 * Return Type
	 *
	 * @return
	 */
	public String getType();

	/**
	 * Back Sort
	 *
	 * @return
	 */
	public Integer getSn();

	public String getParentId();

	Boolean getHidden();

}
