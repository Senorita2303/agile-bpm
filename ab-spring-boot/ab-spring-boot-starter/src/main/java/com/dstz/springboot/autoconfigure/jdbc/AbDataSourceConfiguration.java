package com.dstz.springboot.autoconfigure.jdbc;

import com.dstz.base.datasource.AbRoutingDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * ab data source configuration
 *
 */
@Configuration
public class AbDataSourceConfiguration {


	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	public DataSource dataSource(DataSourceProperties dataSourceProperties) {
		return dataSourceProperties.initializeDataSourceBuilder().build();
	}

	@Primary
	@Bean
	public DataSource primaryDataSource() {
		return new AbRoutingDataSource(dataSource(null));
	}
}
