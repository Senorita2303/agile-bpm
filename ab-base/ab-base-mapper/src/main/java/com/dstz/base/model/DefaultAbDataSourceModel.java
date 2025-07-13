package com.dstz.base.model;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.dstz.base.enums.AbDbType;
import com.dstz.base.utils.AbDataSourceUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.function.SingletonSupplier;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Data Source Model
 *
 */
public class DefaultAbDataSourceModel implements AbDataSourceModel {

    private static final String SEATA_DS_PROXY_CLASS = "io.seata.rm.datasource.DataSourceProxy";

    private static final boolean SEATA_DS_PROXY_CLASS_PRESENT = ClassUtils.isPresent(SEATA_DS_PROXY_CLASS, Thread.currentThread().getContextClassLoader());

    /**
     * Data source alias
     */
    private final String dsAlias;

    /**
     * Data Source
     */
    private final DataSource dataSource;

    /**
     * Database Type
     */
    private final AbDbType dbType;

    /**
     * Operation template
     */
    private final SingletonSupplier<JdbcOperations> jdbcOperations = SingletonSupplier.of(() -> new JdbcTemplate(getDataSource()));

    /**
     * jdbc Transaction Processor
     */
    private final SingletonSupplier<DataSourceTransactionManager> dataSourceTransactionManager = SingletonSupplier.of(() -> new DataSourceTransactionManager(getDataSource()));

    private final SingletonSupplier<ConcurrentHashMap<Object, Object>> resources = SingletonSupplier.of(ConcurrentHashMap::new);

    public DefaultAbDataSourceModel(String dbAlias, DataSource dataSource) {
        this.dsAlias = dbAlias;
        this.dataSource = proxyDataSource(dataSource);
        try {
            this.dbType = AbDbType.fromProductName(JdbcUtils.extractDatabaseMetaData(dataSource, DatabaseMetaData::getDatabaseProductName));
        } catch (MetaDataAccessException e) {
            ReflectionUtils.rethrowRuntimeException(e);
            // Bypassing compilation checks
            throw new RuntimeException();
        }
        // Publish Create Event
        SpringUtil.getBeansOfType(AbDataSourceModeInitializeSchema.class).values().forEach(initializer -> initializer.initialize(DefaultAbDataSourceModel.this));
    }

    private DataSource proxyDataSource(DataSource dataSource) {
        // Agent Seata
        if (SEATA_DS_PROXY_CLASS_PRESENT) {
            Class<? extends DataSource> seataProxyClass = ClassUtil.loadClass(SEATA_DS_PROXY_CLASS, false);
            if (!seataProxyClass.isAssignableFrom(AopUtils.getTargetClass(dataSource))) {
                return ReflectUtil.newInstance(seataProxyClass, dataSource);
            }
        }
        return dataSource;
    }

    @Override
    public String getDsAlias() {
        return dsAlias;
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public AbDbType getDbType() {
        return dbType;
    }

    @Override
    public JdbcOperations getJdbcOperations() {
        return this.jdbcOperations.get();
    }

    @Override
    public DataSourceTransactionManager getDataSourceTransactionManager() {
        return dataSourceTransactionManager.get();
    }

    /**
     * Get resources
     *
     * @param key             Related resource KEY
     * @param mappingFunction If it does not exist, call the mapping function
     * @return resource
     */
    @Override
    public Object getResource(Object key, Supplier<Object> mappingFunction) {
        return Objects.requireNonNull(resources.get()).computeIfAbsent(key, k -> mappingFunction.get());
    }

    /**
     * Unbinding resources
     *
     * @param key Association Key
     * @return Unbinding resources
     */
    @Override
    public Object unbindResource(Object key) {
        return Objects.requireNonNull(resources.get()).remove(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbDataSourceModel that = (AbDataSourceModel) o;
        return dsAlias.equals(that.getDsAlias());
    }

    @Override
    public int hashCode() {
        return Objects.hash(dsAlias);
    }

    @Override
    public void close() throws Exception {
        Objects.requireNonNull(resources.get()).clear();
        AbDataSourceUtils.closeDataSource(getDataSource());
    }
}
