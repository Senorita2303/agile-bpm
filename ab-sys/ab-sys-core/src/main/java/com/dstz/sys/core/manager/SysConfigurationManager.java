package com.dstz.sys.core.manager;

import com.dstz.base.manager.AbBaseManager;
import com.dstz.sys.core.entity.SysConfiguration;
import com.dstz.sys.rest.model.vo.SysConfigurationVO;

import java.util.List;

/**
 * <p>
 *  General business category
 * </p>
 *
 */
public interface SysConfigurationManager extends AbBaseManager<SysConfiguration> {

    /**
     * Get system configuration based on the code
     * @param code Configuration code
     * @return Configuration information
     */
    SysConfiguration getConfByCode(String code);


    /**
     * Save system configuration
     *
     * @param sysConfiguration Configuration information
     */
    void save(SysConfiguration sysConfiguration);

    /**
     * Delete configuration according to code
     * @param code Configuration code
     */
    void deleteByCode(String code);

    /**
     * Get system property collection
     * @return
     */
    List<SysConfigurationVO>  getSysProperties(String codes);

    /**
     * Disable Enable
     * @param code
     * @return
     */
    int changeEnableByCode(String code);

    /**
     * Modify login type
     * @param code
     * @return
     */
    int changeQuickLoginType(String code);
}
