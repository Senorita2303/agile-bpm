package com.dstz.base.entity;

import java.util.Date;

/**
 * Persistent entities, all entities should implement
 *
 */
public interface IPersistentEntity {

	/**
	 * Set ID
	 *
	 * @param id ID
	 */
	void setId(String id);

	/**
	 * Get ID
	 *
	 * @return ID
	 */
	String getId();

	/**
	 * Set Creator ID
	 *
	 * @param createBy Creator ID
	 */
	default void setCreateBy(String createBy) {
	}

	/**
	 * Get the creator ID
	 *
	 * @return Creator ID
	 */
	default String getCreateBy() {
		return null;
	}

	/**
	 * Set creation time
	 *
	 * @param createTime Creation time
	 */
	default void setCreateTime(Date createTime) {
	}

	/**
	 * Get creation time
	 *
	 * @return Creation time
	 */
	default Date getCreateTime() {
		return null;
	}

	/**
	 * Set Updater ID
	 *
	 * @param updateBy Updater ID
	 */
	default void setUpdateBy(String updateBy) {
	}

	/**
	 * Get the updater ID
	 *
	 * @return Updater ID
	 */
	default String getUpdateBy() {
		return null;
	}

	/**
	 * Settings Update
	 *
	 * @param updater Updated by
	 */
	default void setUpdater(String updater) {
	}

	/**
	 * Get Updater
	 *
	 * @return Updated by
	 */
	default String getUpdater() {
		return null;
	}

	/**
	 * Set update time
	 *
	 * @param updateTime Update time
	 */
	default void setUpdateTime(Date updateTime) {
	}

	/**
	 * Get update time
	 *
	 * @return Update time
	 */
	default Date getUpdateTime() {
		return null;
	}

	/**
	 * Set optimistic lock version number
	 *
	 * @param rev Optimistic locking version number
	 */
	default void setRev(Integer rev) {
	}

	/**
	 * Get the optimistic lock version number
	 *
	 * @return Optimistic locking version number
	 */
	default Integer getRev() {
		return null;
	}
}
