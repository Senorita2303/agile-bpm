package com.dstz.component.mq.msg.engine.core.manager;

import com.dstz.base.manager.AbBaseManager;
import com.dstz.component.mq.msg.engine.core.entity.AbMessageLog;

import java.util.List;

/**
 * <p>
 * Third-party message sending log General business class
 * </p>
 *
 */
public interface AbMessageLogManager extends AbBaseManager<AbMessageLog> {

    /**
     * Update callback status according to task id
     * @param businessId Business ID
     * @return Number of rows affected
     */
    Integer updateMsgLogStatusByBusinessId(String businessId,String type);

    /**
     * Get unprocessed message logs based on task id
     * @param businessId Business ID
     * @return
     */
    List<AbMessageLog> getByBusinessId(String businessId);

}
