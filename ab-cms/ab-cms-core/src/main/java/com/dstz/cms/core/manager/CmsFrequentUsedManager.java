package com.dstz.cms.core.manager;

import com.dstz.base.manager.AbBaseManager;
import com.dstz.cms.core.entity.CmsFrequentUsed;

import java.util.List;

/**
 * <p>
 * Common process management General business
 * </p>
 *
 */
public interface CmsFrequentUsedManager extends AbBaseManager<CmsFrequentUsed> {

    /**
     * Add common processes in batches
     *
     * @param resourceIdList Common process ID collection
     */
    void saveBatch(List<String> resourceIdList);
}
