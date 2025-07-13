package com.dstz.base.datasource;

import javax.sql.DataSource;

/**
 * Data source loading
 *
 */
public interface DataSourceLoading {

	/**
	 * Loading Data Sources
	 * @param alias Aliases
	 * @return Data Source
	 */
	DataSource loadDataSource(String alias);
}
