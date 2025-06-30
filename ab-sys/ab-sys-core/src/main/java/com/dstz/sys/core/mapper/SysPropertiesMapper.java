package com.dstz.sys.core.mapper;

import com.dstz.sys.core.entity.SysProperties;
import com.dstz.base.mapper.AbBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * System Property Mapper Interface
 * </p>
 *
 */
@Mapper
public interface SysPropertiesMapper extends AbBaseMapper<SysProperties> {

    /**
     * Determine whether the attribute exists.
     *
     * @param sysProperties
     * @return
     */
    Integer isExist(SysProperties sysProperties);

    /**
     * Grouped list.
     *
     * @return
     */
    @Select("SELECT distinct group_ FROM SYS_PROPERTIES")
    List<String> getGroups();

}
