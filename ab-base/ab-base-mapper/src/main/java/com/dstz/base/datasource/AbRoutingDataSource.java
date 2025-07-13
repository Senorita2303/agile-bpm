package com.dstz.base.datasource;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.dstz.base.common.codes.IBaseCode;
import com.dstz.base.common.exceptions.ApiException;
import com.dstz.base.common.exceptions.BusinessException;
import com.dstz.base.common.utils.ContextCleanUtils;
import com.dstz.base.context.AbDataSourceContext;
import com.dstz.base.event.AbDataSourceEvent;
import com.dstz.base.model.AbDataSourceModel;
import com.dstz.base.model.DefaultAbDataSourceModel;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.UncheckedExecutionException;
import org.springframework.jdbc.datasource.AbstractDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.concurrent.ExecutionException;

/**
 * <pre>
 * Ab Routing Data Source
 * </pre>
 */
public class AbRoutingDataSource extends AbstractDataSource implements AbDataSourceContext {

    private final AbDataSourceModel defaultTargetDataSource;

    private final Cache<String, AbDataSourceModel> targetDataSources;

    private final ThreadLocal<AbDataSourceModel> currentDataSourceModelLocal = new ThreadLocal<>();

    public AbRoutingDataSource(DataSource primaryDataSource) {
        this.defaultTargetDataSource = new DefaultAbDataSourceModel(DEFAULT_DATASOURCE_ALIAS, primaryDataSource);
        targetDataSources = CacheBuilder.newBuilder()
                                        .expireAfterAccess(Duration.ofMinutes(30))
                                        .maximumSize(128)
                                        .removalListener(notification -> SpringUtil.publishEvent(new AbDataSourceEvent((AbDataSourceModel) notification.getValue(), AbDataSourceEvent.EventType.REMOVE_AFTER)))
                                        .build();
        ContextCleanUtils.register(this::clear, ContextCleanUtils.Phase.THREAD, ContextCleanUtils.Phase.REQUEST_COMPLETE);
    }
    
    @Override
    public Connection getConnection() throws SQLException {
        return getCurrentDataSource().getDataSource().getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return getConnection();
    }

    @Override
    public void switchDataSource(String dsAlias) {
        AbDataSourceModel currentDataSource = getCurrentDataSource();
        // The switch is executed only when the current data source is inconsistent with the incoming data source
        if (!currentDataSource.getDsAlias().equals(dsAlias)) {
            AbDataSourceModel abDataSourceModel = getByDsAlias(dsAlias);
            SpringUtil.publishEvent(new AbDataSourceEvent(abDataSourceModel, AbDataSourceEvent.EventType.SWITCH_BEFORE));
            currentDataSourceModelLocal.set(abDataSourceModel);
        }
    }

    @Override
    public AbDataSourceModel getCurrentDataSource() {
        AbDataSourceModel abDataSourceModel = currentDataSourceModelLocal.get();
        if (abDataSourceModel == null) {
            return defaultTargetDataSource;
        }
        return abDataSourceModel;
    }

    @Override
    public AbDataSourceModel getDefaultDataSource() {
        return getByDsAlias(DEFAULT_DATASOURCE_ALIAS);
    }

    @Override
    public AbDataSourceModel getByDsAlias(String dsAlias) {
        AbDataSourceModel abDataSourceModel;
        if (DEFAULT_DATASOURCE_ALIAS.equals(dsAlias)) {
            abDataSourceModel = defaultTargetDataSource;
            // Triggering recycling of data sources
            targetDataSources.getIfPresent(StrUtil.EMPTY);
        } else {
            try {
                abDataSourceModel = targetDataSources.get(dsAlias, () -> new DefaultAbDataSourceModel(dsAlias, SpringUtil.getBean(DataSourceLoading.class).loadDataSource(dsAlias)));
            } catch (Throwable ex) {
                if(ex instanceof ExecutionException || ex instanceof UncheckedExecutionException){
                    ex = ex.getCause();
                }
                if(ex instanceof ApiException){
                    throw (ApiException) ex;
                }
                IBaseCode baseCode = IBaseCode.newBuilder()
                        .withCode("ObtainDataSourceFailure")
                        .withMessage("Failed to obtain system data source [{}], failure reason: {}", dsAlias, ex.getMessage())
                        .build();
                throw new BusinessException(baseCode, ex);
            }
        }
        SpringUtil.publishEvent(new AbDataSourceEvent(abDataSourceModel, AbDataSourceEvent.EventType.GET_BEFORE));
        return abDataSourceModel;
    }

    @Override
    public void removeDataSource(String dsAlias) {
        targetDataSources.invalidate(dsAlias);
    }

    @Override
    public void clear() {
        currentDataSourceModelLocal.remove();
    }

    @Override
    public boolean equals(Object thatDataSource) {
        DataSource thisDataSource = getCurrentDataSource().getDataSource();
        if (thisDataSource == thatDataSource) {
            return true;
        }
        if (thatDataSource == null || thisDataSource.getClass() != thatDataSource.getClass()) {
            return false;
        }
        return thisDataSource.equals(thatDataSource);
    }

    @Override
    public int hashCode() {
        return getCurrentDataSource().getDataSource().hashCode();
    }
}
