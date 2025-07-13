package com.dstz.base.datasource;

import com.dstz.base.event.AbDataSourceEvent;
import com.dstz.base.model.AbDataSourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Data source removes listener
 *
 */
@Component
public class DataSourceRemoveListener implements ApplicationListener<AbDataSourceEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceRemoveListener.class);

	@Override
	public void onApplicationEvent(AbDataSourceEvent event) {
		event.ifEventType(AbDataSourceEvent.EventType.REMOVE_AFTER, this::closeDataSource);
	}

	private void closeDataSource(AbDataSourceModel dataSourceModel) {
		try {
			dataSourceModel.close();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
}
