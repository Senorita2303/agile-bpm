package com.dstz.base.utils;

import cn.hutool.core.exceptions.ExceptionUtil;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * jdbc tool class
 *
 */
public class JdbcUtils {

    /**
     * Get the connection schema
     *
     * @return Connection schema
     */
    public static String getConnectionSchema(DataSource dataSource) {
        Connection connection = null;
        try {
            connection = DataSourceUtils.getConnection(dataSource);
            return connection.getSchema();
        } catch (SQLException e) {
            ExceptionUtil.wrapAndThrow(e);
            return null;
        } finally {
            if (connection != null) {
                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        }
    }

}
