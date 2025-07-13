package com.dstz.auth.core.manager;

import com.dstz.auth.core.entity.SysApplication;
import com.dstz.auth.rest.model.vo.SysResourceTreeVO;
import com.dstz.base.manager.AbBaseManager;

import java.util.List;

/**
 * <p>
 * Application General business
 * </p>
 *
 */
public interface SysApplicationManager extends AbBaseManager<SysApplication> {
    /**
     * Determine whether the alias exists.
     *
     * @param subsystem
     * @return
     */
    boolean isExist(SysApplication subsystem);

    /**
     * Get the default subsystem.
     * 1.Get the system that the user has permission to use, or return null if the user does not have permission.
     * 2.If there is a permission subsystem, determine whether there is a default subsystem, and return if there is.
     * 3.Otherwise take the first one.
     *
     * @return
     */
    SysApplication getDefaultSystem(String userId);

    /**
     * Sets the default subsystem.
     * 1.If it is the default, cancel it.
     * 2.If non-default, set the default.
     *
     * @param systemId
     */
    void setDefaultSystem(String systemId);

    /**
     * Get the resource tree through the application system object
     *
     * @param sysApplication Application
     * @return Resource Tree Object
     */
     List<SysResourceTreeVO> getTreeDataByApplication(SysApplication sysApplication);

    /**
     * Get the current user's available system
     * @return
     */
    List<SysApplication> getCurrentUserSystem();
    /**
     * Get the system based on userId
     * @return
     */
    List<SysApplication> getSystemByUser(String userId);
    /**
     * Get the system according to the code
     * @return
     */
    SysApplication getByAlias(String code);


    /**
     * Update application by id
     */

    int updateById(SysApplication subsystem);
}
