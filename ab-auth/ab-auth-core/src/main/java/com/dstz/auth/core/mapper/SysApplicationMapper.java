package com.dstz.auth.core.mapper;

import com.dstz.auth.core.entity.SysApplication;
import com.dstz.base.mapper.AbBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * Applying the Mapper interface
 * </p>
 *
 */
@Mapper
public interface SysApplicationMapper extends AbBaseMapper<SysApplication> {

    /**
     * Determine if an alias exists
     *
     * @param subsystem
     * @return
     */
    Integer isExist(SysApplication subsystem);

    /**
     * Updated to default.
     */
    void updNoDefault();

    /**
     * Get the system according to the code
     * @return
     */
    SysApplication getByAlias(String systemAlias);
}
