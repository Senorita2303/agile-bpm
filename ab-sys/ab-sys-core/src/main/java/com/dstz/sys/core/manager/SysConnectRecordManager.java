package com.dstz.sys.core.manager;

import com.dstz.base.manager.AbBaseManager;
import com.dstz.sys.core.entity.SysConnectRecord;

import java.util.List;

/**
 * <p>
 * Public business related records General business category
 * </p>
 * 
 */
public interface SysConnectRecordManager extends AbBaseManager<SysConnectRecord> {

    /**
     * Get by target ID and type
     *
     * @param targetId
     * @param type
     * @return
     */
    List<SysConnectRecord> getByTargetId(String targetId, String type);

    /**
     * Get by resource ID and type
     * @param sourceId
     * @param type
     * @return
     */
    List<SysConnectRecord> getBySourceId(List<String> sourceIds, String type);

    /**
     * Delete records based on source ID
     *
     * @param sourceId
     * @param type
     */
    void removeBySourceId(String sourceId, String type);
}
