package com.dstz.cms.core.manager.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dstz.base.common.utils.UserContextUtils;
import com.dstz.base.manager.impl.AbBaseManagerImpl;
import com.dstz.cms.core.entity.CmsFrequentUsed;
import com.dstz.cms.core.manager.CmsFrequentUsedManager;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Common process management General service implementation class
 *
 */
@Service("cmsFrequentUsedManager")
public class CmsFrequentUsedManagerImpl extends AbBaseManagerImpl<CmsFrequentUsed> implements CmsFrequentUsedManager {

    /**
     * Get all common processes of the current user
     *
     * @return List<CmsFrequentUsed> Common process collection
     */
    @Override
    public List<CmsFrequentUsed> list() {
        return selectByWrapper(Wrappers.<CmsFrequentUsed>lambdaQuery().eq(CmsFrequentUsed::getUserId, UserContextUtils.getUserId()));
    }

    /**
     * Add common processes in batches
     *
     * @param defIdList Common process ID collection
     */
    @Override
    public void saveBatch(List<String> defIdList) {
        defIdList.forEach(s -> create(new CmsFrequentUsed(UserContextUtils.getUserId(), s)));
    }
}
