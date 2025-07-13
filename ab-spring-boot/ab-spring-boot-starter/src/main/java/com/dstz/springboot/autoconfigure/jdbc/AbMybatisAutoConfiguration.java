package com.dstz.springboot.autoconfigure.jdbc;


import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.dstz.base.common.exceptions.OptimisticLockingFieldValueException;
import com.dstz.base.context.AbDataSourceContext;
import com.dstz.base.datasource.AbRoutingDataSource;
import com.dstz.base.interceptor.AbPaginationInnerInterceptor;
import com.dstz.base.interceptor.AbQueryFilterInterceptor;
import com.dstz.base.interceptor.MybatisPlusInterceptorCustomizer;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.transaction.SpringManagedTransaction;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;

/**
 * ab mybatis configuration
 *
 */
@ConditionalOnClass({MybatisPlusAutoConfiguration.class, SqlSessionFactory.class, SqlSessionFactoryBean.class})
@Configuration
public class AbMybatisAutoConfiguration {

    @ConditionalOnMissingBean
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(ObjectProvider<MybatisPlusInterceptorCustomizer> customizerObjectProvider) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new AbPaginationInnerInterceptor());

        // Optimistic locking configuration
        OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor = new OptimisticLockerInnerInterceptor();
        // Verify optimistic lock field value when updating
        optimisticLockerInnerInterceptor.setException(new OptimisticLockingFieldValueException("Optimistic field value is null"));
        interceptor.addInnerInterceptor(optimisticLockerInnerInterceptor);
        customizerObjectProvider.forEach(mybatisPlusInterceptorCustomizer -> mybatisPlusInterceptorCustomizer.customize(interceptor));
        return interceptor;
    }

    @Bean
    public ConfigurationCustomizer abConfigurationCustomizer() {
        return configuration -> configuration.setJdbcTypeForNull(JdbcType.NULL);
    }

    @ConditionalOnMissingBean
    @Bean
    public AbQueryFilterInterceptor abQueryFilterInterceptor() {
        return new AbQueryFilterInterceptor();
    }

    @Bean
    public TransactionFactory springManagedTransactionFactory() {
        return new SpringManagedTransactionFactory() {
            @Override
            public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
                if(dataSource instanceof TransactionAwareDataSourceProxy){
                    dataSource = ((TransactionAwareDataSourceProxy) dataSource).getTargetDataSource();
                }
                if (dataSource instanceof AbRoutingDataSource) {
                    dataSource = ((AbRoutingDataSource) dataSource).getCurrentDataSource().getDataSource();
                }
                return new SpringManagedTransaction(dataSource);
            }
        };
    }

    @Bean
    public DatabaseIdProvider databaseIdProvider(AbDataSourceContext dataSourceContext) {
        return dataSource -> dataSourceContext.getDefaultDataSource().getDbType().getDb();
    }
}
