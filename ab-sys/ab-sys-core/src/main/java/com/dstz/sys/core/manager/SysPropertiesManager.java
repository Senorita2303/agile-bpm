package com.dstz.sys.core.manager;

import com.dstz.base.manager.AbBaseManager;
import com.dstz.sys.core.entity.SysProperties;

/**
 * <p>
 * System properties General business class
 * </p>
 *
 */
public interface SysPropertiesManager extends AbBaseManager<SysProperties> {

    /**
     * Determine whether the alias exists.
     *
     * @param sysProperties
     * @return
     */
    boolean isExist(SysProperties sysProperties);

    /**
     * Reread the property configuration.
     */
    void reloadProperty();

}
