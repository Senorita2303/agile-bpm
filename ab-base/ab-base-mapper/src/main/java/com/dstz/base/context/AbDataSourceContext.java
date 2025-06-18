package com.dstz.base.context;

import com.dstz.base.model.AbDataSourceModel;

/**
 * Database context object
 *
 */
public interface AbDataSourceContext {

    /**
     * Default data source alias
     */
    String DEFAULT_DATASOURCE_ALIAS = "dataSourceDefault";

    /**
     * Switching Data Sources
     *
     * @param dsAlias Data source alias
     */
    void switchDataSource(String dsAlias);

    /**
     * Get the current data source
     *
     * @return Data Source
     */
    AbDataSourceModel getCurrentDataSource();

    /**
     * Get the default data source
     *
     * @return Default Data Source
     */
    AbDataSourceModel getDefaultDataSource();

    /**
     * Get the data source. This acquisition will not switch and will be added to the current transaction manager.
     *
     * @param dsAlias Data source alias
     * @return Data Source Model
     */
    AbDataSourceModel getByDsAlias(String dsAlias);

    /**
     * Delete data source
     *
     * @param dsAlias Data source alias
     */
    void removeDataSource(String dsAlias);

    /**
     * Cleaning up the context
     */
    void clear();
}
