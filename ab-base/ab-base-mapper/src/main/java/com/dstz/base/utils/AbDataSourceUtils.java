package com.dstz.base.utils;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.dstz.base.context.AbDataSourceContext;
import com.dstz.base.model.AbDataSourceModel;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;

import javax.sql.DataSource;
import java.util.function.Supplier;

/**
 * Data source context tool class
 *
 */
public class AbDataSourceUtils {

    private static final Logger logger = LoggerFactory.getLogger(AbDataSourceUtils.class);

    private static volatile AbDataSourceContext dataSourceContext;

    /**
     * Get the data source context object
     *
     * @return Data source context object
     */
    public static AbDataSourceContext getDataSourceContext() {
        if (dataSourceContext == null) {
            synchronized (AbDataSourceUtils.class) {
                if (dataSourceContext == null) {
                    dataSourceContext = SpringUtil.getBean(AbDataSourceContext.class);
                }
            }
        }
        return dataSourceContext;
    }

    /**
     * Switching Data Sources
     *
     * @param dsAlias Data source alias
     * @return Data source after switching
     */
    public static void switchDataSource(String dsAlias) {
        getDataSourceContext().switchDataSource(dsAlias);
    }

    /**
     * Get the current data source
     *
     * @return Current Data Source
     */
    public static AbDataSourceModel getCurrentDataSource() {
        return getDataSourceContext().getCurrentDataSource();
    }

    /**
     * Get the data source. This will not switch, but will be added to the current transaction manager.
     *
     * @param dsAlias Data source alias
     * @return Data Source Model
     */
    public static AbDataSourceModel getByDsAlias(String dsAlias) {
        return getDataSourceContext().getByDsAlias(dsAlias);
    }

    /**
     * Close the data source
     *
     * @param dataSource Data Source
     */
    public static void closeDataSource(DataSource dataSource) {
        if (dataSource == null) {
            return;
        }
        try {
            if (dataSource instanceof AutoCloseable) {
                ((AutoCloseable) dataSource).close();
            } else if (dataSource instanceof PooledDataSource) {
                ((PooledDataSource) dataSource).forceCloseAll();
            } else {
                Class<?> targetClass = AopUtils.getTargetClass(dataSource);
                Class<?> dataSourceProxyClass = ClassUtil.loadClass("org.apache.tomcat.jdbc.pool.DataSourceProxy");
                if (dataSourceProxyClass.isAssignableFrom(targetClass)) {
                    ReflectUtil.invoke(dataSource, "close", true);
                }
            }
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Switch data source execution call
     *
     * @param targetDsAlias Target data source
     * @param withSupplier  Execution Method
     * @param <V>           Method return result type
     * @return Method execution results
     */
    public static <V> V switchDataSourceRunWith(String targetDsAlias, Supplier<V> withSupplier) {
        AbDataSourceModel currentDataSource = getCurrentDataSource();
        try {
            switchDataSource(targetDsAlias);
            return withSupplier.get();
        } finally {
            // Switch back to the original calling data source
            switchDataSource(currentDataSource.getDsAlias());
        }
    }
}
