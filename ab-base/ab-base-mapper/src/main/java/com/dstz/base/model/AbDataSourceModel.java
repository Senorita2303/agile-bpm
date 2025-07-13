package com.dstz.base.model;

import com.dstz.base.enums.AbDbType;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.function.Supplier;

/**
 * Data Source Model
 *
 */
public interface AbDataSourceModel extends AutoCloseable {

	/**
	 * Get the data source alias
	 *
	 * @return Data source alias
	 */
	String getDsAlias();

	/**
	 * Get the database type
	 *
	 * @return Database Type
	 */
	AbDbType getDbType();

	/**
	 * Get the transaction manager
	 *
	 * @return Transaction Manager
	 */
	DataSourceTransactionManager getDataSourceTransactionManager();

	/**
	 * Get the data source
	 *
	 * @return Data Source
	 */
	DataSource getDataSource();

	/**
	 * Get JDBC Operator
	 *
	 * @return JDBC Operator
	 */
	JdbcOperations getJdbcOperations();

	/**
	 * Get resources
	 *
	 * @param key             Resource KEY
	 * @param mappingFunction If the resource does not exist, the function will be called to obtain it.
	 * @return resource
	 */
	Object getResource(Object key, Supplier<Object> mappingFunction);

	/**
	 * Unbinding resources
	 *
	 * @param key Resource KEY
	 * @return Resource Object
	 */
	Object unbindResource(Object key);
}
